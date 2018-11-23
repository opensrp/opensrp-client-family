package org.smartregister.family.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.family.R;
import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.fragment.FamilyProfileActivityFragment;
import org.smartregister.family.fragment.FamilyProfileDueFragment;
import org.smartregister.family.fragment.FamilyProfileMemberFragment;
import org.smartregister.family.presenter.FamilyProfilePresenter;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.view.activity.BaseProfileActivity;

public class FamilyProfileActivity extends BaseProfileActivity implements FamilyProfileContract.View {

    public static final String TAG = FamilyProfileActivity.class.getName();

    private TextView nameView;
    private TextView detailOneView;
    private TextView detailTwoView;
    private TextView detailThreeView;
    private ImageView imageView;
    private String phoneNumber;

    private ViewPagerAdapter adapter;

    @Override
    protected void initializePresenter() {
        presenter = new FamilyProfilePresenter(this);
    }

    @Override
    protected void setupViews() {

        super.setupViews();

        detailOneView = findViewById(R.id.textview_detail_one);
        detailTwoView = findViewById(R.id.textview_detail_two);
        detailThreeView = findViewById(R.id.textview_detail_three);

        nameView = findViewById(R.id.textview_name);
        imageView = findViewById(R.id.imageview_profile);

    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FamilyProfileMemberFragment profileMemberFragment = FamilyProfileMemberFragment.newInstance(this.getIntent().getExtras());
        FamilyProfileDueFragment profileDueFragment = FamilyProfileDueFragment.newInstance(this.getIntent().getExtras());
        FamilyProfileActivityFragment profileActivityFragment = FamilyProfileActivityFragment.newInstance(this.getIntent().getExtras());

        adapter.addFragment(profileMemberFragment, this.getString(R.string.member));
        adapter.addFragment(profileDueFragment, this.getString(R.string.due));
        adapter.addFragment(profileActivityFragment, this.getString(R.string.activity));

        viewPager.setAdapter(adapter);

        return viewPager;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_member) {
            Fragment fragment = adapter.getItem(0);
            if (fragment instanceof FamilyProfileMemberFragment) {
                FamilyProfileMemberFragment memberFragment = (FamilyProfileMemberFragment) fragment;
                memberFragment.addMember();
            }
        }

        return super.onOptionsItemSelected(item);
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
        detailOneView.setText(String.format(getString(R.string.unique_id_text), uniqueId));
    }

    @Override
    public void setProfileAge(String age) {
        detailTwoView.setText(String.format(getString(R.string.age_text), age));
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
