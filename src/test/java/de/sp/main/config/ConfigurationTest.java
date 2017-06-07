package de.sp.main.config;

import org.junit.Test;

/**
 * Created by Martin on 07.06.2017.
 */
public class ConfigurationTest {
    @Test
    public void getInstance() throws Exception {

        Configuration c = Configuration.getInstance();

        org.junit.Assert.assertNotNull(c.getDatabase().getType());

    }

}