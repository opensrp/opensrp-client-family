package org.smartregister.family.fragment;

import android.os.Bundle;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonWizardFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.viewstates.JsonFormFragmentViewState;

import org.smartregister.family.presenter.FamilyWizardFormFragmentPresenter;

public class FamilyWizardFormFragment extends JsonWizardFormFragment {

    public static final String TAG = FamilyWizardFormFragment.class.getName();

    private static final int MENU_NAVIGATION = 100001;

    public static FamilyWizardFormFragment getFormFragment(String stepName) {
        FamilyWizardFormFragment jsonFormFragment = new FamilyWizardFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(JsonFormConstants.JSON_FORM_KEY.STEPNAME, stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    protected JsonFormFragmentViewState createViewState() {
        return new JsonFormFragmentViewState();
    }

    @Override
    protected FamilyWizardFormFragmentPresenter createPresenter() {
        return new FamilyWizardFormFragmentPresenter(this, JsonFormInteractor.getInstance());
    }

    @Override
    public void updateVisibilityOfNextAndSave(boolean next, boolean save) {
        super.updateVisibilityOfNextAndSave(next, save);
        getMenu().findItem(com.vijay.jsonwizard.R.id.action_next).setVisible(next);
        getMenu().findItem(com.vijay.jsonwizard.R.id.action_save).setVisible(save);
    }
}
