package com.psp.paypalservice.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Utils {

    public static String generateId() {
        return String.format("%.0f", (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L));
    }

}
