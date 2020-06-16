package org.smartregister.family.interactor;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
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
import org.smartregister.family.util.Utils;
import org.smartregister.repository.UniqueIdRepository;

import java.util.HashMap;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


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
    private Context context;

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
    public void testSaveRegistration() {
        FamilyEventClient familyEventClient = Mockito.mock(FamilyEventClient.class);
        String someJson = "{\n" +
                "  \"count\": \"2\",\n" +
                "  \"encounter_type\": \"Family Registration\",\n" +
                "  \"entity_id\": \"\",\n" +
                "  \"relational_id\": \"\",\n" +
                "  \"step1\": {\n" +
                "    \"title\": \"Family member registration\",\n" +
                "    \"fields\": [\n" +
                "{\n" +
                "        \"key\": \"unique_id\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"person_identifier\",\n" +
                "        \"openmrs_entity_id\": \"opensrp_id\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"hint\": \"ID\",\n" +
                "        \"read_only\": \"True\",\n" +
                "        \"v_required\": {\n" +
                "          \"value\": \"true\",\n" +
                "          \"err\": \"Please enter the ID\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"first_name\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"\",\n" +
                "        \"openmrs_entity_id\": \"\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"hint\": \"First name\",\n" +
                "        \"edit_type\": \"name\",\n" +
                "        \"v_required\": {\n" +
                "          \"value\": \"true\",\n" +
                "          \"err\": \"Please enter the first name\"\n" +
                "        },\n" +
                "        \"relevance\": {\n" +
                "          \"rules-engine\": {\n" +
                "            \"ex-rules\": {\n" +
                "              \"rules-file\": \"family-member-relevance.yml\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }" +
                "    ]\n" +
                "  }\n" +
                "}";

        familyProfileInteractor.saveRegistration(familyEventClient, someJson, false, familyProfileCallback);
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).onRegistrationSaved(eq(false), eq(false), eq(familyEventClient));
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
        when(context.commonrepository(anyString())).thenReturn(commonRepository);
        commonPersonObject.getColumnmaps().put(DBConstants.KEY.FAMILY_HEAD, "12121213445");
        when(commonRepository.findByBaseEntityId(commonPersonObject.getCaseId())).thenReturn(commonPersonObject);
        when(commonRepository.findByBaseEntityId("12121213445")).thenReturn(familyHead);
        ReflectionHelpers.setField(FamilyLibrary.getInstance(), "context", context);

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
        when(context.commonrepository(anyString())).thenReturn(commonRepository);
        commonPersonObject.getColumnmaps().put(DBConstants.KEY.FAMILY_HEAD, "12121213445");
        when(commonRepository.findByBaseEntityId(commonPersonObject.getCaseId())).thenReturn(commonPersonObject);
        when(commonRepository.findByBaseEntityId("12121213445")).thenReturn(familyHead);
        ReflectionHelpers.setField(FamilyLibrary.getInstance(), "context", context);

        familyProfileInteractor.refreshProfileView(commonPersonObject.getCaseId(), true, familyProfileCallback);
        verify(commonRepository, timeout(ASYNC_TIMEOUT)).findByBaseEntityId(commonPersonObject.getCaseId());
        verify(commonRepository, timeout(ASYNC_TIMEOUT)).findByBaseEntityId(familyHead.getCaseId());
        verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).startFormForEdit(commonPersonObjectClientArgumentCaptor.capture());
        assertEquals(commonPersonObject.getCaseId(), commonPersonObjectClientArgumentCaptor.getValue().getCaseId());
        assertEquals(commonPersonObject.getColumnmaps(), commonPersonObjectClientArgumentCaptor.getValue().getColumnmaps());
        assertEquals("Jack", commonPersonObjectClientArgumentCaptor.getValue().getColumnmaps().get(Constants.KEY.FAMILY_HEAD_NAME));
    }
}