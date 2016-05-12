package com.github.totoCastaldi.stageTimesheet;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by toto on 10/05/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserTokenTest {

    private UserToken userToken;

    @Mock
    private UserCredential userCredential;
    private String email0 = "toto.castaldi@gmail.com";
    private String password0 = "secret";

    private String email1 = "antonio@skillbill.it";
    private String password1 = "hidden";

    @Before
    public void setup() throws IOException {
        Mockito.when(userCredential.isValid(email0, password0)).thenReturn(true);
        Mockito.when(userCredential.isValid(email1, password1)).thenReturn(true);

        userToken = new UserToken(
                userCredential
        );
    }

    @Test
    public void testLogin() throws IOException {
        final Optional<String> token = userToken.generate(email0, password0);

        assertThat(token, is(not(nullValue())));
        assertThat(token.isPresent(), is(true));

        final Optional<String> wrongToken = userToken.generate(email0, password0 + "a");

        assertThat(wrongToken, is(not(nullValue())));
        assertThat(wrongToken.isPresent(), is(false));

        final Optional<String> secondToken = userToken.generate(email1, password1);

        assertThat(token.get(), is(not(equalTo(secondToken.get()))));
    }

    @Test
    public void testUser() throws IOException {
        final Optional<String> token = userToken.generate(email0, password0);

        final Optional<String> user = userToken.user(token.get());

        assertThat(user, is(not(nullValue())));
        assertThat(user.isPresent(), is(true));
        assertThat(user.get(), is(equalTo(email0)));

        final Optional<String> wrongToken = userToken.user(token.get() + "a");

        assertThat(wrongToken, is(not(nullValue())));
        assertThat(wrongToken.isPresent(), is(false));

    }
}
