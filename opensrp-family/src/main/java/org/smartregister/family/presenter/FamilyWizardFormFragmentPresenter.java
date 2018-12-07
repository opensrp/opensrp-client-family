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
        ValidationStatus validationStatus = this.writeValuesAndValidate(mainView);
        if (validationStatus.isValid()) {
            JsonFormFragment next = FamilyWizardFormFragment.getFormFragment(mStepDetails.optString(Constants.JSON_FORM_EXTRA.NEXT));
            getView().hideKeyBoard();
            getView().transactThis(next);
        } else {
            validationStatus.requestAttention();
            getView().showToast(validationStatus.getErrorMessage());
        }
    }

    public boolean intermediatePage() {
        return this.mStepDetails != null && this.mStepDetails.has(Constants.JSON_FORM_EXTRA.NEXT);
    }
}