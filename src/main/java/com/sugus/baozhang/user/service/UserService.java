package com.sugus.baozhang.user.service;

import com.sugus.baozhang.user.dto.User;

public interface UserService {

    User findByUserName(String userName);

    User findByEmail(String email);

    User save(User user);

}
