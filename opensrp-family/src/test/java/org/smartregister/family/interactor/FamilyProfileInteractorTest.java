package org.smartregister.family.interactor;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.family.TestApplication;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.AppExecutors;

import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;


@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class FamilyProfileInteractorTest {

    private final int ASYNC_TIMEOUT = 2000;

    private FamilyProfileContract.Interactor familyProfileInteractor;

    @Mock
    private FamilyProfileContract.InteractorCallBack familyProfileCallback;

    private String someBaseEntityId = "somebase-enityt-id";

    @Before
    public void setUp() {
        AppExecutors appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor());
        MockitoAnnotations.initMocks(this);
        familyProfileInteractor = new FamilyProfileInteractor(appExecutors);
    }


    @Test
    public void testGetNextUniqueId() {
        Triple<String, String, String> triple = new Triple<String, String, String>() {
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
        familyProfileInteractor.getNextUniqueId(triple, familyProfileCallback);
        Mockito.verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).onNoUniqueId();
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
        Mockito.verify(familyProfileCallback, timeout(ASYNC_TIMEOUT)).onRegistrationSaved(eq(false), eq(false), eq(familyEventClient));
    }
}