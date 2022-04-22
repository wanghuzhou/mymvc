package com.wanghz.mymvc.webtest.service;

import com.wanghz.mymvc.annotation.Autowired;
import com.wanghz.mymvc.annotation.Service;
import com.wanghz.mymvc.webtest.dao.UserMapper;
import com.wanghz.mymvc.webtest.entity.User;

@Service("userService")
public class UserService {

    @Autowired("userMapper")
    private UserMapper userMapper;

    public User getUserByID(int id){
        return userMapper.getUserByID(id);
    }
}
