package com.github.totoCastaldi.stageTimesheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Created by toto on 18/05/16.
 */
@Data
@AllArgsConstructor(staticName = "of")
@ToString
public class Entry {
    private int oim;
    private int oum;
    private int oip;
    private int oup;
    private String description;
}
