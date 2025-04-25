package restful.jr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import restful.jr.dto.UpdateUserDTO;
import restful.jr.dto.UserDTO;
import restful.jr.entity.User;
import restful.jr.exception.ResourceNotFoundException;
import restful.jr.mapper.UserMapper;
import restful.jr.repository.UserRepository;
import restful.jr.util.utility;

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


    /**
     * Retrieves a paginated list of all users, with optional sorting and search functionality.
     * Results are cached in Redis to improve performance for subsequent requests.
     *
     * @param pageNumber the page number to retrieve (1-based index)
     * @param sortBy     the field to sort by (optional, defaults to unsorted if null or empty)
     * @param searchKey  the keyword to filter users by username (optional, case-insensitive)
     * @return a {@link Page} containing a list of {@link UserDTO} objects, along with pagination metadata
     */
    public Page<UserDTO> getAllUsers(int pageNumber, String sortBy, String searchKey) {
        // Create a unique Redis cache key based on method parameters
        String cacheKey = "users:page:" + pageNumber + ":sort:" + (sortBy != null ? sortBy : "none") + ":search:" + (searchKey != null ? searchKey.toLowerCase() : "none");

        // Attempt to retrieve cached data from Redis
        try {
            @SuppressWarnings("unchecked")
            List<UserDTO> content = (List<UserDTO>) redisTemplate.opsForHash().get(cacheKey, "content");
            Object totalObj = redisTemplate.opsForHash().get(cacheKey, "totalElements");
            Long total = null;

            // Handle type conversion for total elements
            if (totalObj != null) {
                if (totalObj instanceof Long) {
                    total = (Long) totalObj;
                } else if (totalObj instanceof Integer) {
                    total = ((Integer) totalObj).longValue();
                }
            }

            // Return cached data if available
            if (content != null && total != null) {
                Sort sort = StringUtils.hasText(sortBy) ? Sort.by(sortBy) : Sort.unsorted();
                Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
                return new PageImpl<>(content, pageable, total);
            }
        } catch (Exception e) {
            // Log any serialization errors during cache retrieval
            System.err.println("Serialization error: " + e.getMessage());
            e.printStackTrace();
        }

        // Define sorting and pagination parameters
        Sort sort = StringUtils.hasText(sortBy) ? Sort.by(sortBy) : Sort.unsorted();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        // Fetch users from the database based on search criteria
        Page<User> userPage;
        if (StringUtils.hasText(searchKey)) {
            userPage = userRepository.findByUsernameContainingIgnoreCase(searchKey, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        // Convert User entities to UserDTOs
        List<UserDTO> contentDto = userPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        Page<UserDTO> resultPage = new PageImpl<>(contentDto, pageable, userPage.getTotalElements());

        // Cache the results in Redis for future requests
        try {
            redisTemplate.opsForHash().put(cacheKey, "content", contentDto);
            redisTemplate.opsForHash().put(cacheKey, "totalElements", userPage.getTotalElements());
            redisTemplate.expire(cacheKey, redis_timeout, TimeUnit.MINUTES);
        } catch (Exception e) {
            // Log any serialization errors during cache storage
            System.err.println("Serialization error: " + e.getMessage());
            e.printStackTrace();
        }

        return resultPage;
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
        UserDTO userDTO = userMapper.toDto(user);
        return userDTO;
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
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        User userUpdate = userRepository.save(user);
        return userMapper.toDto(userUpdate);
    }

    //patch
    public UserDTO patchUser(String id, UpdateUserDTO userDTO) {
        Long userId = utility.validateAndConvertId(id);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found with " + id));

        if (userDTO.getName() != null) {
            user.setUsername(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        User userUpdate = userRepository.save(user);
        return userMapper.toDto(userUpdate);
    }

    public UserDetailsService UserServiceDetail() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
