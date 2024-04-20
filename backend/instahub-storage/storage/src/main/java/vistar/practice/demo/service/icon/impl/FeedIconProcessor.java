package vistar.practice.demo.service.icon.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vistar.practice.demo.service.icon.IconProcessor;

@Component
public class FeedIconProcessor {

    @Value("${feed.width}")
    private int feedWidth;

    @Value("${feed.height}")
    private int feedHeight;

    @Value("${feed.format}")
    private String feedFormat;

    public IconProcessor.IconParameters getFeedIconParameters() {
        return new IconProcessor.IconParameters(feedWidth, feedHeight, feedFormat, false);
    }
}
