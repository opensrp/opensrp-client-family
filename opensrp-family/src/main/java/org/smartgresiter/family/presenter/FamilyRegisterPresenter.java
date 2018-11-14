package org.smartgresiter.family.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartgresiter.family.contract.FamilyRegisterContract;
import org.smartgresiter.family.interactor.FamilyRegisterInteractor;
import org.smartgresiter.family.model.FamilyRegisterModel;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.FetchStatus;
import org.smartregister.repository.AllSharedPreferences;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by keyman on 12/11/2018.
 */
public class FamilyRegisterPresenter implements FamilyRegisterContract.Presenter, FamilyRegisterContract.InteractorCallBack {

    public static final String TAG = FamilyRegisterPresenter.class.getName();

    private WeakReference<FamilyRegisterContract.View> viewReference;
    private FamilyRegisterContract.Interactor interactor;
    private FamilyRegisterContract.Model model;

    public FamilyRegisterPresenter(FamilyRegisterContract.View view) {
        viewReference = new WeakReference<>(view);
        interactor = new FamilyRegisterInteractor();
        model = new FamilyRegisterModel();
    }

    public void setModel(FamilyRegisterContract.Model model) {
        this.model = model;
    }

    public void setInteractor(FamilyRegisterContract.Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void registerViewConfigurations(List<String> viewIdentifiers) {
        model.registerViewConfigurations(viewIdentifiers);
    }

    @Override
    public void unregisterViewConfiguration(List<String> viewIdentifiers) {
        model.unregisterViewConfiguration(viewIdentifiers);
    }

    @Override
    public void saveLanguage(String language) {
        model.saveLanguage(language);
        getView().displayToast(language + " selected");
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
    public void closeFamilyRecord(String jsonString) {

        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);

            Log.d("JSONResult", jsonString);
            //getView().showProgressDialog(jsonString.contains(Constants.EventType.CLOSE) ? R.string.removing_dialog_title : R.string.saving_dialog_title);

            interactor.removeFamilyFromRegister(jsonString, allSharedPreferences.fetchRegisteredANM());

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));

        }
    }

    @Override
    public void saveForm(String jsonString, boolean isEditMode) {

        try {

            //getView().showProgressDialog(R.string.saving_dialog_title);

            Pair<Client, Event> pair = model.processRegistration(jsonString);
            if (pair == null) {
                return;
            }

            interactor.saveRegistration(pair, jsonString, isEditMode, this);

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void onNoUniqueId() {
        //getView().displayShortToast(R.string.no_openmrs_id);
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
        try {
            startForm(triple.getLeft(), entityId, triple.getMiddle(), triple.getRight());
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            // getView().displayToast(R.string.error_unable_to_start_form);
        }
    }

    @Override
    public void onRegistrationSaved(boolean isEdit) {
        getView().refreshList(FetchStatus.fetched);
        getView().hideProgressDialog();
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {

        viewReference = null;//set to null on destroy
        // Inform interactor
        interactor.onDestroy(isChangingConfiguration);
        // Activity destroyed set interactor to null
        if (!isChangingConfiguration) {
            interactor = null;
            model = null;
        }
    }

    @Override
    public void updateInitials() {
        String initials = model.getInitials();
        if (initials != null) {
            getView().updateInitialsText(initials);
        }
    }

    private FamilyRegisterContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

}
