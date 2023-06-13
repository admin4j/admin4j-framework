package com.admin4j.framework.kaptcha.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andanyang
 * @since 2023/6/13 13:57
 */
@ConfigurationProperties(prefix = "admin4j.kaptcha")
@Data
public class KaptchaProperties {

    /**
     * 是否有边框 默认为true
     */
    private boolean border = true;

    /**
     * 边框颜色 默认为Color.BLACK.可以试RGB，105,179,90
     */
    private String borderColor = "black";

    /**
     * 厚度
     */
    private int borderThickness = 1;
    /**
     * 验证码文本字符颜色 默认为null 随机.可以试RGB，105,179,90
     */
    private String color;

    private String backgroundClrFrom = "lightGray";
    private String backgroundClrTo = "white";


    /**
     * 验证码图片宽度 默认为160
     */
    private int width = 160;
    /**
     * 验证码图片高度 默认为60
     */
    private int height = 60;
    /**
     * 验证码文本字符大小 默认为40
     */
    private int fontSize = 40;
    /**
     * 验证码文本字符长度 默认为4
     */
    private int charLength = 4;
    /**
     * 验证码文本字符间距 默认为2
     */
    private int charSpace = 2;
    /**
     * 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
     */
    private String fontNames = "Arial,Courier";
    /**
     * 验证码噪点颜色
     */
    private String noiseColor = "white";
    /**
     * 干扰实现类
     */
    private String noiseImpl = "com.google.code.kaptcha.impl.NoNoise";
    /**
     * 验证码文本生成器
     */
    private String textProducerImpl = "com.admin4j.framework.kaptcha.KaptchaDigitCalculateCreator";

    private int textProducerCharSpace = 2;
    /**
     * 图片样式
     * 水纹com.google.code.kaptcha.impl.WaterRipple
     * 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy
     * 阴影com.google.code.kaptcha.impl.ShadowGimpy
     */
    private String obscurificatorImpl = "com.google.code.kaptcha.impl.WaterRipple";
}
