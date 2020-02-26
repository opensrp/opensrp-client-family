package org.smartregister.family.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.configurableviews.model.Field;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.contract.FamilyRegisterFragmentContract;
import org.smartregister.family.domain.FamilyMetadata;

import java.util.List;


/**
 * @author rkodev
 */
public class BaseFamilyRegisterFragmentPresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private BaseFamilyRegisterFragmentPresenter presenter;

    @Mock
    private FamilyRegisterFragmentContract.View view;

    @Mock
    private FamilyRegisterFragmentContract.Model model;

    private String viewConfigurationIdentifier = "12345";

    @Before
    public void setUp() {
        presenter = Mockito.mock(BaseFamilyRegisterFragmentPresenter.class, Mockito.withSettings()
                .useConstructor(view, model, viewConfigurationIdentifier)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));
    }

    @Test
    public void testProcessViewConfigurations() {
        ViewConfiguration viewConfiguration = Mockito.mock(ViewConfiguration.class);
        Mockito.doReturn(viewConfiguration).when(model).getViewConfiguration(viewConfigurationIdentifier);

        RegisterConfiguration registerConfiguration = Mockito.mock(RegisterConfiguration.class);
        Mockito.doReturn("Search Text").when(registerConfiguration).getSearchBarText();
        Mockito.doReturn(registerConfiguration).when(viewConfiguration).getMetadata();

        presenter.processViewConfigurations();
        Mockito.verify(view).updateSearchBarHint("Search Text");
    }

    @Test
    public void testInitializeQueriesUpdatesView() {

        org.smartregister.Context context = Mockito.mock(org.smartregister.Context.class);
        FamilyLibrary.init(context, getMetadata(), 1, 1);

        FamilyMetadata.FamilyRegister familyRegister = new FamilyMetadata.FamilyRegister("formName", "tableName", null, null, null, null, null);
        Whitebox.setInternalState(getMetadata(), "familyRegister", familyRegister);

        Mockito.doReturn("12345").when(presenter).getQueryTable();

        String mainCondition = "mainCondition";
        presenter.initializeQueries(mainCondition);

        Mockito.verify(view).countExecute();
        Mockito.verify(view).filterandSortInInitializeQueries();
    }

    @Test
    public void testUpdateSortAndFilter() {
        List<Field> filterList = Mockito.mock(List.class);
        Field sortField = Mockito.mock(Field.class);

        Mockito.doReturn("123").when(view).getString(org.smartregister.R.string.filter);
        Mockito.doReturn("1234").when(model).getFilterText(filterList, "123");
        Mockito.doReturn("12123").when(model).getSortText(sortField);

        presenter.updateSortAndFilter(filterList, sortField);
        Mockito.verify(view).updateFilterAndFilterStatus(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetMainCondition() {
        String text = presenter.getMainCondition();
        Assert.assertEquals("date_removed is null", text.trim());
    }

    @Test
    public void testSetModel() {
        FamilyRegisterFragmentContract.Model model = Mockito.mock(FamilyRegisterFragmentContract.Model.class);
        presenter.setModel(model);
        Assert.assertEquals(Whitebox.getInternalState(presenter, "model"), model);
    }
}
