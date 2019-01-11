package org.smartregister.family.domain;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;

import org.smartregister.view.activity.BaseProfileActivity;

public class FamilyMetadata {

    public final Class familyFormActivity;
    public final Class familyMemberFormActivity;
    public final Class profileActivity;

    public FamilyRegister familyRegister;
    public FamilyMemberRegister familyMemberRegister;

    public FamilyMetadata(Class<? extends JsonWizardFormActivity> familyFormActivity, Class<? extends JsonWizardFormActivity> familyMemberFormActivity, Class<? extends BaseProfileActivity> profileActivity) {
        this.familyFormActivity = familyFormActivity;
        this.familyMemberFormActivity = familyMemberFormActivity;
        this.profileActivity = profileActivity;
    }

    public void updateFamilyRegister(String formName, String tableName, String registerEventType, String updateEventType, String config, String familyHeadRelationKey, String familyCareGiverRelationKey) {
        this.familyRegister = new FamilyRegister(formName, tableName, registerEventType, updateEventType, config, familyHeadRelationKey, familyCareGiverRelationKey);
    }

    public void updateFamilyMemberRegister(String formName, String tableName, String registerEventType, String updateEventType, String config, String familyRelationKey) {
        this.familyMemberRegister = new FamilyMemberRegister(formName, tableName, registerEventType, updateEventType, config, familyRelationKey);
    }

    public class FamilyRegister {

        public final String formName;

        public final String tableName;

        public final String registerEventType;

        public final String updateEventType;

        public final String config;

        public final String familyHeadRelationKey;

        public final String familyCareGiverRelationKey;


        public FamilyRegister(String formName, String tableName, String registerEventType, String updateEventType, String config, String familyHeadRelationKey, String familyCareGiverRelationKey) {
            this.formName = formName;
            this.tableName = tableName;
            this.registerEventType = registerEventType;
            this.updateEventType = updateEventType;
            this.config = config;
            this.familyHeadRelationKey = familyHeadRelationKey;
            this.familyCareGiverRelationKey = familyCareGiverRelationKey;
        }
    }

    public class FamilyMemberRegister {

        public final String formName;

        public final String tableName;

        public final String registerEventType;

        public final String updateEventType;

        public final String config;

        public final String familyRelationKey;


        public FamilyMemberRegister(String formName, String tableName, String registerEventType, String updateEventType, String config, String familyRelationKey) {
            this.formName = formName;
            this.tableName = tableName;
            this.registerEventType = registerEventType;
            this.updateEventType = updateEventType;
            this.config = config;
            this.familyRelationKey = familyRelationKey;
        }

    }
}
