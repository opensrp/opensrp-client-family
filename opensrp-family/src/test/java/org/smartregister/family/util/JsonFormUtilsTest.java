package org.smartregister.family.util;

import org.joda.time.DateTime;
import org.json.JSONArray;
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
import org.smartregister.Context;
import org.smartregister.clientandeventmodel.Address;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.domain.ProfileImage;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.ImageRepository;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.view.LocationPickerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.vijay.jsonwizard.constants.JsonFormConstants.STEP1;
import static com.vijay.jsonwizard.utils.FormUtils.fields;
import static com.vijay.jsonwizard.utils.FormUtils.getFieldJSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.family.util.JsonFormUtils.STEP2;
import static org.smartregister.family.util.JsonFormUtils.getFieldValue;
import static org.smartregister.util.JsonFormUtils.ENCOUNTER_LOCATION;
import static org.smartregister.util.JsonFormUtils.VALUE;


/**
 * Created by samuelgithengi on 4/14/20.
 */
public class JsonFormUtilsTest extends BaseUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Context context;

    private JSONObject originalForm = new JSONObject(TestDataUtils.REGISTER_FAMILY_FORM);

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private ECSyncHelper ecSyncHelper;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private LocationPickerView locationPickerView;

    @Mock
    private ProfileImage profileImage;

    @Captor
    private ArgumentCaptor<JSONObject> jsonObjectArgumentCaptor;

    @Captor
    private ArgumentCaptor<ProfileImage> profileImageCaptor;


    public JsonFormUtilsTest() throws JSONException {
    }

    @Before
    public void setUp() {
        FamilyLibrary.init(context, null, 1, 1);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
    }

    @Test
    public void getFormAsJsonShouldReturnNullWithNullForm() throws Exception {
        assertNull(JsonFormUtils.getFormAsJson(null, null, null, null));
    }

    @Test
    public void getFormAsJsonShouldPopulateEncounterLocation() throws Exception {

        JSONObject originalForm = new JSONObject();
        originalForm.put(METADATA, new JSONObject());
        JSONObject form = JsonFormUtils.getFormAsJson(originalForm, null, null, "location1");
        assertNotNull(form);
        assertEquals("location1", form.getJSONObject(METADATA).getString(ENCOUNTER_LOCATION));
    }

    @Test
    public void getFormAsJsonWithRegisterFamilyShouldPopulateUniqueId() throws Exception {

        originalForm.put(METADATA, new JSONObject());
        JSONObject form = JsonFormUtils.getFormAsJson(originalForm, "FAMILY_REGISTER", "1234", "location1");
        assertNotNull(form);
        assertEquals("location1", form.getJSONObject(METADATA).getString(ENCOUNTER_LOCATION));

        JSONArray step1 = fields(form, STEP1);
        assertEquals("1234_Family", getFieldJSONObject(step1, Constants.JSON_FORM_KEY.UNIQUE_ID).getString(VALUE));

        JSONArray step2 = fields(form, STEP2);
        assertEquals("1234", getFieldJSONObject(step2, Constants.JSON_FORM_KEY.UNIQUE_ID).getString(VALUE));
    }

    @Test
    public void getFormAsJsonWithRegisterFamilyMemberShouldPopulateUniqueId() throws Exception {
        originalForm.put(METADATA, new JSONObject());
        JSONObject form = JsonFormUtils.getFormAsJson(originalForm, "FAMILY_MEMBER_REGISTER", "1234", "location1");
        assertNotNull(form);
        assertEquals("location1", form.getJSONObject(METADATA).getString(ENCOUNTER_LOCATION));

        JSONArray step1 = fields(form, STEP1);
        assertEquals("1234", getFieldJSONObject(step1, Constants.JSON_FORM_KEY.UNIQUE_ID).getString(VALUE));


    }

    @Test
    public void updateJsonFormShouldUpdateFamilyName() throws Exception {
        JsonFormUtils.updateJsonForm(originalForm, "family122");
        JSONArray step1 = fields(originalForm, STEP1);
        assertEquals("family122", getFieldJSONObject(step1, Constants.JSON_FORM_KEY.FAMILY_NAME).getString(VALUE));
    }

    @Test
    public void updateJsonFormWithNullForm() throws Exception {
        JSONObject form = null;
        JsonFormUtils.updateJsonForm(form, "family122");
        assertNull(form);
    }


    @Test
    public void processFamilyUpdateFormShouldReturnEventAndClient() {
        FamilyEventClient familyEventClient = JsonFormUtils.processFamilyUpdateForm(allSharedPreferences, TestDataUtils.FILLED_FAMILY_FORM);
        assertNotNull(familyEventClient);
        assertEquals("763268733n", familyEventClient.getClient().getBaseEntityId());
        assertEquals("John", familyEventClient.getClient().getFirstName());
        assertEquals("Family", familyEventClient.getClient().getLastName());
        assertFalse(familyEventClient.getClient().getBirthdateApprox());
        assertEquals(new Date(0), familyEventClient.getClient().getBirthdate());

        assertEquals("FAMILY", familyEventClient.getEvent().getEntityType());
        assertEquals("FAMILY_REGISTRATION", familyEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyEventClient.getEvent().getBaseEntityId());
        assertEquals(1, familyEventClient.getEvent().getObs().size());
    }


    @Test
    public void processFamilyUpdateFormWithFamilyIdShouldReturnEventAndClient() {
        FamilyEventClient familyEventClient = JsonFormUtils.processFamilyUpdateForm(allSharedPreferences, TestDataUtils.FILLED_FAMILY_FORM, "1234455");
        assertNotNull(familyEventClient);
        assertEquals("763268733n", familyEventClient.getClient().getBaseEntityId());
        assertEquals("John", familyEventClient.getClient().getFirstName());
        assertEquals("Jack", familyEventClient.getClient().getLastName());
        assertTrue(familyEventClient.getClient().getBirthdateApprox());
        assertEquals(Utils.getDob(34), new SimpleDateFormat("dd-MM-yyyy").format(familyEventClient.getClient().getBirthdate()));

        assertEquals("34", familyEventClient.getClient().getAttribute("age_entered"));
        Address address = familyEventClient.getClient().getAddress("");
        assertEquals("Vllage 23", address.getCityVillage());
        assertEquals("Pepe 1", address.getAddressField("street"));

        assertEquals("1234455", familyEventClient.getClient().getRelationships().get("FAMILY").get(0));

        assertEquals("FAMILY_MEMBER", familyEventClient.getEvent().getEntityType());
        assertEquals("UPDATE_FAMILY_REGISTRATION", familyEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyEventClient.getEvent().getBaseEntityId());
        assertEquals(3, familyEventClient.getEvent().getObs().size());

    }

    @Test
    public void processFamilyHeadRegistrationFormShouldReturnEventClient() {

        FamilyEventClient familyEventClient = JsonFormUtils.processFamilyHeadRegistrationForm(allSharedPreferences, TestDataUtils.FILLED_FAMILY_FORM, "1234455");
        assertNotNull(familyEventClient);
        assertEquals("763268733n", familyEventClient.getClient().getBaseEntityId());
        assertEquals("Doe", familyEventClient.getClient().getFirstName());
        assertEquals("Jack", familyEventClient.getClient().getLastName());
        assertTrue(familyEventClient.getClient().getBirthdateApprox());
        assertEquals(Utils.getDob(34), new SimpleDateFormat("dd-MM-yyyy").format(familyEventClient.getClient().getBirthdate()));

        assertEquals("34", familyEventClient.getClient().getAttribute("age_entered"));
        assertTrue(familyEventClient.getClient().getAddresses().isEmpty());

        assertEquals("1234455", familyEventClient.getClient().getRelationships().get("FAMILY").get(0));

        assertEquals("FAMILY_MEMBER", familyEventClient.getEvent().getEntityType());
        assertEquals("FAMILY_MEMBER_REGISTRATION", familyEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyEventClient.getEvent().getBaseEntityId());
        assertEquals(3, familyEventClient.getEvent().getObs().size());

    }

    @Test
    public void testMergeAndSaveClientShouldSave() throws Exception {
        FamilyEventClient familyEventClient = JsonFormUtils.processFamilyHeadRegistrationForm(allSharedPreferences, TestDataUtils.FILLED_FAMILY_FORM, "1234455");
        String baseEntityId = familyEventClient.getClient().getBaseEntityId();
        when(ecSyncHelper.getClient(baseEntityId)).thenReturn(new JSONObject(org.smartregister.util.JsonFormUtils.gson.toJson(familyEventClient.getClient())));
        Client newClient = new Client(baseEntityId);
        newClient.withAddress(new Address().withCityVillage("Nairobi").withAddressField("street", "Padmore").withAddressType("home"));
        DateTime lastModified = new DateTime();
        newClient.withAttribute("last_edited", lastModified);
        newClient.setFirstName("Frank");
        JsonFormUtils.mergeAndSaveClient(ecSyncHelper, newClient);

        verify(ecSyncHelper).addClient(eq(baseEntityId), jsonObjectArgumentCaptor.capture());


        Client mergedClient = org.smartregister.util.JsonFormUtils.gson.fromJson(jsonObjectArgumentCaptor.getValue().toString(), Client.class);
        assertNotNull(mergedClient);
        assertEquals("763268733n", mergedClient.getBaseEntityId());
        assertEquals("Frank", mergedClient.getFirstName());
        assertEquals("Jack", mergedClient.getLastName());
        assertTrue(mergedClient.getBirthdateApprox());
        assertEquals(Utils.getDob(34), new SimpleDateFormat("dd-MM-yyyy").format(mergedClient.getBirthdate()));

        assertEquals(lastModified.toString(), mergedClient.getAttribute("last_edited"));
        assertFalse(mergedClient.getAddresses().isEmpty());
        assertEquals("Nairobi", mergedClient.getAddress("home").getCityVillage());
        assertEquals("Padmore", mergedClient.getAddress("home").getAddressField("street"));

        assertEquals("1234455", mergedClient.getRelationships().get("FAMILY").get(0));

    }

    @Test
    public void testSaveImage() {
        Whitebox.setInternalState(Utils.context(), "imageRepository", imageRepository);
        String id = UUID.randomUUID().toString();
        JsonFormUtils.saveImage("user", id, "src/main/res/mipmap-hdpi/ic_family.png");
        verify(imageRepository).add(profileImageCaptor.capture());
        assertNotNull(profileImageCaptor.getValue());
        assertEquals("user", profileImageCaptor.getValue().getAnmId());
        assertEquals(id, profileImageCaptor.getValue().getEntityID());
        assertEquals("profilepic", profileImageCaptor.getValue().getFilecategory());
        assertEquals(ImageRepository.TYPE_Unsynced, profileImageCaptor.getValue().getSyncStatus());
        File compressedFile = new File(profileImageCaptor.getValue().getFilepath());
        assertTrue(compressedFile.exists());
    }

    @Test
    public void testGetFieldValue() {
        String fieldValue = getFieldValue(TestDataUtils.FILLED_FAMILY_FORM, "step1",  "fam_name");
        assertEquals("John",  fieldValue);
    }

    @Test
    public void testGetAutoPopulatedJsonEditFormString() throws JSONException {
        Whitebox.setInternalState(Utils.context(), "imageRepository", imageRepository);
        when(imageRepository.findByEntityId(anyString())).thenReturn(profileImage);
        when(profileImage.getFilepath()).thenReturn("/tmp/image112/.png");
        LocationHelper.init(Utils.ALLOWED_LEVELS, Utils.DEFAULT_LOCATION_LEVEL);
        String formString = TestDataUtils.FAMILY_MEMBER_FORM;
        JSONObject form = JsonFormUtils.getAutoPopulatedJsonEditFormString(TestDataUtils.getCommonPersonObjectClient(), new JSONObject(formString), locationPickerView);
        assertNotNull(form);
        assertEquals("01-01-1982", org.smartregister.util.JsonFormUtils.getFieldValue(form.toString(), Constants.JSON_FORM_KEY.DOB));
        assertEquals(profileImage.getFilepath(), org.smartregister.util.JsonFormUtils.getFieldValue(form.toString(), Constants.KEY.PHOTO));
        assertEquals("12987632", org.smartregister.util.JsonFormUtils.getFieldValue(form.toString(), Constants.JSON_FORM_KEY.UNIQUE_ID));


    }


}
