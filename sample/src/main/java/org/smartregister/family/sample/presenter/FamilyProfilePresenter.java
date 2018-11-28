package org.smartregister.family.sample.presenter;

import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.presenter.BaseFamilyProfilePresenter;

public class FamilyProfilePresenter extends BaseFamilyProfilePresenter {

    public FamilyProfilePresenter(FamilyProfileContract.View loginView, String familyBaseEntityId) {
        super(loginView, familyBaseEntityId);
    }

}
