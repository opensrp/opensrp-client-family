package org.smartregister.family.presenter;

import android.content.Context;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.FetchStatus;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.R;
import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;

public class BaseFamilyProfilePresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private BaseFamilyProfilePresenter presenter;

    @Mock
    private FamilyProfileContract.View view;

    @Mock
    private FamilyProfileContract.Model model;

    @Mock
    private FamilyProfileContract.Interactor interactor;

    @Mock
    private BaseFamilyProfileActivity familyProfileActivity;

    private Context context = RuntimeEnvironment.application;

    private String familyBaseEntityId = "familyBaseEntityId";
    private String familyHead = "familyHead";
    private String primaryCaregiver = "primaryCaregiver";
    private String familyName = "familyName";

    @Before
    public void setUp() {
        presenter = new BaseFamilyProfilePresenter(view, model, familyBaseEntityId, familyHead, primaryCaregiver, familyName);
        Whitebox.setInternalState(presenter, "interactor", interactor);
    }

    @Test
    public void testOnDestroyNotifiesInteractor() {
        presenter.onDestroy(true);
        Mockito.verify(interactor).onDestroy(true);
    }

    @Test
    public void testFetchProfileDataNotifiesInteractor() {
        presenter.fetchProfileData();
        Mockito.verify(interactor).refreshProfileView(familyBaseEntityId, true, presenter);
    }

    @Test
    public void testRefreshProfileView() {
        presenter.refreshProfileView();
        Mockito.verify(interactor).refreshProfileView(familyBaseEntityId, false, presenter);
    }

    @Test
    public void testRefreshProfileTopSection() {
        BaseFamilyProfilePresenter mockPresenter = Mockito.spy(presenter);
        CommonPersonObjectClient client = Mockito.mock(CommonPersonObjectClient.class);
        Mockito.doReturn("12345").when(client).getCaseId();
        Mockito.doReturn(familyProfileActivity).when(mockPresenter).getView();

        mockPresenter.refreshProfileTopSection(client);

        Mockito.verify(familyProfileActivity).setProfileImage(client.getCaseId());
    }

    @Test
    public void testStartFormForEdit() {
        CommonPersonObjectClient client = Mockito.mock(CommonPersonObjectClient.class);
        Mockito.doReturn(context).when(view).getApplicationContext();

        presenter.startFormForEdit(client);
        Mockito.verify(view).startFormActivity(null);
    }


    @Test
    public void testStartForm() throws Exception {

        // no id exists
        presenter.startForm("123345", null, "asdasd", "asdasd");
        Mockito.verify(interactor).getNextUniqueId(Mockito.any(Triple.class), Mockito.any(BaseFamilyProfilePresenter.class));


        // with id

        presenter.startForm("123345", "123455", "asdasd", "asdasd");
        Mockito.verify(view).startFormActivity(null);
    }

    @Test
    public void testOnNoUniqueId() {
        presenter.onNoUniqueId();
        Mockito.verify(view).displayShortToast(R.string.no_unique_id);
    }

    @Test
    public void testonUniqueIdFetched() throws Exception {
        Triple<String, String, String> triple = Triple.of("1", "2", "3");
        String entityId = "12345";

        presenter = Mockito.spy(presenter);
        presenter.onUniqueIdFetched(triple, entityId);
        Mockito.verify(presenter).startForm(triple.getLeft(), entityId, triple.getMiddle(), triple.getRight());
    }

    @Test
    public void testSaveFamilyMember() {

        String jsonString = "{}";
        FamilyEventClient familyEventClient = Mockito.mock(FamilyEventClient.class);
        Mockito.doReturn(familyEventClient).when(model).processMemberRegistration(jsonString, familyBaseEntityId);


        presenter.saveFamilyMember(jsonString);
        Mockito.verify(interactor).saveRegistration(familyEventClient, jsonString, false, presenter);
    }

    @Test
    public void testUpdateFamilyRegister() {
        String jsonString = "{}";
        FamilyEventClient familyEventClient = Mockito.mock(FamilyEventClient.class);
        Mockito.doReturn(familyEventClient).when(model).processFamilyRegistrationForm(jsonString, familyBaseEntityId);


        presenter.updateFamilyRegister(jsonString);
        Mockito.verify(interactor).saveRegistration(familyEventClient, jsonString, true, presenter);
    }

    @Test
    public void testOnRegistrationSaved() {
        presenter.onRegistrationSaved(false, true, null);
        Mockito.verify(view).refreshMemberList(FetchStatus.fetched);
    }

    @Test
    public void testFamilyBaseEntityId() {
        Assert.assertEquals(presenter.familyBaseEntityId(), familyBaseEntityId);
    }

    @Test
    public void testSetInteractor() {
        FamilyProfileContract.Interactor interactor = Mockito.mock(FamilyProfileContract.Interactor.class);
        presenter.setInteractor(interactor);
        Assert.assertEquals(interactor, Whitebox.getInternalState(presenter, "interactor"));
    }
}
