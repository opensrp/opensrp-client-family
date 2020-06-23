package org.smartregister.family.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.configurableviews.helper.ConfigurableViewsHelper;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.family.BaseUnitTest;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 6/23/20.
 */
public class BaseFamilyOtherMemberProfileActivityModelTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    private BaseFamilyOtherMemberProfileActivityModel model;

    @Mock
    private ConfigurableViewsHelper configurableViewsHelper;

    @Mock
    private ViewConfiguration viewConfiguration;

    @Mock
    private Set<View> views;

    @Before
    public void setUp() {
        model = new BaseFamilyOtherMemberProfileActivityModel();
        Whitebox.setInternalState(ConfigurableViewsLibrary.getInstance(), "configurableViewsHelper", configurableViewsHelper);
    }


    @Test
    public void testDefaultRegisterConfiguration() {
        assertNotNull(model.defaultRegisterConfiguration());
    }

    @Test
    public void testGetRegisterActiveColumns() {
        when(configurableViewsHelper.getRegisterActiveColumns("view")).thenReturn(views);
        assertEquals(views, model.getRegisterActiveColumns("view"));
        verify(configurableViewsHelper).getRegisterActiveColumns("view");
    }


    @Test
    public void testGetViewConfiguration() {
        when(configurableViewsHelper.getViewConfiguration("view")).thenReturn(viewConfiguration);
        assertEquals(viewConfiguration, model.getViewConfiguration("view"));
        verify(configurableViewsHelper).getViewConfiguration("view");
    }
}
