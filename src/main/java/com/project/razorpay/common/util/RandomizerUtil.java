package com.project.razorpay.common.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class RandomizerUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom(); // thread-safe

    public static String randomBase64(int length){
        byte[] buf = new byte[length];
        SECURE_RANDOM.nextBytes(buf);

        String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(buf); //generate url safe string
        log.info(encoded);

        return encoded;
    }

}
