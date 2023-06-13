package com.admin4j.framework.kaptcha;

import com.admin4j.framework.kaptcha.propertie.KaptchaProperties;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.util.StringUtils;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * @author andanyang
 * @since 2023/6/13 14:10
 */
public class KaptchaFactory {

    public static DefaultKaptcha create(KaptchaProperties kaptchaProperties) {

        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框 默认为true 我们可以自己设置yes，no
        properties.setProperty(KAPTCHA_BORDER, kaptchaProperties.isBorder() ? "yes" : "no");
        // 边框颜色 默认为Color.BLACK
        properties.setProperty(KAPTCHA_BORDER_COLOR, kaptchaProperties.getBorderColor());
        // 验证码文本字符颜色 默认为Color.BLACK
        if (StringUtils.hasText(kaptchaProperties.getColor())) {
            properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, kaptchaProperties.getColor());
        }

        properties.setProperty(KAPTCHA_BORDER_THICKNESS, String.valueOf(kaptchaProperties.getBorderThickness()));
        properties.setProperty(KAPTCHA_BACKGROUND_CLR_FROM, kaptchaProperties.getBackgroundClrFrom());
        properties.setProperty(KAPTCHA_BACKGROUND_CLR_TO, kaptchaProperties.getBackgroundClrTo());
        // 验证码图片宽度 默认为200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, String.valueOf(kaptchaProperties.getWidth()));
        // 验证码图片高度 默认为50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, String.valueOf(kaptchaProperties.getHeight()));
        // 验证码文本字符大小 默认为40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, String.valueOf(kaptchaProperties.getFontSize()));
        // KAPTCHA_SESSION_KEY
//        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 验证码文本生成器
        if (StringUtils.hasText(kaptchaProperties.getTextProducerImpl())) {
            properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, kaptchaProperties.getTextProducerImpl());
        }

        // 验证码文本字符间距 默认为2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, String.valueOf(kaptchaProperties.getCharSpace()));
        // 验证码文本字符长度 默认为5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, String.valueOf(kaptchaProperties.getCharLength()));
        // 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, kaptchaProperties.getFontNames());
        // 验证码噪点颜色 默认为Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, kaptchaProperties.getNoiseColor());
        // 干扰实现类
        if (StringUtils.hasText(kaptchaProperties.getNoiseImpl())) {
            properties.setProperty(KAPTCHA_NOISE_IMPL, kaptchaProperties.getNoiseImpl());
        }

        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple
        // 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy
        // 阴影com.google.code.kaptcha.impl.ShadowGimpy
        if (StringUtils.hasText(kaptchaProperties.getObscurificatorImpl())) {
            properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, kaptchaProperties.getObscurificatorImpl());
        }

        Config config = new KaptchaConfig(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
