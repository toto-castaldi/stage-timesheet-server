package com.github.totoCastaldi.stageTimesheet.controller;

import com.github.totoCastaldi.stageTimesheet.Main;
import com.github.totoCastaldi.stageTimesheet.UserCredential;
import com.github.totoCastaldi.stageTimesheet.UserToken;
import com.google.common.base.Optional;
import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.POST;
import org.rapidoid.annotation.PUT;
import org.rapidoid.http.Req;

import java.io.IOException;
import java.util.Map;

/**
 * Created by toto on 10/05/16.
 */
@Controller
public class CredentialController {

    private final UserToken userToken;
    private final UserCredential userCredential;

    public CredentialController(
    ) {
        this.userToken = Main.injector.getInstance(UserToken.class);
        this.userCredential = Main.injector.getInstance(UserCredential.class);
    }

    @POST
    public String login(Req req) throws IOException {
        final Map<String, Object> posted = req.posted();
        if (posted.size() > 0) {
            final Map.Entry<String, Object> param = posted.entrySet().iterator().next();
            final String email = param.getKey();
            final String password = String.valueOf(param.getValue());

            final Optional<String> tokenOptional = this.userToken.generate(email, password);

            return tokenOptional.or("KO");
        } else {
            return "KO";
        }
    }

    @PUT
    public void changePassword(Req req) {
        final Map<String, Object> posted = req.posted();
        if (posted.size() > 0) {
            final Map.Entry<String, Object> param = posted.entrySet().iterator().next();
            final String token = param.getKey();
            final String newPassword = String.valueOf(param.getValue());

            final Optional<String> user = userToken.user(token);
            if (user.isPresent()) {
                userCredential.changePassword(user.get(), newPassword);
            }
        }
    }
}
