package org.smartregister.family.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.Context;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.TestData;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.repository.AllSharedPreferences;

import java.util.Date;

import static com.vijay.jsonwizard.constants.JsonFormConstants.STEP1;
import static com.vijay.jsonwizard.utils.FormUtils.fields;
import static com.vijay.jsonwizard.utils.FormUtils.getFieldJSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.family.util.JsonFormUtils.STEP2;
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

    private JSONObject originalForm = new JSONObject(TestData.REGISTER_FAMILY_FORM);

    @Mock
    private AllSharedPreferences allSharedPreferences;

    public JsonFormUtilsTest() throws JSONException {
    }

    @Before
    public void setUp() {
        FamilyLibrary.init(context, null, 1, 1);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
    }

    @Test
    public void getFormAsJson_ShouldReturnNullWithNullForm() throws Exception {
        assertNull(JsonFormUtils.getFormAsJson(null, null, null, null));
    }

    @Test
    public void getFormAsJson_ShouldPopulateEncounterLocation() throws Exception {

        JSONObject originalForm = new JSONObject();
        originalForm.put(METADATA, new JSONObject());
        JSONObject form = JsonFormUtils.getFormAsJson(originalForm, null, null, "location1");
        assertNotNull(form);
        assertEquals("location1", form.getJSONObject(METADATA).getString(ENCOUNTER_LOCATION));
    }

    @Test
    public void getFormAsJson_WithRegisterFamily_ShouldPopulateUniqueId() throws Exception {

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
    public void getFormAsJson_WithRegisterFamilyMember_ShouldPopulateUniqueId() throws Exception {
        originalForm.put(METADATA, new JSONObject());
        JSONObject form = JsonFormUtils.getFormAsJson(originalForm, "FAMILY_MEMBER_REGISTER", "1234", "location1");
        assertNotNull(form);
        assertEquals("location1", form.getJSONObject(METADATA).getString(ENCOUNTER_LOCATION));

        JSONArray step1 = fields(form, STEP1);
        assertEquals("1234", getFieldJSONObject(step1, Constants.JSON_FORM_KEY.UNIQUE_ID).getString(VALUE));


    }

    @Test
    public void updateJsonForm_ShouldUpdateFamilyName() throws Exception {
        JsonFormUtils.updateJsonForm(originalForm, "family122");
        JSONArray step1 = fields(originalForm, STEP1);
        assertEquals("family122", getFieldJSONObject(step1, Constants.JSON_FORM_KEY.FAMILY_NAME).getString(VALUE));
    }

    @Test
    public void updateJsonForm_WithNullForm() throws Exception {
        JSONObject form = null;
        JsonFormUtils.updateJsonForm(form, "family122");
        assertNull(form);
    }


    @Test
    public void processFamilyUpdateForm_ShouldReturnEventAndClient() {
        FamilyEventClient familyEventClient = JsonFormUtils.processFamilyUpdateForm(allSharedPreferences, TestData.FILLED_FAMILY_FORN);
        assertNotNull(familyEventClient);
        assertEquals("763268733n",familyEventClient.getClient().getBaseEntityId());
        assertEquals("John", familyEventClient.getClient().getFirstName());
        assertEquals("Family", familyEventClient.getClient().getLastName());
        assertFalse(familyEventClient.getClient().getBirthdateApprox());
        assertEquals(new Date(0), familyEventClient.getClient().getBirthdate());

        assertEquals("FAMILY", familyEventClient.getEvent().getEntityType());
        assertEquals("FAMILY_REGISTRATION", familyEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyEventClient.getEvent().getBaseEntityId());
        assertEquals(1,familyEventClient.getEvent().getObs().size());
    }
}
