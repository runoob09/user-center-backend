package github.runoob09.service.impl;

import github.runoob09.entity.User;
import github.runoob09.entity.request.UserRegisterRequest;
import github.runoob09.mapper.UserMapper;
import github.runoob09.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterRequest userRegisterRequest;

    @BeforeEach
    void setUp() {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUserAccount("user123");
        userRegisterRequest.setUserPassword("Password1");
        userRegisterRequest.setCheckPassword("Password1");
    }

    @Test
    void testUserRegister_UserAlreadyExists() {
        User existingUser = new User();
        when(userMapper.selectOne(any())).thenReturn(existingUser);

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, times(1)).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_BlankFields() {
        userRegisterRequest.setUserAccount("");
        userRegisterRequest.setUserPassword("");
        userRegisterRequest.setCheckPassword("");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_PasswordsNotMatching() {
        userRegisterRequest.setCheckPassword("DifferentPassword");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_InvalidUsername() {
        userRegisterRequest.setUserAccount("us");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_InvalidPassword() {
        userRegisterRequest.setUserPassword("password");
        userRegisterRequest.setCheckPassword("password");

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(-1L, result);

        verify(userMapper, never()).selectOne(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void testUserRegister_Success() {
        when(userMapper.selectOne(any())).thenReturn(null);
        when(userMapper.insert(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });

        Long result = userService.userRegister(userRegisterRequest);
        assertEquals(1L, result);

        verify(userMapper, times(1)).selectOne(any());
        verify(userMapper, times(1)).insert(any());
    }
}
