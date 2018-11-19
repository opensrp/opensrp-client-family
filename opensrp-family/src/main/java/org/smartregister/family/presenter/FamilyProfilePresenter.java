package org.smartregister.family.presenter;

import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.repository.AllSharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by keyman on 19/11/2018.
 */
public class FamilyProfilePresenter implements FamilyProfileContract.Presenter {

    private static final String TAG = FamilyProfilePresenter.class.getCanonicalName();

    private WeakReference<FamilyProfileContract.View> mProfileView;
    private FamilyProfileContract.Interactor mProfileInteractor;

    public FamilyProfilePresenter(FamilyProfileContract.View loginView) {
        mProfileView = new WeakReference<>(loginView);
        //mProfileInteractor = new ProfileInteractor(this);
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {

        mProfileView = null;//set to null on destroy

        // Inform interactor
        mProfileInteractor.onDestroy(isChangingConfiguration);

        // Activity destroyed set interactor to null
        if (!isChangingConfiguration) {
            mProfileInteractor = null;
        }

    }

    @Override
    public void refreshProfileView(String baseEntityId) {
        mProfileInteractor.refreshProfileView(baseEntityId);
    }

    @Override
    public FamilyProfileContract.View getProfileView() {
        if (mProfileView != null) {
            return mProfileView.get();
        } else {
            return null;
        }
    }

    @Override
    public void processFormDetailsSave(Intent data, AllSharedPreferences allSharedPreferences) {
        try {
            String jsonString = data.getStringExtra(Constants.INTENT_KEY.JSON);
            Log.d("JSONResult", jsonString);

            JSONObject form = new JSONObject(jsonString);

            /*getProfileView().showProgressDialog(form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Constants.EventType.CLOSE) ? R.string.removing_dialog_title : R.string.saving_dialog_title);

            if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Constants.EventType.UPDATE_REGISTRATION)) {

                Pair<Client, Event> values = JsonFormUtils.processRegistrationForm(allSharedPreferences, jsonString);
                mRegisterInteractor.saveRegistration(values, jsonString, true, this);

            } else if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Constants.EventType.CLOSE)) {

                mRegisterInteractor.removeWomanFromANCRegister(jsonString, allSharedPreferences.fetchRegisteredANM());

            } else {
                getProfileView().hideProgressDialog();
            }*/
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void refreshProfileTopSection(Map<String, String> client) {

        getProfileView().setProfileName(client.get(DBConstants.KEY.FIRST_NAME) + " " + client.get(DBConstants.KEY.LAST_NAME));
        getProfileView().setProfileAge(String.valueOf(Utils.getAgeFromDate(client.get(DBConstants.KEY.DOB))));
        getProfileView().setProfileID(client.get(DBConstants.KEY.UNIQUE_ID));
        getProfileView().setProfileImage(client.get(DBConstants.KEY.BASE_ENTITY_ID));
        getProfileView().setPhoneNumber(client.get(DBConstants.KEY.PHONE_NUMBER));
    }
}
