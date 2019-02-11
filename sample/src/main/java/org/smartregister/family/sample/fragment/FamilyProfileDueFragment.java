package org.smartregister.family.sample.fragment;

import android.os.Bundle;

import org.smartregister.family.fragment.BaseFamilyProfileDueFragment;
import org.smartregister.family.sample.model.FamilyProfileDueModel;
import org.smartregister.family.sample.presenter.FamilyProfileDuePresenter;
import org.smartregister.family.util.Constants;

import java.util.HashMap;

public class FamilyProfileDueFragment extends BaseFamilyProfileDueFragment {

    public static BaseFamilyProfileDueFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        BaseFamilyProfileDueFragment fragment = new FamilyProfileDueFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializePresenter() {
        String familyBaseEntityId = getArguments().getString(Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID);
        presenter = new FamilyProfileDuePresenter(this, new FamilyProfileDueModel(), null, familyBaseEntityId);
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {

    }
}
