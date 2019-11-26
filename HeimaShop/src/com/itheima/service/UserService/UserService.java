package com.itheima.service.UserService;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;

import java.sql.SQLException;

public class UserService {
    public boolean register(User user) {
        UserDao dao = new UserDao();
        int num = 0;
        try {
            num = dao.register(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num > 0 ? true : false;
    }

}
