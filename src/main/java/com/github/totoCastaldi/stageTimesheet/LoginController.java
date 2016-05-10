package com.github.totoCastaldi.stageTimesheet;

import com.google.common.base.Optional;
import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;

import java.util.Map;

/**
 * Created by toto on 10/05/16.
 */
@Controller
public class LoginController {

    private final UserToken userToken;

    public LoginController(
    ) {
        this.userToken = Main.injector.getInstance(UserToken.class);
    }

    @POST
    public String login(Req req) {
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
}
