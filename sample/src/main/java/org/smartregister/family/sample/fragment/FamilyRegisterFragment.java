package org.smartregister.family.sample.fragment;

import org.smartregister.family.fragment.BaseFamilyRegisterFragment;
import org.smartregister.family.model.BaseFamilyRegisterFramentModel;
import org.smartregister.family.sample.presenter.FamilyRegisterFragmentPresenter;
import org.smartregister.view.activity.BaseRegisterActivity;

import java.util.HashMap;

public class FamilyRegisterFragment extends BaseFamilyRegisterFragment {

    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }

        String viewConfigurationIdentifier = ((BaseRegisterActivity) getActivity()).getViewIdentifiers().get(0);
        presenter = new FamilyRegisterFragmentPresenter(this, new BaseFamilyRegisterFramentModel(), viewConfigurationIdentifier);
    }

    @Override
    protected String getMainCondition() {
        return presenter().getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter().getDefaultSortQuery();
    }


    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {
        //do nothing
    }

}
