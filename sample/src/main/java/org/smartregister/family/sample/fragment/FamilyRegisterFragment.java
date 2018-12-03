package org.smartregister.family.sample.fragment;

import org.smartregister.family.fragment.BaseFamilyRegisterFragment;
import org.smartregister.family.sample.model.FamilyRegisterFramentModel;
import org.smartregister.family.sample.presenter.FamilyRegisterFragmentPresenter;
import org.smartregister.view.activity.BaseRegisterActivity;

public class FamilyRegisterFragment extends BaseFamilyRegisterFragment {

    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }

        String viewConfigurationIdentifier = ((BaseRegisterActivity) getActivity()).getViewIdentifiers().get(0);
        presenter = new FamilyRegisterFragmentPresenter(this, new FamilyRegisterFramentModel(), viewConfigurationIdentifier);
    }

    @Override
    protected String getMainCondition() {
        return presenter().getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter().getDefaultSortQuery();
    }

}
