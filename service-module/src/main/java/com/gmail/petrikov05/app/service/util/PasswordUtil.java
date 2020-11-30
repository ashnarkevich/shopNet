package com.gmail.petrikov05.app.service.util;

import net.bytebuddy.utility.RandomString;

public class PasswordUtil {

    public static String generatePassword() {
        return RandomString.make(5);
    }

}
