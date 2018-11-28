package org.smartregister.family.sample.presenter;

import org.smartregister.family.contract.FamilyProfileMemberContract;
import org.smartregister.family.presenter.BaseFamilyProfileMemberPresenter;

public class FamilyProfileMemberPresenter extends BaseFamilyProfileMemberPresenter {

    public FamilyProfileMemberPresenter(FamilyProfileMemberContract.View view, String viewConfigurationIdentifier, String familyBaseEntityId) {
        super(view, viewConfigurationIdentifier, familyBaseEntityId);
    }
}
