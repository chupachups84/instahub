package vistar.practice.demo.service.icon.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vistar.practice.demo.service.icon.IconProcessor;

@Component
public class AvatarProcessor {

    @Value("${avatar.width}")
    private int avatarWidth;

    @Value("${avatar.height}")
    private int avatarHeight;

    @Value("${avatar.format}")
    private String avatarFormat;

    public IconProcessor.IconParameters getAvatarParameters() {
        return new IconProcessor.IconParameters(avatarWidth, avatarHeight, avatarFormat, true);
    }
}
