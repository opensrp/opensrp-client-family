package org.smartregister.family.sample.presenter;

import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.presenter.BaseFamilyProfilePresenter;

public class FamilyProfilePresenter extends BaseFamilyProfilePresenter {

    public FamilyProfilePresenter(FamilyProfileContract.View loginView, FamilyProfileContract.Model model, String familyBaseEntityId, String familyHead, String primaryCaregiver, String familyName) {
        super(loginView, model, familyBaseEntityId, familyHead, primaryCaregiver, familyName);
    }

}
