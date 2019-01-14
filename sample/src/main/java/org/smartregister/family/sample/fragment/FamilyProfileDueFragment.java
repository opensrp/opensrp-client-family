package org.smartregister.family.sample.fragment;

import android.os.Bundle;
import android.view.View;

import org.smartregister.family.fragment.BaseFamilyProfileDueFragment;

public class FamilyProfileDueFragment extends BaseFamilyProfileDueFragment {

    public static BaseFamilyProfileDueFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        BaseFamilyProfileDueFragment fragment = new FamilyProfileDueFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    public void setUniqueID(String s) {

    }

    @Override
    protected String getMainCondition() {
        return null;
    }

    @Override
    protected String getDefaultSortQuery() {
        return null;
    }

    @Override
    protected void startRegistration() {

    }

    @Override
    protected void onViewClicked(View view) {
        super.onViewClicked(view);
    }

    @Override
    public void showNotFoundPopup(String s) {

    }
}
