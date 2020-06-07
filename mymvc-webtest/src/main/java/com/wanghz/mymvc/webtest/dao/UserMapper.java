package com.wanghz.mymvc.webtest.dao;


import com.wanghz.mymvc.annotation.Repository;
import com.wanghz.mymvc.webtest.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Repository("userMapper")
public interface UserMapper {
	User getUserByID(@Param("id") int id);

	List<User> getUserList();

	@Select("SELECT * from user WHERE Username=#{username}")
    User getUserByName(String username);
}
