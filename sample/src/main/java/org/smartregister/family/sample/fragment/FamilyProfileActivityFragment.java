package org.smartregister.family.sample.fragment;

import android.os.Bundle;

import org.smartregister.family.fragment.BaseFamilyProfileActivityFragment;
import org.smartregister.family.sample.model.FamilyProfileActivityModel;
import org.smartregister.family.sample.presenter.FamilyProfileActivityPresenter;
import org.smartregister.family.util.Constants;

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
        String familyBaseEntityId = getArguments().getString(Constants.INTENT_KEY.BASE_ENTITY_ID);
        presenter = new FamilyProfileActivityPresenter(this, new FamilyProfileActivityModel(), null, familyBaseEntityId);
    }
}
