package com.github.totoCastaldi.stageTimesheet;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import javax.inject.Singleton;
import java.util.Collection;

/**
 * Created by toto on 18/05/16.
 */
@Singleton
public class UserEntries {

    private Multimap<String, Entry> storage;

    public UserEntries() {
        this.storage = ArrayListMultimap.create();
    }

    public void put(String userName, Entry entry) {
        this.storage.put(userName,entry);
    }

    public Collection<Entry> get(String username) {
        return this.storage.get(username);
    }
}
