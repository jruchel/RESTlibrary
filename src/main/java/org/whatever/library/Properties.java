package org.whatever.library;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Properties {

    @Value("${page.elements}")
    private int pageElements;

    @Value("${subscription.price.monthly.usd}")
    private int subscriptionPriceMonthly;

    private static Properties instance;

    @Autowired
    public void setInstance(Properties properties) {
        Properties.instance = properties;
    }

    public static Properties getInstance() {
        return instance;
    }
}
