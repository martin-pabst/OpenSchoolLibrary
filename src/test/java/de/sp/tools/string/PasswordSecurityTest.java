package de.sp.tools.string;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Martin on 07.06.2017.
 */
public class PasswordSecurityTest {

    @Test
    public void getSaltedHash() throws Exception {

        String password = "asv";

        SaltAndHash hashWithSalt = PasswordSecurity.getSaltedHash(password);

        boolean ok = PasswordSecurity.check(password, hashWithSalt);

        Assert.assertTrue(ok);

    }

}