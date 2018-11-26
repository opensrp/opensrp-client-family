package org.smartregister.family.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.vijay.jsonwizard.activities.JsonFormActivity;

import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.domain.FetchStatus;
import org.smartregister.family.FamilyLibrary;
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
        String familyBaseEntityId = getIntent().getStringExtra(Constants.INTENT_KEY.BASE_ENTITY_ID);
        presenter = new FamilyProfilePresenter(this, familyBaseEntityId);
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

        ((FamilyProfilePresenter) presenter).refreshProfileView();
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
            startFormActivity(Constants.JSON_FORM.FAMILY_MEMBER_REGISTER, null, null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void fetchProfileData() {

        ((FamilyProfilePresenter) presenter).fetchProfileData();
    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData) {
        try {
            String locationId = FamilyLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
            ((FamilyProfilePresenter) presenter).startForm(formName, entityId, metaData, locationId);

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            // displayToast(getString(R.string.error_unable_to_start_form));
        }

    }

    @Override
    public void startFormActivity(JSONObject form) {
        Intent intent = new Intent(this, JsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, form.toString());
        startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void startFormForEdit(int jsonFormActivityRequestCode, String metaData) {
        Intent intent = new Intent(this, JsonFormActivity.class);
        intent.putExtra(Constants.INTENT_KEY.JSON, metaData);

        Log.d(TAG, "form is " + metaData);

        startActivityForResult(intent, jsonFormActivityRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JsonFormUtils.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            try {
                String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                Log.d("JSONResult", jsonString);

                JSONObject form = new JSONObject(jsonString);
                if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Constants.EventType.FAMILY_REGISTRATION)) {
                    //((FamilyProfileContract.Presenter) presenter).updateFamilyRegister(jsonString, false);
                } else if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Constants.EventType.FAMILY_MEMBER_REGISTRATION)) {
                    ((FamilyProfileContract.Presenter) presenter).saveFamilyMember(jsonString);
                }
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

        }
    }

    @Override
    public void refreshMemberList(final FetchStatus fetchStatus) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            FamilyProfileMemberFragment memberFragment = getProfileMemberFragment();
            if (memberFragment != null && fetchStatus.equals(FetchStatus.fetched)) {
                memberFragment.refreshListView();
            }
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    FamilyProfileMemberFragment memberFragment = getProfileMemberFragment();
                    if (memberFragment != null && fetchStatus.equals(FetchStatus.fetched)) {
                        memberFragment.refreshListView();
                    }
                }
            });
        }
    }

    @Override
    public void displayShortToast(int resourceId) {
        Utils.showShortToast(this, getString(resourceId));
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
        // TODO Set phone number
    }

    public FamilyProfileMemberFragment getProfileMemberFragment() {
        Fragment fragment = adapter.getItem(0);
        if (fragment != null && fragment instanceof FamilyProfileMemberFragment) {
            return (FamilyProfileMemberFragment) fragment;
        }
        return null;
    }

}
