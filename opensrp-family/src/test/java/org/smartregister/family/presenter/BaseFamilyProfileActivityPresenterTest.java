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
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.contract.FamilyProfileActivityContract;
import org.smartregister.family.domain.FamilyMetadata;

import java.util.HashSet;
import java.util.Set;

public class BaseFamilyProfileActivityPresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyProfileActivityContract.View view;

    @Mock
    private FamilyProfileActivityContract.Model model;

    private String viewConfigurationIdentifier = "viewConfigurationIdentifier";

    private String familyBaseEntityId = "familyBaseEntityId";

    private BaseFamilyProfileActivityPresenter presenter;

    @Before
    public void setUp() {
        presenter = new BaseFamilyProfileActivityPresenter(view, model, viewConfigurationIdentifier, familyBaseEntityId);
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

        presenter = Mockito.spy(presenter);
        Mockito.doReturn("12345").when(presenter).getQueryTable();

        String mainCondition = "mainCondition";
        presenter.initializeQueries(mainCondition);

        Mockito.verify(view).countExecute();
        Mockito.verify(view).filterandSortInInitializeQueries();
    }

    @Test
    public void testGetMainCondition() {
        Assert.assertEquals(presenter.getMainCondition().trim(), "object_relational_id = 'familyBaseEntityId' and date_removed is null");
    }

    @Test
    public void testGetDefaultSortQuery() {
        Assert.assertEquals(presenter.getDefaultSortQuery().trim(), "dod, dob ASC");
    }

    @Test
    public void testSetVisibleColumns() throws Exception {
        Set<View> visibleColumns = new HashSet<>();
        Whitebox.invokeMethod(presenter, "setVisibleColumns", visibleColumns);
        Assert.assertEquals(visibleColumns, Whitebox.getInternalState(presenter, "visibleColumns"));
    }

    @Test
    public void testSetModel() {
        FamilyProfileActivityContract.Model newModel = Mockito.mock(FamilyProfileActivityContract.Model.class);
        presenter.setModel(newModel);
        Assert.assertEquals(newModel, Whitebox.getInternalState(presenter, "model"));
    }

    @Test
    public void testGetFamilyBaseEntityId() {
        Assert.assertEquals(presenter.getFamilyBaseEntityId(), familyBaseEntityId);
    }
}
