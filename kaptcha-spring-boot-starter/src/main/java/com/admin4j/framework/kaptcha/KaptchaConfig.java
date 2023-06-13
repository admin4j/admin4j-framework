package com.admin4j.framework.kaptcha;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.util.ConfigHelper;

import java.awt.*;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andanyang
 * @since 2023/6/13 15:20
 */
public class KaptchaConfig extends Config {

    private ConfigHelper helper;

    public KaptchaConfig(Properties properties) {
        super(properties);
        this.helper = new ConfigHelper();
    }

//    @Override
//    public Color getBackgroundColorFrom() {
//        String paramName = Constants.KAPTCHA_BACKGROUND_CLR_FROM;
//        String paramValue = getProperties().getProperty(paramName);
//        Random rnd = ThreadLocalRandom.current();
//        return helper.getColor(paramName, paramValue, Color.LIGHT_GRAY);
//    }

    static Color[] FONT_COLORS = new Color[]{
            Color.BLACK, Color.RED, Color.BLUE, Color.DARK_GRAY, Color.PINK, Color.CYAN, Color.GREEN,
            Color.ORANGE, Color.YELLOW
    };

    @Override
    public Color getTextProducerFontColor() {
        String paramName = Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR;
        String paramValue = this.getProperties().getProperty(paramName);
        Random random = ThreadLocalRandom.current();
        int i = random.nextInt(FONT_COLORS.length);
        return this.helper.getColor(paramName, paramValue, FONT_COLORS[i]);
    }
}
