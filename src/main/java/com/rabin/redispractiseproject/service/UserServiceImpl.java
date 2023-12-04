package com.rabin.redispractiseproject.service;

import com.rabin.redispractiseproject.dto.UserRequest;
import com.rabin.redispractiseproject.entity.UserEntity;
import com.rabin.redispractiseproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public String savingTheRecord(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        if (userRequest.getName().isEmpty() || userRequest.getAddress().isEmpty() || userRequest.getEmail().isEmpty() || userRequest.getPassword().isEmpty()) {
            return "unsuccessfully";
        }
        BeanUtils.copyProperties(userRequest, user);
        user.setRole("Client");
        userRepository.save(user);
        return "register successfully";
    }

    //    @Cacheable(cacheNames = "cache1", key = "'#key'")
    @Cacheable(cacheNames = "cache1", key = "'user'")
    @Override
    public List<UserRequest> listOfRecord() {
        List<UserEntity> userEntities = userRepository.findAll();
        log.info("Data from data base {}", userEntities);
        return userEntities.stream().map(userEntity -> {
            UserRequest userRequest = new UserRequest();
            BeanUtils.copyProperties(userEntity, userRequest);
            return userRequest;
        }).toList();
    }

    @Cacheable(cacheNames = "cache2", key = "'user'")
    @Override
    public UserRequest getRecordByName(String name) throws Exception {
        Optional<UserEntity> existUser = userRepository.findByName(name);
        if (existUser.isPresent()) {
            UserEntity user = existUser.get();
            log.info("user present in database {}", user);
            UserRequest userRequest = new UserRequest();
            BeanUtils.copyProperties(user, userRequest);
            return userRequest;
        }
        throw new Exception("user not found");
    }
}
