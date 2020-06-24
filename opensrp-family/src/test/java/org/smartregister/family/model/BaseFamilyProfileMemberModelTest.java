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
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.family.BaseUnitTest;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 6/23/20.
 */
public class BaseFamilyProfileMemberModelTest extends BaseUnitTest {

    private BaseFamilyProfileMemberModel model;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConfigurableViewsHelper configurableViewsHelper;

    @Mock
    private ViewConfiguration viewConfiguration;

    @Mock
    private Set<View> views;

    private SmartRegisterQueryBuilder queryBuilder;

    @Before
    public void setUp() {
        model = new BaseFamilyProfileMemberModel();
        Whitebox.setInternalState(ConfigurableViewsLibrary.getInstance(), "configurableViewsHelper", configurableViewsHelper);
        queryBuilder = new SmartRegisterQueryBuilder();
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

    @Test
    public void testCountSelect() {
        String tableName = "family";
        String filter = "name=?";
        String[] columns = model.mainColumns(tableName);
        queryBuilder.selectInitiateMainTableCounts(tableName);
        assertEquals(queryBuilder.mainCondition(filter), model.countSelect("family", "name=?"));
    }

    @Test
    public void testMainSelect() {
        String tableName = "family";
        String filter = "name=?";
        String[] columns = model.mainColumns(tableName);
        queryBuilder.selectInitiateMainTable(tableName, columns);
        assertEquals(queryBuilder.mainCondition(filter), model.mainSelect(tableName, filter));
    }
}
