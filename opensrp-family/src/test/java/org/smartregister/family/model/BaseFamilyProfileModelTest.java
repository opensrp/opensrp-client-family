package org.smartregister.family.model;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.Utils;
import org.smartregister.util.FormUtils;

import java.text.SimpleDateFormat;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 6/23/20.
 */
public class BaseFamilyProfileModelTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FormUtils formUtils;

    private BaseFamilyProfileModel model;

    private String familyName = UUID.randomUUID().toString();

    @Before
    public void setUp() {
        model = new BaseFamilyProfileModel(familyName);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        model.setFormUtils(formUtils);
    }

    @Test
    public void testGetFormAsJson() throws Exception {
        JSONObject form = new JSONObject(TestDataUtils.FAMILY_MEMBER_FORM);
        when(formUtils.getFormJson(anyString())).thenReturn(form);
        JSONObject results = model.getFormAsJson(Utils.metadata().familyMemberRegister.formName, familyName, "");
        assertNotNull(results);
        assertEquals(form, results);
    }

    @Test
    public void testGetFormAsJsonWithNonExistentForm() throws Exception {
        assertNull(model.getFormAsJson(Utils.metadata().familyMemberRegister.formName, familyName, ""));
    }


    @Test
    public void testProcessMemberRegistration() {
        FamilyEventClient familyEventClient = model.processMemberRegistration(TestDataUtils.FILLED_FAMILY_FORM, familyName);
        assertNotNull(familyEventClient);

        assertEquals("763268733n", familyEventClient.getClient().getBaseEntityId());
        assertEquals("John", familyEventClient.getClient().getFirstName());
        assertEquals("Jack", familyEventClient.getClient().getLastName());
        assertTrue(familyEventClient.getClient().getBirthdateApprox());
        assertEquals(Utils.getDob(34), new SimpleDateFormat("dd-MM-yyyy").format(familyEventClient.getClient().getBirthdate()));

        assertEquals("FAMILY_MEMBER", familyEventClient.getEvent().getEntityType());
        assertEquals("FAMILY_MEMBER_REGISTRATION", familyEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyEventClient.getEvent().getBaseEntityId());
        assertEquals(3, familyEventClient.getEvent().getObs().size());
    }

    @Test
    public void testProcessUpdateMemberRegistration() {
        FamilyEventClient familyEventClient = model.processUpdateMemberRegistration(TestDataUtils.FILLED_FAMILY_FORM, familyName);
        assertNotNull(familyEventClient);

        assertEquals("763268733n", familyEventClient.getClient().getBaseEntityId());
        assertEquals("John", familyEventClient.getClient().getFirstName());
        assertEquals("Jack", familyEventClient.getClient().getLastName());
        assertTrue(familyEventClient.getClient().getBirthdateApprox());
        assertEquals(Utils.getDob(34), new SimpleDateFormat("dd-MM-yyyy").format(familyEventClient.getClient().getBirthdate()));

        assertEquals("FAMILY_MEMBER", familyEventClient.getEvent().getEntityType());
        assertEquals("UPDATE_FAMILY_MEMBER_REGISTRATION", familyEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyEventClient.getEvent().getBaseEntityId());
        assertEquals(3, familyEventClient.getEvent().getObs().size());
    }

    @Test
    public void testUpdateWra() {
        FamilyEventClient familyEventClient = model.processMemberRegistration(TestDataUtils.FILLED_FAMILY_FORM, familyName);
        familyEventClient.getClient().setGender("female");
        model.updateWra(familyEventClient);
        assertEquals(4, familyEventClient.getEvent().getObs().size());
        Obs obs = familyEventClient.getEvent().getObs().get(3);
        assertEquals("162849AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", obs.getFieldCode());
    }


    @Test
    public void testProcessFamilyRegistrationForm() {
        FamilyEventClient familyEventClient = model.processFamilyRegistrationForm(TestDataUtils.FILLED_FAMILY_FORM, familyName);
        assertNotNull(familyEventClient);

        assertEquals("763268733n", familyEventClient.getClient().getBaseEntityId());
        assertEquals("John", familyEventClient.getClient().getFirstName());
        assertEquals("Jack", familyEventClient.getClient().getLastName());
        assertTrue(familyEventClient.getClient().getBirthdateApprox());
        assertEquals(Utils.getDob(34), new SimpleDateFormat("dd-MM-yyyy").format(familyEventClient.getClient().getBirthdate()));

        assertEquals("FAMILY_MEMBER", familyEventClient.getEvent().getEntityType());
        assertEquals("UPDATE_FAMILY_REGISTRATION", familyEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyEventClient.getEvent().getBaseEntityId());
        assertEquals(3, familyEventClient.getEvent().getObs().size());
    }

    @Test
    public void testSetters() {
        model.setFamilyName(familyName);
        model.setPrimaryCaregiver(true);
        model.setFamilyHead(true);
        assertEquals(familyName, Whitebox.getInternalState(model, "familyName"));
        assertEquals(true, Whitebox.getInternalState(model, "familyHead"));
        assertEquals(true, Whitebox.getInternalState(model, "primaryCaregiver"));
    }
}
