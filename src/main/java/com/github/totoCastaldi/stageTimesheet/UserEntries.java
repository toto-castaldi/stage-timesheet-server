package com.github.totoCastaldi.stageTimesheet;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Synchronized;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Collection;

/**
 * Created by toto on 18/05/16.
 */
@Singleton
public class UserEntries {

    private long counter;
    private Multimap<String, Entry> storage;

    public UserEntries() {
        this.counter = 0;
        this.storage = ArrayListMultimap.create();
    }

    @Synchronized
    public void put(String userName, Entry entry) {
        entry.setId(++ counter);
        this.storage.put(userName,entry);
    }

    public Collection<Entry> get(String username) {
        return this.storage.get(username);
    }

    public void remove(String username, long id) {
        final Optional<Entry> entryOptional = Iterables.tryFind(storage.values(), new Predicate<Entry>() {
            @Override
            public boolean apply(@Nullable Entry input) {
                if (input != null && input.getId() == id) {
                    return true;
                }
                return false;
            }
        });
        if (entryOptional.isPresent()) {
            storage.remove(username, entryOptional.get());
        }
    }
}
