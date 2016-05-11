package com.github.totoCastaldi.stageTimesheet;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;

import java.util.List;
import java.util.Map;

/**
 * Created by toto on 10/05/16.
 */
@Controller
public class TimesheetController {

    private final UserToken userToken;

    public TimesheetController(
    ) {
        this.userToken = Main.injector.getInstance(UserToken.class);
    }

    @GET
    public List<String> entries(String token) {
        final Optional<String> user = this.userToken.user(token);
        if (user.isPresent()) {
            final String username = user.get();
            return Lists.newArrayList("ciao " + username, "primo inserimento", "secondo inserimento");
        } else {
            return Lists.newArrayList();
        }

    }
}
