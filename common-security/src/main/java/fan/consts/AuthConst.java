package fan.consts;

/**
 * 权限枚举类
 *
 * @author Fan
 * @since 2022/11/25 23:30
 */
public class AuthConst {

    public static final String CAPTCHA_KEY = "Captcha";

    public static final long CAPTCHA_EXPIRE_TIME = 60 * 2L;

    public static final String AUTH_KEY = "Authorization";

    public static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3L;
}
