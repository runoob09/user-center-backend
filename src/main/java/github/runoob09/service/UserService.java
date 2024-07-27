package github.runoob09.service;

import github.runoob09.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import github.runoob09.entity.request.UserRegisterRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZJH
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-07-24 13:16:48
 */
public interface UserService extends IService<User> {
    /**
     * 执行用户注册的方法
     * @param userRegisterRequest 用户注册的请求封装类
     * @return
     */
    Long userRegister(UserRegisterRequest userRegisterRequest); // 用户注册

    /**
     * 执行登陆操作
     * @param userAccount 用户账号
     * @param userPassword 用户名
     * @return 用户对象
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);
}
