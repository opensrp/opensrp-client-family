package org.smartregister.family.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.family.R;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.presenter.FamilyProfilePresenter;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.view.activity.BaseProfileActivity;


public class FamilyProfileActivity extends BaseProfileActivity implements FamilyProfileContract.View {

    public static final String TAG = FamilyProfileActivity.class.getName();

    private TextView nameView;
    private TextView ageView;
    private TextView gestationAgeView;
    private TextView uniqueIdView;
    private ImageView imageView;
    private String phoneNumber;

    @Override
    protected void initializePresenter() {
        presenter = new FamilyProfilePresenter(this);
    }

    @Override
    protected void setupViews() {

        super.setupViews();

        ageView = findViewById(R.id.textview_detail_two);
        gestationAgeView = findViewById(R.id.textview_detail_three);
        uniqueIdView = findViewById(R.id.textview_detail_one);
        nameView = findViewById(R.id.textview_name);
        imageView = findViewById(R.id.imageview_profile);

    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        String baseEntityId = getIntent().getStringExtra(Constants.INTENT_KEY.BASE_ENTITY_ID);
        ((FamilyProfilePresenter) presenter).refreshProfileView(baseEntityId);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy(isChangingConfigurations());

    }

    @Override
    protected void fetchProfileData() {
        String baseEntityId = getIntent().getStringExtra(Constants.INTENT_KEY.BASE_ENTITY_ID);
        ((FamilyProfilePresenter) presenter).fetchProfileData(baseEntityId);
    }

    @Override
    public void setProfileName(String fullName) {
        nameView.setText(fullName);
    }

    @Override
    public void setProfileID(String uniqueId) {
        uniqueIdView.setText(uniqueId);
    }

    @Override
    public void setProfileAge(String age) {
        ageView.setText(age);
    }

    @Override
    public void setProfileImage(String baseEntityId) {
        imageRenderHelper.refreshProfileImage(baseEntityId, imageView, Utils.getProfileImageResourceIDentifier());
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
    }

    @Override
    public void startFormForEdit(int jsonFormActivityRequestCode, String metaData) {
        Intent intent = new Intent(this, JsonFormUtils.class);
        intent.putExtra(Constants.INTENT_KEY.JSON, metaData);

        Log.d(TAG, "form is " + metaData);

        startActivityForResult(intent, jsonFormActivityRequestCode);

    }

}
