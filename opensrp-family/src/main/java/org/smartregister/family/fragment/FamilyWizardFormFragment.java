package org.smartregister.family.fragment;

import android.os.Bundle;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonWizardFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.viewstates.JsonFormFragmentViewState;

import org.smartregister.family.FamilyLibrary;
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
        if (!FamilyLibrary.getInstance().metadata().formValidateRequiredFieldsBefore) {
            this.getMenu().findItem(com.vijay.jsonwizard.R.id.action_save).setVisible(save);
        }
    }

    public void validateActivateNext() {
        if (!isVisible()) { //form fragment is initializing or not the last page
            return;
        }
        ValidationStatus validationStatus = null;
        for (View dataView : getJsonApi().getFormDataViews()) {

            validationStatus = getPresenter().validate(this, dataView, false);
            if (!validationStatus.isValid()) {
                break;
            }
        }

        if (validationStatus != null && validationStatus.isValid()) {
            if (getPresenter().intermediatePage()) {
                //getMenu().findItem(com.vijay.jsonwizard.R.id.action_next).setVisible(true);
            } else {
                getMenu().findItem(com.vijay.jsonwizard.R.id.action_save).setVisible(true);
            }
        } else {
            if (getPresenter().intermediatePage()) {
                //getMenu().findItem(com.vijay.jsonwizard.R.id.action_next).setVisible(false);
            } else {
                getMenu().findItem(com.vijay.jsonwizard.R.id.action_save).setVisible(false);
            }
        }
    }

    public FamilyWizardFormFragmentPresenter getPresenter() {
        return (FamilyWizardFormFragmentPresenter) presenter;
    }
}
