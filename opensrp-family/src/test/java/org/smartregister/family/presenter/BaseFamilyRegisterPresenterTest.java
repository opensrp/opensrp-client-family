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
import org.smartregister.domain.FetchStatus;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.contract.FamilyRegisterContract;
import org.smartregister.family.domain.FamilyEventClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rkodev
 */
public class BaseFamilyRegisterPresenterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private BaseFamilyRegisterPresenter presenter;

    @Mock
    private FamilyRegisterContract.View view;

    @Mock
    private FamilyRegisterContract.Model model;

    @Mock
    private FamilyRegisterContract.Interactor interactor;

    private Context context = RuntimeEnvironment.application;

    @Before
    public void setUp() {
        presenter = new BaseFamilyRegisterPresenter(view, model);
        Whitebox.setInternalState(presenter, "interactor", interactor);
    }

    @Test
    public void testSetModel() {
        FamilyRegisterContract.Model currentModel = Whitebox.getInternalState(presenter, "model");
        Assert.assertEquals(currentModel, model);

        FamilyRegisterContract.Model newModel = Mockito.mock(FamilyRegisterContract.Model.class);
        presenter.setModel(newModel);

        Assert.assertNotEquals(newModel, model);
    }

    @Test
    public void testSetInteractor() {
        FamilyRegisterContract.Interactor newInteractor = Mockito.mock(FamilyRegisterContract.Interactor.class);
        presenter.setInteractor(newInteractor);

        Assert.assertEquals(newInteractor, Whitebox.getInternalState(presenter, "interactor"));
    }

    @Test
    public void testRegisterViewConfigurations() {
        List<String> viewIdentifiers = new ArrayList<>();

        presenter.registerViewConfigurations(viewIdentifiers);
        Mockito.verify(model).registerViewConfigurations(viewIdentifiers);
    }

    @Test
    public void testUnregisterViewConfiguration() {
        List<String> viewIdentifiers = new ArrayList<>();
        presenter.unregisterViewConfiguration(viewIdentifiers);
        Mockito.verify(model).unregisterViewConfiguration(viewIdentifiers);
    }

    @Test
    public void testSaveLanguage() {
        String language = "language";
        presenter.saveLanguage(language);
        Mockito.verify(model).saveLanguage(language);
        Mockito.verify(view).displayToast(language + " selected");
    }

    @Test
    public void testStartForm() throws Exception {
        String formName = "form_name";
        String entitId = "entity_id";
        String metadata = "metadata";
        String currentLocationId = "current_location_id";

        // if id is provided
        presenter.startForm(formName, entitId, metadata, currentLocationId);
        Mockito.verify(view).startFormActivity(null);

        // if the is is not provided, verify extraction by interactor
        presenter.startForm(formName, null, metadata, currentLocationId);
        Mockito.verify(interactor).getNextUniqueId(Mockito.any(Triple.class), Mockito.any(FamilyRegisterContract.InteractorCallBack.class));
    }

    @Test
    public void testCloseFamilyRecord() {
        Mockito.doReturn(context).when(view).getContext();
        presenter.closeFamilyRecord("{}");

        Mockito.verify(interactor).removeFamilyFromRegister(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testSaveForm() {
        List<FamilyEventClient> familyEventClientList = new ArrayList<>();
        familyEventClientList.add(Mockito.mock(FamilyEventClient.class));

        Mockito.doReturn(familyEventClientList).when(model).processRegistration(Mockito.anyString());
        String jsonString = "{}";

        presenter.saveForm("{}", false);

        Mockito.verify(interactor).saveRegistration(familyEventClientList, jsonString, false, presenter);
    }

    @Test
    public void testOnNoUniqueId() {
        presenter.onNoUniqueId();
        Mockito.verify(view).displayShortToast(Mockito.anyInt());
    }

    @Test
    public void testOnUniqueIdFetched() throws Exception {
        Triple<String, String, String> triple = Triple.of("1", "2", "3");
        String entityId = "entityId";

        presenter = Mockito.spy(presenter);

        presenter.onUniqueIdFetched(triple, entityId);
        Mockito.verify(presenter).startForm(triple.getLeft(), entityId, triple.getMiddle(), triple.getRight());
    }

    @Test
    public void testOnRegistrationSaved() {
        presenter.onRegistrationSaved(false, true, Mockito.mock(List.class));
        Mockito.verify(view).refreshList(FetchStatus.fetched);
        Mockito.verify(view).hideProgressDialog();
    }

    @Test
    public void testOnDestroy() {
        presenter.onDestroy(false);
        Mockito.verify(interactor).onDestroy(false);
        Assert.assertNull(Whitebox.getInternalState(presenter, "interactor"));
        Assert.assertNull(Whitebox.getInternalState(presenter, "model"));
    }

    @Test
    public void testUpdateInitials() {
        String initials = "RK";
        Mockito.doReturn(initials).when(model).getInitials();

        presenter.updateInitials();
        Mockito.verify(view).updateInitialsText(initials);
    }
}
