package com.github.totoCastaldi.stageTimesheet;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by toto on 12/05/16.
 */
@Singleton
public class FileUserCredential implements UserCredential {

    private final Map<String, String> newPasswords;

    public FileUserCredential(

    ) {
        this.newPasswords = Maps.newConcurrentMap();
    }

    @Override
    public boolean isValid(String email, String password) throws IOException {
        final Properties p = new Properties();
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("credentials.properties");
        p.load(inputStream);

        final String systemPassword;
        if (newPasswords.containsKey(email)) {
            systemPassword = newPasswords.get(email);
        } else {
            systemPassword = p.getProperty(email);
        }

        if (p.containsKey(email) && StringUtils.equals(systemPassword, password)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void changePassword(String email, String newPassword) {
        newPasswords.put(email, newPassword);
    }
}
