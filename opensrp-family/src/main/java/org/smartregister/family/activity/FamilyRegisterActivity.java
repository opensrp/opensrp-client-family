package org.smartregister.family.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import com.vijay.jsonwizard.activities.JsonFormActivity;

import org.json.JSONObject;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.contract.FamilyRegisterContract;
import org.smartregister.family.fragment.FamilyRegisterFragment;
import org.smartregister.family.presenter.FamilyRegisterPresenter;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.AllConstants;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Arrays;
import java.util.List;

public class FamilyRegisterActivity extends BaseRegisterActivity implements FamilyRegisterContract.View {
    public static final String TAG = FamilyRegisterActivity.class.getCanonicalName();

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new FamilyRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }


    @Override
    protected void initializePresenter() {
        presenter = new FamilyRegisterPresenter(this);
    }

    @Override
    protected void registerBottomNavigation() {
        super.registerBottomNavigation();

        MenuItem clients = bottomNavigationView.getMenu().findItem(org.smartregister.R.id.action_clients);
        if (clients != null) {
            clients.setTitle(getString(R.string.families));
        }

        bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_search);
        bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_library);
    }

    @Override
    public void startRegistration() {
        startFormActivity(Constants.JSON_FORM.FAMILY_REGISTER, null, null);
    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData) {
        try {
            if (mBaseFragment instanceof FamilyRegisterFragment) {
                String locationId = FamilyLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
                ((FamilyRegisterPresenter) presenter).startForm(formName, entityId, metaData, locationId);
            }
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
    protected void onActivityResultExtended(int requestCode, int resultCode, Intent data) {
        if (requestCode == JsonFormUtils.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            try {
                String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                Log.d("JSONResult", jsonString);

                JSONObject form = new JSONObject(jsonString);
                if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Constants.EventType.FAMILY_REGISTRATION)) {
                    ((FamilyRegisterContract.Presenter) presenter).saveForm(jsonString, false);
                } else if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Constants.EventType.FAMILY_MEMBER_REGISTRATION)) {
                    ((FamilyRegisterContract.Presenter) presenter).saveForm(jsonString, false);
                }
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

        }
    }

    @Override
    public List<String> getViewIdentifiers() {
        return Arrays.asList(Constants.CONFIGURATION.FAMILY_REGISTER);
    }

}
