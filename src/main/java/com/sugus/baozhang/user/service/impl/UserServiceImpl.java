package com.sugus.baozhang.user.service.impl;

import com.sugus.baozhang.user.dto.User;
import com.sugus.baozhang.user.repository.UserRepository;
import com.sugus.baozhang.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUserName(String userName) {
        Optional<User> userOptional = userRepository.findById(userName);
        return userOptional.orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User save(User user) {
        if (null == user || StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getEmail())) {
            return null;
        }
        User.init(user);
        return userRepository.save(user);
    }
}
