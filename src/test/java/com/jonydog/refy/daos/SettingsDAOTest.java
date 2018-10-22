package com.jonydog.refy.daos;

import com.jonydog.refy.configs.TestConfigs;
import com.jonydog.refy.model.Settings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes= TestConfigs.class)
public class SettingsDAOTest {

    @Autowired
    private SettingsDAO settingsDAO;

    @Test
    public void testSettingsPersistence(){

        Settings s = new Settings();
        s.setHomeFolder("C:/yoo/yoo");
        this.settingsDAO.save(s);

        Settings readObject = this.settingsDAO.get();
        Assert.assertEquals( "C:/yoo/yoo",readObject.getHomeFolder() );

    }


}
