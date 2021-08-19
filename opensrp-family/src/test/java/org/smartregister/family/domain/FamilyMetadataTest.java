package org.smartregister.family.domain;

import com.vijay.jsonwizard.activities.FormConfigurationJsonWizardFormActivity;
import com.vijay.jsonwizard.activities.JsonWizardFormActivity;

import org.junit.Test;
import org.smartregister.family.activity.FamilyWizardFormActivity;
import org.smartregister.family.activity.NoLocaleFamilyWizardFormActivity;
import org.smartregister.view.activity.BaseProfileActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

public class FamilyMetadataTest {

    @Test
    public void testUpdateFamilyActivityRegisterUpdatesWithCorrectDetails() {
        String formName = "family_register";
        String tableName = "ec_family";
        String registerEventType = "Family_Registration";
        String updateEventType = "Update_Family_Registration";
        String config = "family_register";
        String familyHeadRelationKey = "family_head";
        String familyCareGiverRelationKey = "primary_caregiver";
        FamilyMetadata familyMetadata = spy(new FamilyMetadata(FamilyWizardFormActivity.class, FamilyWizardFormActivity.class, BaseProfileActivity.class, "", true));
        assertNull(familyMetadata.familyRegister);

        familyMetadata.updateFamilyRegister(formName, tableName, registerEventType, updateEventType, config, familyHeadRelationKey, familyCareGiverRelationKey);

        FamilyMetadata.FamilyRegister familyRegister = familyMetadata.familyRegister;
        assertNotNull(familyRegister);
        assertThat(familyRegister.formName, is(equalTo(formName)));
        assertThat(familyRegister.tableName, is(equalTo(tableName)));
        assertThat(familyRegister.registerEventType, is(equalTo(registerEventType)));
        assertThat(familyRegister.updateEventType, is(equalTo(updateEventType)));
        assertThat(familyRegister.config, is(equalTo(config)));
        assertThat(familyRegister.familyHeadRelationKey, is(equalTo(familyHeadRelationKey)));
        assertThat(familyRegister.familyCareGiverRelationKey, is(equalTo(familyCareGiverRelationKey)));
    }

    @Test
    public void testFormConfigurationJsonWizardFormActivityConstructor(){
        FamilyMetadata familyMetadata = new FamilyMetadata(NoLocaleFamilyWizardFormActivity.class, NoLocaleFamilyWizardFormActivity.class, BaseProfileActivity.class, false, "");
        assertTrue(FormConfigurationJsonWizardFormActivity.class.isAssignableFrom(familyMetadata.familyFormActivity));
        assertTrue(FormConfigurationJsonWizardFormActivity.class.isAssignableFrom(familyMetadata.familyMemberFormActivity));
        assertThat(familyMetadata.profileActivity, is(equalTo(BaseProfileActivity.class)));
        assertThat(familyMetadata.formWizardValidateRequiredFieldsBefore, is(equalTo(false)));
    }

    @Test
    public void testJsonWizardFormActivityConstructor(){
        FamilyMetadata familyMetadata = new FamilyMetadata(FamilyWizardFormActivity.class, FamilyWizardFormActivity.class, BaseProfileActivity.class, "", false);
        assertTrue(JsonWizardFormActivity.class.isAssignableFrom(familyMetadata.familyFormActivity));
        assertTrue(JsonWizardFormActivity.class.isAssignableFrom(familyMetadata.familyMemberFormActivity));
        assertThat(familyMetadata.profileActivity, is(equalTo(BaseProfileActivity.class)));
        assertThat(familyMetadata.formWizardValidateRequiredFieldsBefore, is(equalTo(false)));
    }
}