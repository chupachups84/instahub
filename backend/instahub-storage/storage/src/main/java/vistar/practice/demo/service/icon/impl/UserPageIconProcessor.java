package vistar.practice.demo.service.icon.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vistar.practice.demo.service.icon.IconProcessor;

@Component
public class UserPageIconProcessor {

    @Value("${icon.width}")
    private int iconWidth;

    @Value("${icon.height}")
    private int iconHeight;

    @Value("${icon.format}")
    private String iconFormat;

    public IconProcessor.IconParameters getUserPageIconParameters() {
        return new IconProcessor.IconParameters(iconWidth, iconHeight, iconFormat, false);
    }
}
