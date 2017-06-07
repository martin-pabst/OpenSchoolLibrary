package de.sp.main.resources.text;

import de.sp.main.config.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Martin on 07.06.2017.
 */
public class TSTest {
    @Test
    public void readTextResources() throws Exception {

        Configuration c = Configuration.getInstance();

        org.junit.Assert.assertNotNull(c.getLanguagesConfig());

        TS.readTextResources();

        TS ts = new TS(null);

        String text = ts.get("test.text");

        Assert.assertEquals("Only for testing purposes", text);

        //Test fallback to en-EN
        String text2 = ts.get("test.text2");

        Assert.assertEquals("Only for testing purposes - 2", text2);


    }

}