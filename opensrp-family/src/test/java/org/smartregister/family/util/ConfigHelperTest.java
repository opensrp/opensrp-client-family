package org.smartregister.family.util;

import android.app.Application;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.family.BaseUnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ConfigHelperTest extends BaseUnitTest {

    @Test
    public void testDefaultRegisterConfigurationWithNullInput() {
       assertNull(ConfigHelper.defaultRegisterConfiguration(null));
    }

    @Test
    public void testDefaultRegisterConfigurationWithCorrectInput() {
        Application context =  RuntimeEnvironment.application;
        RegisterConfiguration registerConfiguration = ConfigHelper.defaultRegisterConfiguration(context);
        assertFalse(registerConfiguration.isEnableSortList());
        assertFalse(registerConfiguration.isEnableFilterList());
        assertEquals(registerConfiguration.getSearchBarText(), context.getString(org.smartregister.R.string.search_hint));
    }
}