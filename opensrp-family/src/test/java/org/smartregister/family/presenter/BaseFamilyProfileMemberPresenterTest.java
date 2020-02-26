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
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.contract.FamilyProfileMemberContract;
import org.smartregister.family.domain.FamilyMetadata;

public class BaseFamilyProfileMemberPresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyProfileMemberContract.View view;

    @Mock
    private FamilyProfileMemberContract.Model model;

    private BaseFamilyProfileMemberPresenter presenter;

    private String viewConfigurationIdentifier = "viewConfigurationIdentifier";

    private String familyBaseEntityId = "familyBaseEntityId";
    private String familyHead = "familyHead";
    private String primaryCaregiver = "primaryCaregiver";

    @Before
    public void setUp() {
        presenter = new BaseFamilyProfileMemberPresenter(view, model, viewConfigurationIdentifier, familyBaseEntityId, familyHead, primaryCaregiver);
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
        String values = presenter.getMainCondition();
        Assert.assertEquals(values.trim(), "object_relational_id = 'familyBaseEntityId' and date_removed is null");
    }

    @Test
    public void testGetDefaultSortQuery() {
        String values = presenter.getDefaultSortQuery();
        Assert.assertEquals(values.trim(), "dod, dob ASC");
    }

    @Test
    public void testSetModel() {
        FamilyProfileMemberContract.Model model = Mockito.mock(FamilyProfileMemberContract.Model.class);
        presenter.setModel(model);
        Assert.assertEquals(Whitebox.getInternalState(presenter, "model"), model);
    }

    @Test
    public void testGetBaseEntityID() {
        Assert.assertEquals(familyBaseEntityId, presenter.getFamilyBaseEntityId());
    }

    @Test
    public void testGetFamilyHead() {
        Assert.assertEquals(familyHead, presenter.getFamilyHead());
    }

    @Test
    public void testGetPrimaryCaregiver() {
        Assert.assertEquals(primaryCaregiver, presenter.getPrimaryCaregiver());
    }

    @Test
    public void testSetFamilyHead() {
        String newHead = "newHead";
        Assert.assertNotEquals(newHead, presenter.getFamilyHead());

        presenter.setFamilyHead(newHead);
        Assert.assertEquals(newHead, presenter.getFamilyHead());
    }

    @Test
    public void testSetPrimaryCaregiver() {
        String newCareGiver = "newCareGiver";
        Assert.assertNotEquals(newCareGiver, presenter.getFamilyHead());

        presenter.setPrimaryCaregiver(newCareGiver);
        Assert.assertEquals(newCareGiver, presenter.getPrimaryCaregiver());
    }
}
