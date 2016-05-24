package com.github.totoCastaldi.stageTimesheet;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Created by toto on 18/05/16.
 */
@Data
@RequiredArgsConstructor(staticName = "of")
@ToString
public class Entry {
    private final int oim;
    private final int oum;
    private final int oip;
    private final int oup;
    private final String description;
    private final Date date;
    private long id;
}
