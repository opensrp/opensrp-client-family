package org.smartregister.family.activity;

import android.support.v4.view.ViewPager;

import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.presenter.FamilyProfilePresenter;
import org.smartregister.view.activity.BaseProfileActivity;


public class FamilyProfileActivity extends BaseProfileActivity implements FamilyProfileContract.View {

    @Override
    protected void initializePresenter() {
        presenter = new FamilyProfilePresenter(this);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {

    }

    @Override
    public void setProfileName(String fullName) {

    }

    @Override
    public void setProfileID(String ancId) {

    }

    @Override
    public void setProfileAge(String age) {

    }

    @Override
    public void setProfileImage(String baseEntityId) {

    }

    @Override
    public void setPhoneNumber(String phoneNumber) {

    }
}
