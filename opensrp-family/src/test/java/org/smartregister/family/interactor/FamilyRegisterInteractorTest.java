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
import org.smartregister.family.contract.FamilyRegisterContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.AppExecutors;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class FamilyRegisterInteractorTest {

    private final int ASYNC_TIMEOUT = 2000;

    private FamilyRegisterContract.Interactor familyRegisterInteractor;

    @Mock
    private FamilyRegisterContract.InteractorCallBack callBack;

    @Before
    public void setUp() {
        AppExecutors appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor());
        MockitoAnnotations.initMocks(this);
        familyRegisterInteractor = new FamilyRegisterInteractor(appExecutors);
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
        familyRegisterInteractor.getNextUniqueId(triple, callBack);
        Mockito.verify(callBack, timeout(ASYNC_TIMEOUT)).onNoUniqueId();
    }

    @Test
    public void testSaveRegistration() {
        ArrayList<FamilyEventClient> familyEventClients = new ArrayList<>();
        String someJson = "{\n" +
                "  \"count\": \"2\",\n" +
                "  \"encounter_type\": \"Family Registration\",\n" +
                "  \"entity_id\": \"\",\n" +
                "  \"relational_id\": \"\",\n" +
                "  \"step1\": {\n" +
                "    \"title\": \"Family details\",\n" +
                "    \"fields\": [\n" +
                "      {\n" +
                "        \"key\": \"fam_name\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"person\",\n" +
                "        \"openmrs_entity_id\": \"first_name\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"hint\": \"First name of Head of Household\",\n" +
                "        \"edit_type\": \"name\",\n" +
                "        \"v_required\": {\n" +
                "          \"value\": \"true\",\n" +
                "          \"err\": \"Please enter first name of Head of Household\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"house_number\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"person_address\",\n" +
                "        \"openmrs_entity_id\": \"address2\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"hint\": \"House Number\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"street\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"person_address\",\n" +
                "        \"openmrs_entity_id\": \"street\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"hint\": \"Street\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"landmark\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"person_address\",\n" +
                "        \"openmrs_entity_id\": \"landmark\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"hint\": \"Landmark\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        familyRegisterInteractor.saveRegistration(familyEventClients, someJson, false, callBack);
        Mockito.verify(callBack, timeout(ASYNC_TIMEOUT)).onRegistrationSaved(eq(false), eq(true), eq(familyEventClients));

    }
}