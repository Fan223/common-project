package fan.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置类
 *
 * @author Fan
 * @since 2022/11/23 16:17
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        // 验证码高度
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "40");
        // 验证码宽度
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "170");
        // 验证码边框
        properties.setProperty(Constants.KAPTCHA_BORDER, "no");
        // 验证码个数
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "5");
        // 字体间隔
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "8");
        // 字体大小
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");
        // 干扰线颜色
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // 干扰实现类
        // properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式
        // properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.WaterRipple");
        // 文字来源
        // properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "0123456789");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
