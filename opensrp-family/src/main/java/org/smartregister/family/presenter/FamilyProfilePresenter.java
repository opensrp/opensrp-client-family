package org.smartregister.family.presenter;

import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.FetchStatus;
import org.smartregister.family.R;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.interactor.FamilyProfileInteractor;
import org.smartregister.family.model.FamilyProfileModel;
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
public class FamilyProfilePresenter implements FamilyProfileContract.Presenter, FamilyProfileContract.InteractorCallBack {

    private static final String TAG = FamilyProfilePresenter.class.getCanonicalName();

    private WeakReference<FamilyProfileContract.View> view;
    private FamilyProfileContract.Interactor interactor;
    private FamilyProfileContract.Model model;

    private String familyBaseEntityId;

    public FamilyProfilePresenter(FamilyProfileContract.View loginView, String familyBaseEntityId) {
        this.view = new WeakReference<>(loginView);
        this.interactor = new FamilyProfileInteractor();
        this.model = new FamilyProfileModel();
        this.familyBaseEntityId = familyBaseEntityId;
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {

        view = null;//set to null on destroy

        // Inform interactor
        interactor.onDestroy(isChangingConfiguration);

        // Activity destroyed set interactor to null
        if (!isChangingConfiguration) {
            interactor = null;
        }

    }

    @Override
    public void fetchProfileData() {
        interactor.refreshProfileView(familyBaseEntityId, true, this);
    }

    @Override
    public void refreshProfileView() {
        interactor.refreshProfileView(familyBaseEntityId, false, this);
    }

    @Override
    public FamilyProfileContract.View getView() {
        if (view != null) {
            return view.get();
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

        getView().setProfileName(getName(firstName, lastName));


        String dobString = Utils.getDuration(Utils.getValue(client.getColumnmaps(), DBConstants.KEY.DOB, false));
        dobString = dobString.contains("y") ? dobString.substring(0, dobString.indexOf("y")) : dobString;

        getView().setProfileAge(dobString);

        String uniqueId = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.UNIQUE_ID, false);
        getView().setProfileID(uniqueId);


        getView().setProfileImage(client.getCaseId());

        String phoneNumber = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.PHONE_NUMBER, false);
        getView().setPhoneNumber(phoneNumber);
    }

    @Override
    public void startFormForEdit(CommonPersonObjectClient client) {
        String formMetadata = JsonFormUtils.getAutoPopulatedJsonEditFormString(getView().getApplicationContext(), client);
        try {
            getView().startFormForEdit(JsonFormUtils.REQUEST_CODE_GET_JSON, formMetadata);

        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    @Override
    public void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception {

        if (StringUtils.isBlank(entityId)) {
            Triple<String, String, String> triple = Triple.of(formName, metadata, currentLocationId);
            interactor.getNextUniqueId(triple, this);
            return;
        }

        JSONObject form = model.getFormAsJson(formName, entityId, currentLocationId);
        getView().startFormActivity(form);

    }


    @Override
    public void onNoUniqueId() {
        getView().displayShortToast(R.string.no_unique_id);
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
        try {
            startForm(triple.getLeft(), entityId, triple.getMiddle(), triple.getRight());
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            getView().displayToast(R.string.error_unable_to_start_form);
        }
    }

    @Override
    public void saveFamilyMember(String jsonString) {

        try {
            getView().showProgressDialog(R.string.saving_dialog_title);

            Pair<Client, Event> pair = model.processMemberRegistration(jsonString, familyBaseEntityId);
            if (pair == null) {
                return;
            }

            interactor.saveRegistration(pair, jsonString, false, this);

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        getView().refreshMemberList(FetchStatus.fetched);
        getView().hideProgressDialog();
    }

    public String getFamilyBaseEntityId() {
        return familyBaseEntityId;
    }
}
