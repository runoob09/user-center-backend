package github.runoob09.entity.request;

import lombok.Data;

/**
 * @author runoob09
 * @date 2024年07月24日 23:30:40
 * @description
 */
@Data
public class UserLoginRequest {
    public String userAccount; // 账号
    public String userPassword; // 密码
}
