package com.github.totoCastaldi.stageTimesheet;

import java.io.IOException;

/**
 * Created by toto on 10/05/16.
 */
public interface UserCredential {

    boolean isValid(String email, String password) throws IOException;

    void changePassword(String email, String newPassword);
}
