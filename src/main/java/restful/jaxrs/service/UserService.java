package restful.jaxrs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import restful.jaxrs.dto.UpdateUserDTO;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.entity.User;
import restful.jaxrs.exception.ResourceNotFoundException;
import restful.jaxrs.mapper.UserMapper;
import restful.jaxrs.repository.UserRepository;
import restful.jaxrs.util.utility;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${pagination.number-of-page}")
    private int pageSize;

    @Value("${redis.timeout}")
    private int redis_timeout;


    //list all user
    public Page<UserDTO> getAllUsers(int pageNumber, String sortBy, String searchKey) {

        //create redis key from parameter
        String cacheKey = "users:page:" + pageNumber + ":sort:" + (sortBy != null ? sortBy : "none") + ":search:" + (searchKey != null ? searchKey.toLowerCase() : "none");
        try {
            @SuppressWarnings("unchecked") List<UserDTO> content = (List<UserDTO>) redisTemplate.opsForHash().get(cacheKey, "content");
            Object totalObj = redisTemplate.opsForHash().get(cacheKey, "totalElements");
            Long total = null;
            if (totalObj != null) {
                if (totalObj instanceof Long) {
                    total = (Long) totalObj;
                } else if (totalObj instanceof Integer) {
                    //convert from Integer to Long
                    total = ((Integer) totalObj).longValue();
                }
            }

            if (content != null && total != null) {
                Sort sort = StringUtils.hasText(sortBy) ? Sort.by(sortBy) : Sort.unsorted();
                Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return new PageImpl<>(content, pageable, total);
            }
        } catch (Exception e) {
            System.err.println("Serialization error: " + e.getMessage());
            e.printStackTrace();
        }

        Sort sort = StringUtils.hasText(sortBy) ? Sort.by(sortBy) : Sort.unsorted();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<User> userPage;
        if (StringUtils.hasText(searchKey)) {
            userPage = userRepository.findByUserNameContainingIgnoreCase(searchKey, pageable);
            //userPage = userRepository.findByUserNameStartingWithIgnoreCase(searchKey, pageable);
//            String normalizedSearchKey = searchKey.toUpperCase();
//            userPage = userRepository.findByNormalizedNormalizedUserNameContaining(normalizedSearchKey, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        List<UserDTO> contentDto = userPage.getContent().stream().map(userMapper::toDto).collect(Collectors.toList());
        Page<UserDTO> resultPage = new PageImpl<>(contentDto, pageable, userPage.getTotalElements());

        try {
            redisTemplate.opsForHash().put(cacheKey, "content", contentDto);
            redisTemplate.opsForHash().put(cacheKey, "totalElements", userPage.getTotalElements());
            redisTemplate.expire(cacheKey, redis_timeout, TimeUnit.MINUTES);
        } catch (Exception e) {
            System.err.println("Serialization error: " + e.getMessage());
            e.printStackTrace();
        }

        return resultPage;
      /*
        String cacheKey = "users:page:" + pageNumber + ":sort:" + (sortBy != null ? sortBy : "none") + ":search:" + (searchKey != null ? searchKey.toLowerCase() : "none");

        //check data from Redis
        Object cachedObject = redisTemplate.opsForValue().get(cacheKey);
        Page<User> cachedUsers = null;
        if (cachedObject != null) {
            try {
                //convert from Object to Map
                @SuppressWarnings("unchecked")
                Map<String, Object> cachedMap = (Map<String, Object>)  cachedObject;
                //Extract content, pageable, total
                @SuppressWarnings("unchecked")
                List<User> content = objectMapper.convertValue(cachedMap.get("content"), new com.fasterxml.jackson.core.type.TypeReference<List<User>>() {});
                long total = ((Number) cachedMap.get("totalElements")).longValue();
                Sort sort = StringUtils.hasText(sortBy) ? Sort.by(sortBy) : Sort.unsorted();
                Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                //Recreate Page<User>
                 cachedUsers = new PageImpl<>(content, pageable, total);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       // if cached data, return to controller
        if (cachedUsers != null) {
            return  cachedUsers.map(userMapper::toDto);
        }

        //create sortBy
        Sort sort = StringUtils.hasText(sortBy) ? Sort.by(sortBy) : Sort.unsorted();
        // create Pageable
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        if (StringUtils.hasText(searchKey)) {
            cachedUsers = userRepository.findByUserNameContainingIgnoreCase(searchKey, pageable);
        } else {
            cachedUsers = userRepository.findAll(pageable);
        }

        //save to Redis with a time to live
        redisTemplate.opsForValue().set(cacheKey, cachedUsers, 1, TimeUnit.MINUTES);
        return cachedUsers.map(userMapper::toDto);

         */

    }

    //select user by id
    public UserDTO getUserById(String id) {
        Long userId = utility.validateAndConvertId(id);

        //create Redis key
        String key = "user_" + id;
        User user;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            try {
                user = objectMapper.convertValue(cached, User.class);
                System.out.println("Retrieved from Redis: " + key);
                return userMapper.toDto(user);
            } catch (Exception e) {
                System.err.println("Error converting Redis data to user for key " + key + ": " + e.getMessage());
            }
        } else {
            System.out.println("No data found in Redis for key: " + key);
        }

        user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found with " + id));
        redisTemplate.opsForValue().set(key, user, 5, TimeUnit.MINUTES);
        return userMapper.toDto(user);
    }

    //add user
    public UserDTO addUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    //delete
    public UserDTO deleteUser(String id) {
        Long userId = utility.validateAndConvertId(id);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found with " + id));
        userRepository.delete(user);
        return userMapper.toDto(user);
    }

    //update
    public UserDTO updateUser(String id, UserDTO userDTO) {
        Long userId = utility.validateAndConvertId(id);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found with " + id));
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        User userUpdate = userRepository.save(user);
        return userMapper.toDto(userUpdate);
    }

    //patch
    public UserDTO patchUser(String id, UpdateUserDTO userDTO) {
        Long userId = utility.validateAndConvertId(id);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found with " + id));

        if (userDTO.getName() != null) {
            user.setUserName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        User userUpdate = userRepository.save(user);
        return userMapper.toDto(userUpdate);
    }


}
