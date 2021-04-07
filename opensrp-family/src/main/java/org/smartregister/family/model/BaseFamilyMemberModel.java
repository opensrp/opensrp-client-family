package org.smartregister.family.model;

public class BaseFamilyMemberModel {
    private final String lastName;
    private final String baseEntityId;
    private final String familyBaseEntityId;
    private final String entityType;

    public BaseFamilyMemberModel(String lastName, String baseEntityId, String familyBaseEntityId,String entityType) {
        this.lastName = lastName;
        this.baseEntityId = baseEntityId;
        this.familyBaseEntityId = familyBaseEntityId;
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public String getFamilyBaseEntityId() {
        return familyBaseEntityId;
    }
}
