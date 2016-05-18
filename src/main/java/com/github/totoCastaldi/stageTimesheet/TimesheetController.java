package com.github.totoCastaldi.stageTimesheet;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by toto on 10/05/16.
 */
@Controller
@Slf4j
public class TimesheetController {

    private final UserToken userToken;
    private final UserEntries userEntries;

    public TimesheetController(
    ) {
        this.userToken = Main.injector.getInstance(UserToken.class);
        this.userEntries = Main.injector.getInstance(UserEntries.class);
    }

    @GET
    public List<String> entries(String token) {
        final Optional<String> user = this.userToken.user(token);
        if (user.isPresent()) {
            final String username = user.get();
            return Lists.newArrayList(Collections2.transform(userEntries.get(username), new Function<Entry, String>() {
                @Nullable
                @Override
                public String apply(@Nullable Entry input) {
                    return  StringUtils.leftPad(String.valueOf(input.getOim()), 2, "0") + " - " +
                            StringUtils.leftPad(String.valueOf(input.getOum()), 2, "0") + " | " +
                            StringUtils.leftPad(String.valueOf(input.getOip()), 2, "0") + " - " +
                            StringUtils.leftPad(String.valueOf(input.getOip()), 2, "0") + " | " +
                            StringUtils.left(StringUtils.rightPad(input.getDescription(), 20, " "), 20)
                            ;
                }
            }));
        } else {
            return Lists.newArrayList();
        }

    }

    @POST
    public void entry(Req req) {
        final Map<String, Object> posted = req.posted();
        String token = String.valueOf(posted.get("token"));
        int oim = Integer.parseInt(String.valueOf(posted.get("oim")));
        int oum = Integer.parseInt(String.valueOf(posted.get("oum")));
        int oip = Integer.parseInt(String.valueOf(posted.get("oip")));
        int oup = Integer.parseInt(String.valueOf(posted.get("oup")));
        String d = String.valueOf(posted.get("d"));
        final Entry entry = Entry.of(oim, oum, oip, oup, d);
        log.info("entry {}", entry);
        //, int oim, int oum, int oip, int oup, String d
        final Optional<String> user = this.userToken.user(token);
        if (user.isPresent()) {
            final String username = user.get();
            this.userEntries.put(username, entry);
        }

    }
}
