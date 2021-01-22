package org.whatever.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    public int getPageElements() {
        return pageElements;
    }

    public void setPageElements(int pageElements) {
        this.pageElements = pageElements;
    }

    public int getSubscriptionPriceMonthly() {
        return subscriptionPriceMonthly;
    }

    public void setSubscriptionPriceMonthly(int subscriptionPriceMonthly) {
        this.subscriptionPriceMonthly = subscriptionPriceMonthly;
    }

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
