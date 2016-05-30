package com.github.totoCastaldi.stageTimesheet.controller;

import com.github.totoCastaldi.stageTimesheet.Entry;
import com.github.totoCastaldi.stageTimesheet.Main;
import com.github.totoCastaldi.stageTimesheet.UserEntries;
import com.github.totoCastaldi.stageTimesheet.UserToken;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.DELETE;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.http.Req;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            final String username = user.get();
            List<Entry> entries = sorted(username);
            return Lists.newArrayList(Collections2.transform(entries, new Function<Entry, String>() {
                @Nullable
                @Override
                public String apply(@Nullable Entry input) {

                    return sdf.format(input.getDate()) + " - " +
                            StringUtils.leftPad(String.valueOf(input.getOim()), 2, "0") + " - " +
                            StringUtils.leftPad(String.valueOf(input.getOum()), 2, "0") + " | " +
                            StringUtils.leftPad(String.valueOf(input.getOip()), 2, "0") + " - " +
                            StringUtils.leftPad(String.valueOf(input.getOup()), 2, "0") + " | " +
                            StringUtils.left(StringUtils.rightPad(input.getDescription(), 20, " "), 20)
                            ;
                }
            }));
        } else {
            return Lists.newArrayList();
        }

    }

    @GET
    public List<String> mostUsedDescription(String token) {
        final Optional<String> user = this.userToken.user(token);
        if (user.isPresent()) {
            final String username = user.get();
            final Collection<Entry> entryCollection = userEntries.get(username);
            final Map<String, Integer> counting = Maps.newHashMap();
            final Map<String, String> original = Maps.newHashMap();
            for (Entry e : entryCollection) {
                final String key = StringUtils.stripToEmpty(StringUtils.lowerCase(e.getDescription()));
                if (counting.containsKey(key)) {
                    counting.put(key, counting.get(key) + 1);
                } else {
                    counting.put(key, 1);
                }
                original.put(key, e.getDescription());
            }

            List<Integer> ranking = Lists.newArrayList(counting.values());
            Collections.sort(ranking);
            Collections.reverse(ranking);
            List<String> result = Lists.newArrayList();
            for (int i = 0; i < 3; i++) {
                if (ranking.size() > 0) {
                    int count = ranking.get(0);
                    final Map<String, Integer> withRanking = Maps.filterEntries(counting, new Predicate<Map.Entry<String, Integer>>() {
                        @Override
                        public boolean apply(@Nullable Map.Entry<String, Integer> input) {
                            if (input != null) {
                                return input.getValue() == count;
                            } else {
                                return false;
                            }
                        }
                    });
                    if (withRanking.size() > 0) {
                        final String key = withRanking.keySet().iterator().next();
                        result.add(original.get(key));
                        counting.remove(key);
                    }
                    ranking.remove(0);
                }
            }

            return result;
        } else {
            return Lists.newArrayList();
        }

    }

    @GET
    public String lastDescription(String token) {
        final Optional<String> user = this.userToken.user(token);
        if (user.isPresent()) {
            final String username = user.get();
            List<Entry> entries = sorted(username);

            if (Iterables.size(entries) > 0) {
                return entries.get(0).getDescription();
            }
        }

        return StringUtils.EMPTY;

    }

    private List<Entry> sorted(String username) {
        List<Entry> entries = Lists.newArrayList(userEntries.get(username));
        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return (int) (o2.getDate().getTime() - o1.getDate().getTime());
            }
        });
        return entries;
    }

    @POST
    public void entry(Req req) throws ParseException {
        final Map<String, Object> posted = req.posted();

        log.info("entry post {}", posted);

        final String token = String.valueOf(posted.get("token"));

        final Optional<String> user = this.userToken.user(token);

        log.info("user {}", user);

        int oim = Integer.parseInt(String.valueOf(posted.get("oim")));
        int oum = Integer.parseInt(String.valueOf(posted.get("oum")));
        int oip = Integer.parseInt(String.valueOf(posted.get("oip")));
        int oup = Integer.parseInt(String.valueOf(posted.get("oup")));

        //Trick for AppInventor
        oim --;
        oum --;
        oip --;
        oup --;

        String d = String.valueOf(posted.get("d"));
        String date = String.valueOf(posted.get("date"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Entry entry = Entry.of(oim, oum, oip, oup, d, sdf.parse(date));
        log.info("entry {}", entry);
        //, int oim, int oum, int oip, int oup, String d
        if (user.isPresent()) {
            final String username = user.get();
            this.userEntries.put(username, entry);
        }
    }

    @DELETE
    public void entry(String token, int position) {
        position --; //Trick for AppInventor
        final Optional<String> user = this.userToken.user(token);
        if (user.isPresent()) {
            final String username = user.get();
            List<Entry> entries = sorted(username);
            final Entry entry = entries.get(position);
            this.userEntries.remove(username, entry.getId());
        }
    }
}
