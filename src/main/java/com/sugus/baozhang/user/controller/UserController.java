package com.sugus.baozhang.user.controller;

import com.sugus.baozhang.common.domain.Result;
import com.sugus.baozhang.common.utils.ResultUtil;
import com.sugus.baozhang.user.dto.User;
import com.sugus.baozhang.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/save")
    public Result save(User user) {
        return ResultUtil.success(userService.save(user));
    }

}
