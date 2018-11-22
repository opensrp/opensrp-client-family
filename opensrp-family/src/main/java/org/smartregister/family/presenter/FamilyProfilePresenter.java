package org.smartregister.family.presenter;

import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.interactor.FamilyProfileInteractor;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.repository.AllSharedPreferences;

import java.lang.ref.WeakReference;

import static org.smartregister.util.Utils.getName;

/**
 * Created by keyman on 19/11/2018.
 */
public class FamilyProfilePresenter implements FamilyProfileContract.Presenter, FamilyProfileContract.InteractorCallback {

    private static final String TAG = FamilyProfilePresenter.class.getCanonicalName();

    private WeakReference<FamilyProfileContract.View> mProfileView;
    private FamilyProfileContract.Interactor mProfileInteractor;

    public FamilyProfilePresenter(FamilyProfileContract.View loginView) {
        mProfileView = new WeakReference<>(loginView);
        mProfileInteractor = new FamilyProfileInteractor();
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
    public void fetchProfileData(String baseEntityId) {
        mProfileInteractor.refreshProfileView(baseEntityId, true, this);
    }

    @Override
    public void refreshProfileView(String baseEntityId) {
        mProfileInteractor.refreshProfileView(baseEntityId, false, this);
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
    public void refreshProfileTopSection(CommonPersonObjectClient client) {

        if (client == null || client.getColumnmaps() == null) {
            return;
        }

        String firstName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.FIRST_NAME, true);
        String lastName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.LAST_NAME, true);

        getProfileView().setProfileName(getName(firstName, lastName));


        String dobString = Utils.getDuration(Utils.getValue(client.getColumnmaps(), DBConstants.KEY.DOB, false));
        dobString = dobString.contains("y") ? dobString.substring(0, dobString.indexOf("y")) : dobString;

        getProfileView().setProfileAge(dobString);

        String uniqueId = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.UNIQUE_ID, false);
        getProfileView().setProfileID(uniqueId);


        getProfileView().setProfileImage(client.getCaseId());

        String phoneNumber = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.PHONE_NUMBER, false);
        getProfileView().setPhoneNumber(phoneNumber);
    }

    @Override
    public void startFormForEdit(CommonPersonObjectClient client) {
        String formMetadata = JsonFormUtils.getAutoPopulatedJsonEditFormString(getProfileView().getApplicationContext(), client);
        try {
            getProfileView().startFormForEdit(JsonFormUtils.REQUEST_CODE_GET_JSON, formMetadata);

        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

}
