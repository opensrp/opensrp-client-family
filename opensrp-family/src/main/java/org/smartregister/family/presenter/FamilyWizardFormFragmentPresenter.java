package org.smartregister.family.presenter;

import android.widget.LinearLayout;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.utils.ValidationStatus;

import org.smartregister.family.fragment.FamilyWizardFormFragment;
import org.smartregister.family.util.Constants;

/**
 * Created by keyman on 04/08/18.
 */
public class FamilyWizardFormFragmentPresenter extends JsonFormFragmentPresenter {

    public static final String TAG = FamilyWizardFormFragmentPresenter.class.getName();

    public FamilyWizardFormFragmentPresenter(JsonFormFragment formFragment, JsonFormInteractor jsonFormInteractor) {
        super(formFragment, jsonFormInteractor);
    }

    @Override
    public void setUpToolBar() {
        super.setUpToolBar();
    }

    @Override
    public void onNextClick(LinearLayout mainView) {

        validateAndWriteValues();
        boolean validateOnSubmit = validateOnSubmit();
        if (validateOnSubmit && getIncorrectlyFormattedFields().isEmpty()) {
            moveToNextWizardStep();
        } else if (isFormValid()) {
            moveToNextWizardStep();
        } else {
            getView().showSnackBar(getView().getContext().getResources()
                    .getString(com.vijay.jsonwizard.R.string.json_form_on_next_error_msg));
        }
    }

    private void moveToNextWizardStep() {
        JsonFormFragment next = FamilyWizardFormFragment.getFormFragment(mStepDetails.optString(Constants.JSON_FORM_EXTRA.NEXT));
        getView().hideKeyBoard();
        getView().transactThis(next);
    }

    public boolean intermediatePage() {
        return this.mStepDetails != null && this.mStepDetails.has(Constants.JSON_FORM_EXTRA.NEXT);
    }
}