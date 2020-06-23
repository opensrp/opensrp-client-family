package org.smartregister.family.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.configurableviews.helper.ConfigurableViewsHelper;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.Utils;
import org.smartregister.util.FormUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by samuelgithengi on 6/23/20.
 */
public class BaseFamilyRegisterModelTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConfigurableViewsHelper configurableViewsHelper;

    @Mock
    private List<String> views;

    @Mock
    private FormUtils formUtils;

    private BaseFamilyRegisterModel model;

    @Before
    public void setUp() {
        model = new BaseFamilyRegisterModel();
        Whitebox.setInternalState(ConfigurableViewsLibrary.getInstance(), "configurableViewsHelper", configurableViewsHelper);
        model.setFormUtils(formUtils);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
    }

    @Test
    public void testRegisterViewConfigurations() {
        model.registerViewConfigurations(views);
        verify(configurableViewsHelper).registerViewConfigurations(views);
    }

    @Test
    public void testUnRegisterViewConfigurations() {
        model.unregisterViewConfiguration(views);
        verify(configurableViewsHelper).unregisterViewConfiguration(views);
    }


    @Test
    public void testProcessRegistration() {
        List<FamilyEventClient> familyEventClientList = model.processRegistration(TestDataUtils.FILLED_FAMILY_FORM);
        assertEquals(2, familyEventClientList.size());


        FamilyEventClient familyEventClient = familyEventClientList.get(0);
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

        FamilyEventClient familyMemberEventClient = familyEventClientList.get(1);

        assertEquals("763268733n", familyMemberEventClient.getClient().getBaseEntityId());
        assertEquals("Doe", familyMemberEventClient.getClient().getFirstName());
        assertEquals("Jack", familyMemberEventClient.getClient().getLastName());
        assertTrue(familyMemberEventClient.getClient().getBirthdateApprox());
        assertEquals(Utils.getDob(34), new SimpleDateFormat("dd-MM-yyyy").format(familyMemberEventClient.getClient().getBirthdate()));

        assertEquals("FAMILY_MEMBER", familyMemberEventClient.getEvent().getEntityType());
        assertEquals("FAMILY_MEMBER_REGISTRATION", familyMemberEventClient.getEvent().getEventType());
        assertEquals("763268733n", familyMemberEventClient.getEvent().getBaseEntityId());
        assertEquals(3, familyMemberEventClient.getEvent().getObs().size());
    }


}
