package github.runoob09.controller;

import github.runoob09.entity.User;
import github.runoob09.request.UserLoginRequest;
import github.runoob09.request.UserRegisterRequest;
import github.runoob09.request.UserSearchRequest;
import github.runoob09.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author runoob09
 * @date 2024年07月28日 09:28:01
 * @description
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 执行用户登录操作
     *
     * @param loginRequest 用户登录的请求对象
     * @param request      请求对象
     * @return 查询到的用户信息
     */
    @PostMapping("login")
    public User userLogin(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(loginRequest.getUserAccount(), loginRequest.getUserPassword())) {
            return null;
        }
        return userService.doLogin(loginRequest.getUserAccount(), loginRequest.getUserPassword(), request);
    }

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 用户注册的请求对象
     * @return 用户的新id
     */
    @PostMapping("register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        return userService.userRegister(userRegisterRequest);
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @PostMapping("delete/{id}")
    public Boolean deleteUser(@PathVariable("id") Long id) {
        if (id == null) {
            log.error("user id cannot be null");
            return null;
        }
        return userService.deleteUser(id);
    }

    /**
     * 搜索用户
     *
     * @param searchRequest 搜索用户的请求对象
     * @return 搜索到的用户列表
     */
    @GetMapping("search")
    public List<User> searchUser(UserSearchRequest searchRequest) {
        if (searchRequest == null) {
            log.error("search request cannot be null");
            return null;
        }
        return userService.searchUsers(searchRequest);
    }

    @GetMapping("currentUser")
    public User currentUser(HttpServletRequest request) {
        return userService.currentUser(request);
    }

    @GetMapping("logout")
    public Boolean logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
