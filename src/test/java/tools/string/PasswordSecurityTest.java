package tools.string;

import org.junit.Assert;
import org.junit.Test;

import de.sp.tools.string.PasswordSecurity;
import de.sp.tools.string.SaltAndHash;

public class PasswordSecurityTest {

	@Test
	public void test() throws Exception {
		
		String password = "asv";
		
		SaltAndHash hashWithSalt = PasswordSecurity.getSaltedHash(password);
		
		boolean ok = PasswordSecurity.check(password, hashWithSalt);
		
		Assert.assertTrue(ok);
		
	}

}
