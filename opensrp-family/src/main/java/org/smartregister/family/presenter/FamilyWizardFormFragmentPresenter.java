package org.smartregister.family.presenter;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

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

}