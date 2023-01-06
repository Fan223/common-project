package fan.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import fan.consts.AuthConst;
import fan.exception.CustomException;
import fan.service.AuthService;
import fan.utils.AuthUtil;
import org.springframework.stereotype.Service;
import fan.utils.RedisUtil;
import fan.base.Response;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 权限接口实现类
 *
 * @author Fan
 * @since 2022/11/25 13:55
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private Producer producer;

    @Override
    public Response getCaptcha(HttpServletRequest request) {
        // 生成验证码字符串
        String captchaStr = producer.createText();

        BufferedImage bufferedImage = producer.createImage(captchaStr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 生成图片字节数组
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }

        // 转换为 base64, 生成图片验证码
        String captchaImg = "data:image/jpg;base64," + Base64Encoder.encode(byteArrayOutputStream.toByteArray());

        String ipAddress = AuthUtil.getIpAddress(request);
        String loginToken = UUID.randomUUID().toString();
        // 将验证码存入 Redis, 过期时间为2分钟
        RedisUtil.hashSet(AuthConst.CAPTCHA_KEY + ":" + ipAddress, loginToken, captchaStr, AuthConst.CAPTCHA_EXPIRE_TIME);

        return Response.success("生成验证码成功", MapUtil.builder().put("loginToken", loginToken)
                .put("captchaImg", captchaImg).build());
    }
}
