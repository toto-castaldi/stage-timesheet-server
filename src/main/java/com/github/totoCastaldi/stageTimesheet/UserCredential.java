package com.github.totoCastaldi.stageTimesheet;

/**
 * Created by toto on 10/05/16.
 */
public interface UserCredential {

    boolean isValid(String email, String password);
}
