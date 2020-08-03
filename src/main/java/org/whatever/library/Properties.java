package org.whatever.library;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class Properties {

    @Getter
    @Value("${page.elements}")
    private static int pageElements;

}
