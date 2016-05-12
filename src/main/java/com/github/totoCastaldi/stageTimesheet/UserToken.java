package com.github.totoCastaldi.stageTimesheet;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import lombok.Synchronized;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;

/**
 * Created by toto on 10/05/16.
 */
@Singleton
public class UserToken {

    private final UserCredential userCredential;
    private final Map<String, String> tokens;

    @Inject
    public UserToken(
            UserCredential userCredential
    ) {
        this.userCredential = userCredential;
        this.tokens = Maps.newConcurrentMap();
    }

    @Synchronized
    public Optional<String> generate(String email, String password) throws IOException {
        if (this.userCredential.isValid(email, password)) {
            String random = StringUtils.EMPTY;
            while (StringUtils.isBlank(random) || tokens.containsKey(random)) {
                random = RandomStringUtils.randomAlphabetic(20);
            }
            tokens.put(random, email);
            return Optional.of(random);
        } else {
            return Optional.absent();
        }
    }

    public Optional<String> user(String token) {
        if (tokens.containsKey(token)) {
            return Optional.of(tokens.get(token));
        } else {
            return Optional.absent();
        }
    }
}
