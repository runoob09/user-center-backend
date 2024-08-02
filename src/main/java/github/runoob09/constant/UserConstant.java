package github.runoob09.constant;

/**
 * @author runoob09
 * @date 2024年08月01日 09:24:51
 * @description
 */
public class UserConstant {
    /**
     * 用户性别常量
     */
    public static class Gender {
        /**
         * 男
         */
        public static final Integer MALE = 0;
        /**
         * 女
         */
        public static final Integer FEMALE = 1;
        public static final Integer UNKNOWN = 2;
    }

    /**
     * 用户状态
     */
    public static class Status {
        /**
         * 账户正常
         */
        public static final Integer ACTIVE = 0;
        /**
         * 账户被封禁
         */
        public static final Integer BANNED = 1;
        /**
         * 账户停用
         */
        public static final Integer DEACTIVATE = 2;
    }

    /**
     * 用户角色
     */
    public static class Role {
        /**
         * 普通用户
         */
        public static final Integer USER = 0;
        /**
         * 管理员用户
         */
        public static final Integer ADMIN = 1;
    }
}
