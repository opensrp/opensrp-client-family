package org.smartregister.family.fragment;

import android.os.Bundle;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;
import com.vijay.jsonwizard.fragments.JsonWizardFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.smartregister.family.presenter.FamilyWizardFormFragmentPresenter;

public class FamilyWizardFormFragment extends JsonWizardFormFragment {

    public static final String TAG = FamilyWizardFormFragment.class.getName();

    public static FamilyWizardFormFragment getFormFragment(String stepName) {
        FamilyWizardFormFragment jsonFormFragment = new FamilyWizardFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(JsonFormConstants.JSON_FORM_KEY.STEPNAME, stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    protected FamilyWizardFormFragmentPresenter createPresenter() {
        return new FamilyWizardFormFragmentPresenter(this, JsonFormInteractor.getInstance());
    }

    @Override
    public void updateVisibilityOfNextAndSave(boolean next, boolean save) {
        super.updateVisibilityOfNextAndSave(next, save);
        Form form = getForm();
        if (form != null && form.isWizard()) {
            getMenu().findItem(com.vijay.jsonwizard.R.id.action_next).setVisible(false);
            getMenu().findItem(com.vijay.jsonwizard.R.id.action_save).setVisible(save);
        } else {
            getMenu().findItem(com.vijay.jsonwizard.R.id.action_next).setVisible(next);
            getMenu().findItem(com.vijay.jsonwizard.R.id.action_save).setVisible(save);
        }
    }

    private Form getForm() {
        if (getActivity() != null && getActivity() instanceof JsonFormActivity) {
            return ((JsonFormActivity) getActivity()).getForm();
        }
        return null;
    }
}
