package org.smartregister.family.interactor;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.UniqueId;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.smartregister.family.util.JsonFormUtils.CURRENT_OPENSRP_ID;


public class FamilyProfileInteractorTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private FamilyProfileContract.Interactor familyProfileInteractor;

    @Mock
    private FamilyProfileContract.InteractorCallBack familyProfileCallback;

    @Mock
    private UniqueIdRepository uniqueIdRepository;

    @Mock
    private CommonRepository commonRepository;

    @Mock
    private ECSyncHelper ecSyncHelper;

    private String id = UUID.randomUUID().toString();

    private Client client = new Client(id);

    private Event event = new Event();

    @Captor
    private ArgumentCaptor<CommonPersonObjectClient> commonPersonObjectClientArgumentCaptor;

    private AppExecutors appExecutors;

    private Triple<String, String, String> triple;

    private CommonPersonObject commonPersonObject;


    @Before
    public void setUp() {
        appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor());
        familyProfileInteractor = new FamilyProfileInteractor(appExecutors);
        triple = new Triple<String, String, String>() {
            @Override
            public String getLeft() {
                return "I turned myself to Pickle";
            }

            @Override
            public String getMiddle() {
                return "am a pickle Morty,";
            }

            @Override
            public String getRight() {
                return "pickle Rick";
            }
        };
        Whitebox.setInternalState(FamilyLibrary.getInstance(), "uniqueIdRepository", uniqueIdRepository);
        commonPersonObject = new CommonPersonObject("some-crazy-base-entity-id", "", null, "");
        commonPersonObject.setColumnmaps(TestDataUtils.getCommonPersonObjectClient().getColumnmaps());
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        event.setBaseEntityId(id);

        HashMap<Object, Object> mapOfCommonRepository = spy(new HashMap<>());
        when(mapOfCommonRepository.get(anyString())).thenReturn(commonRepository);
        Whitebox.setInternalState(FamilyLibrary.getInstance().context(), "MapOfCommonRepository", mapOfCommonRepository);
    }


    @Test
    public void testGetNextUniqueIdWithNoUniqueId() {
        familyProfileInteractor.getNextUniqueId(triple, familyProfileCallback);
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).onNoUniqueId();
    }

    @Test
    public void testGetNextUniqueIdWithUniqueId() {
        UniqueId uniqueId = new UniqueId();
        uniqueId.setOpenmrsId("1233-1");
        when(uniqueIdRepository.getNextUniqueId()).thenReturn(uniqueId);
        familyProfileInteractor.getNextUniqueId(triple, familyProfileCallback);
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).onUniqueIdFetched(triple, uniqueId.getOpenmrsId());
    }

    @Test
    public void testSaveRegistrationNonEditMode() {
        FamilyEventClient familyEventClient = new FamilyEventClient(client, event);
        familyProfileInteractor.saveRegistration(familyEventClient, TestDataUtils.FILLED_FAMILY_FORM, false, familyProfileCallback);
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).onRegistrationSaved(false, true, familyEventClient);
    }

    @Test
    public void testSaveRegistrationEditMode() throws JSONException {
        Whitebox.setInternalState(FamilyLibrary.getInstance(), "syncHelper", ecSyncHelper);
        when(ecSyncHelper.getClient(id)).thenReturn(new JSONObject("{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"birthdate\":\"1997-11-21T07:00:00.000+07:00\",\"birthdateApprox\":false,\"deathdateApprox\":false,\"gender\":\"Female\",\"relationships\":{\"family\":[\"9d4c4722-eef4-4baa-94aa-1805b2a0a60b\"]},\"baseEntityId\":\"600d9823-78ae-48e2-8c4c-c9\",\"identifiers\":{\"opensrp_id\":\"11152030\"},\"addresses\":[],\"attributes\":{\"residence\":\"896d12ca-2ac8-4e7c-a725-cd42ea49ac06\"},\"dateCreated\":\"2019-11-21T17:13:33.197+07:00\",\"serverVersion\":1574331213134,\"clientApplicationVersion\":7,\"clientDatabaseVersion\":3,\"type\":\"Client\",\"id\":\"b43ea939-3e54-45b3-9197-cb5839c8518a\",\"revision\":\"v1\"}"));
        FamilyEventClient familyEventClient = new FamilyEventClient(client, event);
        client.addIdentifier("UNIQUE_IDENTIFIER_KEY", "123");
        JSONObject jsonObject = new JSONObject(TestDataUtils.FILLED_FAMILY_FORM);
        jsonObject.put(CURRENT_OPENSRP_ID, "1234");
        familyProfileInteractor.saveRegistration(familyEventClient, jsonObject.toString(), true, familyProfileCallback);
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).onRegistrationSaved(true, true, familyEventClient);
        verify(ecSyncHelper).addClient(eq(id), any(JSONObject.class));
        verify(ecSyncHelper).addEvent(eq(id), any(JSONObject.class));
    }

    @Test
    public void testOnDestroy() {
        appExecutors = spy(appExecutors);
        familyProfileInteractor.onDestroy(false);
        verifyNoMoreInteractions(appExecutors);
    }


    @Test
    public void testRefreshProfileViewRefreshesTopSection() {
        CommonPersonObject familyHead = new CommonPersonObject("12121213445", "", null, "");
        familyHead.setColumnmaps(new HashMap<String, String>());
        familyHead.getColumnmaps().put(DBConstants.KEY.FIRST_NAME, "Jack");
        commonPersonObject.getColumnmaps().put(DBConstants.KEY.FAMILY_HEAD, "12121213445");
        when(commonRepository.findByBaseEntityId(commonPersonObject.getCaseId())).thenReturn(commonPersonObject);
        when(commonRepository.findByBaseEntityId("12121213445")).thenReturn(familyHead);

        familyProfileInteractor.refreshProfileView(commonPersonObject.getCaseId(), false, familyProfileCallback);
        verify(commonRepository, timeout(ASYNC_TIMEOUT)).findByBaseEntityId(commonPersonObject.getCaseId());
        verify(commonRepository, timeout(ASYNC_TIMEOUT)).findByBaseEntityId(familyHead.getCaseId());
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).refreshProfileTopSection(commonPersonObjectClientArgumentCaptor.capture());
        assertEquals(commonPersonObject.getCaseId(), commonPersonObjectClientArgumentCaptor.getValue().getCaseId());
        assertEquals(commonPersonObject.getColumnmaps(), commonPersonObjectClientArgumentCaptor.getValue().getColumnmaps());
        assertEquals("Jack", commonPersonObjectClientArgumentCaptor.getValue().getColumnmaps().get(Constants.KEY.FAMILY_HEAD_NAME));
    }

    @Test
    public void testRefreshProfileViewOpensForm() {
        CommonPersonObject familyHead = new CommonPersonObject("12121213445", "", null, "");
        familyHead.setColumnmaps(new HashMap<String, String>());
        familyHead.getColumnmaps().put(DBConstants.KEY.FIRST_NAME, "Jack");
        commonPersonObject.getColumnmaps().put(DBConstants.KEY.FAMILY_HEAD, "12121213445");
        when(commonRepository.findByBaseEntityId(commonPersonObject.getCaseId())).thenReturn(commonPersonObject);
        when(commonRepository.findByBaseEntityId("12121213445")).thenReturn(familyHead);

        familyProfileInteractor.refreshProfileView(commonPersonObject.getCaseId(), true, familyProfileCallback);
        verify(commonRepository, timeout(ASYNC_TIMEOUT)).findByBaseEntityId(commonPersonObject.getCaseId());
        verify(commonRepository, timeout(ASYNC_TIMEOUT)).findByBaseEntityId(familyHead.getCaseId());
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).startFormForEdit(commonPersonObjectClientArgumentCaptor.capture());
        assertEquals(commonPersonObject.getCaseId(), commonPersonObjectClientArgumentCaptor.getValue().getCaseId());
        assertEquals(commonPersonObject.getColumnmaps(), commonPersonObjectClientArgumentCaptor.getValue().getColumnmaps());
        assertEquals("Jack", commonPersonObjectClientArgumentCaptor.getValue().getColumnmaps().get(Constants.KEY.FAMILY_HEAD_NAME));
    }
}