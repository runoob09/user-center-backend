package github.runoob09.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import github.runoob09.annotation.RequireRole;
import github.runoob09.constant.UserConstant;
import github.runoob09.entity.User;
import github.runoob09.request.UserRegisterRequest;
import github.runoob09.request.UserSearchRequest;
import github.runoob09.service.UserService;
import github.runoob09.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static github.runoob09.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author ZJH
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-07-24 13:16:48
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 账户名校验
     */
    private static final Pattern USER_ACCOUNT_CHECK = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{4,}$");
    /**
     * 密码校验
     */
    private static final Pattern USER_PASSWORD_CHECK = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,}$");
    /**
     * md5加密混淆
     */
    private final static String SALT = "X8s9D2Z3jK4nM6bR";

    /**
     * 执行用户注册操作
     *
     * @param userRegisterRequest 用户注册的请求封装类
     * @return 成功注册后的用户id
     */
    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        // 检查用户输入的数据
        if (StringUtils.isAllBlank(userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword(), userRegisterRequest.getCheckPassword())) {
            return -1L;
        }
        // 校验用户名
        if (!USER_ACCOUNT_CHECK.matcher(userRegisterRequest.getUserAccount()).matches()) {
            return -1L;
        }
        // 检查两次密码是否一致
        if (!userRegisterRequest.getUserPassword().equals(userRegisterRequest.getCheckPassword())) {
            return -1L;
        }
        // 校验密码是否合法
        if (!USER_PASSWORD_CHECK.matcher(userRegisterRequest.getUserPassword()).matches()) {
            return -1L;
        }
        // 校验两次密码是否正确
        if (!userRegisterRequest.getUserPassword().equals(userRegisterRequest.getCheckPassword())) {
            return -1L;
        }
        // 查询用户名是否已经存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userRegisterRequest.getUserAccount());
        User user = this.getOne(queryWrapper);
        if (user != null) {
            return -1L;
        }
        // 对密码进行加密
        String newPassword = DigestUtil.md5Hex((userRegisterRequest.getUserPassword() + "_" + SALT).getBytes());
        user = new User(userRegisterRequest.getUserAccount(), newPassword);
        // 存储用户
        save(user);
        return user.getId();
    }

    /**
     * 执行登陆操作
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request
     * @return 用户对象
     */
    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 检查参数不为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // 不能包含特殊字符
        if (!(USER_ACCOUNT_CHECK.matcher(userAccount).matches() && USER_PASSWORD_CHECK.matcher(userPassword).matches())) {
            return null;
        }
        // 从数据库中查询是否有对应的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, userPassword);
        User user = getOne(queryWrapper);
        if (user == null) {
            log.error("No user found with account: {} with provided password.", userAccount);
            return null;
        }
        // 存储用户信息
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 返回用户的脱敏信息
        return convertToSafeUser(user);
    }

    /**
     * 用户脱敏方法
     *
     * @param user
     * @return
     */
    @Override
    public User convertToSafeUser(User user) {
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setUserPassword(null);
        safeUser.setPhoneNumber(user.getPhoneNumber());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setIsDelete(user.getIsDelete());
        safeUser.setUserRole(user.getUserRole());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUpdateTime(user.getUpdateTime());
        return safeUser;
    }

    /**
     * 查询用户的服务类
     *
     * @param searchRequest 用户请求实体类
     * @return 查询到的用户列表
     */
    @RequireRole(roles = UserConstant.Role.ADMIN)
    @Override
    public List<User> searchUsers(UserSearchRequest searchRequest) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        if (searchRequest == null) {
            log.error("User search request can not be null");
            return null;
        }
        if (searchRequest.getUsername() != null) {
            queryWrapper = queryWrapper.like(User::getUsername, searchRequest.getUsername());
        }
        List<User> userList = list(queryWrapper);
        return userList.stream().map(this::convertToSafeUser).collect(Collectors.toList());
    }

    /**
     * 删除指定id的用户
     *
     * @param userId 用户的唯一id
     * @return 执行的状态
     */
    @RequireRole(roles = UserConstant.Role.ADMIN)
    @Override
    public Boolean deleteUser(Long userId) {
        if (userId == null) {
            log.error("You cannot use a null ID to delete a user, because the user ID cannot be null.");
            return false;
        }
        return removeById(userId);
    }
}