package org.smartregister.family.sample.fragment;

import android.os.Bundle;

import org.smartregister.family.fragment.BaseFamilyProfileActivityFragment;
import org.smartregister.family.model.BaseFamilyProfileActivityModel;
import org.smartregister.family.presenter.BaseFamilyProfileActivityPresenter;
import org.smartregister.family.util.Constants;

import java.util.HashMap;

public class FamilyProfileActivityFragment extends BaseFamilyProfileActivityFragment {
    public static BaseFamilyProfileActivityFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        BaseFamilyProfileActivityFragment fragment = new FamilyProfileActivityFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializePresenter() {
        String familyBaseEntityId = getArguments().getString(Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID);
        presenter = new BaseFamilyProfileActivityPresenter(this, new BaseFamilyProfileActivityModel(), null, familyBaseEntityId);
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {
        //do nothing
    }
}
