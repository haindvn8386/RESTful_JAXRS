package restful.jaxrs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restful.jaxrs.dto.UpdateUserDTO;
import restful.jaxrs.dto.UserDTO;
import restful.jaxrs.entity.User;
import restful.jaxrs.exception.ResourceNotFoundException;
import restful.jaxrs.mapper.UserMapper;
import restful.jaxrs.repository.UserRepository;
import restful.jaxrs.util.utility;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    //list all user
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    //select user by id
    public UserDTO getUserById(String id) {
        Long userId = utility.validateAndConvertId(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found with " + id));
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
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User is not found with " + id));
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        User userUpdate = userRepository.save(user);
        return userMapper.toDto(userUpdate);
    }

    //patch
    public UserDTO patchUser(String id, UpdateUserDTO userDTO) {
        Long userId = utility.validateAndConvertId(id);
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User is not found with " + id));

        if(userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if(userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        User userUpdate = userRepository.save(user);
        return userMapper.toDto(userUpdate);
    }

}
