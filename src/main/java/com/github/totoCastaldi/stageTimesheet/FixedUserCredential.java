package com.github.totoCastaldi.stageTimesheet;

import org.apache.commons.lang.StringUtils;

/**
 * Created by toto on 10/05/16.
 */
public class FixedUserCredential implements UserCredential {
    @Override
    public boolean isValid(String email, String password) {
        if (StringUtils.containsIgnoreCase(email, "toto.castaldi@gmail.com") && StringUtils.equals(password, "aaaa")) return true;
        return false;
    }
}
