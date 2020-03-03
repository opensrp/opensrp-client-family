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
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.configurableviews.model.View;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.interactor.FamilyOtherMemberProfileInteractor;
import org.smartregister.family.util.DBConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseFamilyOtherMemberProfileActivityPresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyOtherMemberContract.View view;

    @Mock
    private FamilyOtherMemberContract.Model model;

    @Mock
    private FamilyOtherMemberProfileInteractor interactor;

    private String viewConfigurationIdentifier = "viewConfigurationIdentifier";

    private String baseEntityId = "baseEntityId";

    private String familyHead = "familyHead";

    private String primaryCaregiver = "primaryCaregiver";

    private String villageTown = "villageTown";


    private BaseFamilyOtherMemberProfileActivityPresenter presenter;

    @Before
    public void setUp() {
        presenter = new BaseFamilyOtherMemberProfileActivityPresenter(view, model, viewConfigurationIdentifier, baseEntityId, familyHead, primaryCaregiver, villageTown);
        ReflectionHelpers.setField(presenter, "interactor", interactor);
    }

    @Test
    public void testOnDestroyNotifiesInteractor() {
        presenter.onDestroy(true);
        Mockito.verify(interactor).onDestroy(true);
    }

    @Test
    public void testFetchProfileData() {
        presenter.fetchProfileData();
        Mockito.verify(interactor).refreshProfileView(baseEntityId, presenter);
    }

    @Test
    public void testRefreshProfileView() {
        presenter.refreshProfileView();
        Mockito.verify(interactor).refreshProfileView(baseEntityId, presenter);
    }

    @Test
    public void testSetVisibleColumns() throws Exception {
        Set<View> visibleColumns = new HashSet<>();
        Whitebox.invokeMethod(presenter, "setVisibleColumns", visibleColumns);
        Assert.assertEquals(visibleColumns, Whitebox.getInternalState(presenter, "visibleColumns"));
    }

    @Test
    public void testSetModel() {
        FamilyOtherMemberContract.Model model = Mockito.mock(FamilyOtherMemberContract.Model.class);
        presenter.setModel(model);
        Assert.assertEquals(Whitebox.getInternalState(presenter, "model"), model);
    }

    @Test
    public void testGetBaseEntityId() {
        Assert.assertEquals(presenter.getBaseEntityId(), baseEntityId);
    }

    @Test
    public void testRefreshProfileTopSection() {
        CommonPersonObjectClient client = Mockito.mock(CommonPersonObjectClient.class);
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put(DBConstants.KEY.ENTITY_TYPE, "entityType");

        Mockito.doReturn(columnMap).when(client).getColumnmaps();
        Mockito.doReturn(" %s ").when(view).getString(Mockito.anyInt());

        presenter.refreshProfileTopSection(client);
        Mockito.verify(view).setProfileImage(client.getCaseId(), "entityType");
    }
}
