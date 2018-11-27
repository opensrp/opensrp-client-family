package org.smartregister.family.contract;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.FetchStatus;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.contract.BaseProfileContract;

public interface FamilyProfileContract {

    interface View extends BaseProfileContract.View {

        Context getApplicationContext();

        void startFormActivity(JSONObject form);

        void startFormForEdit(int jsonFormActivityRequestCode, String metaData);

        void refreshMemberList(final FetchStatus fetchStatus);

        void displayShortToast(int resourceId);

        void setProfileName(String fullName);

        void setProfileID(String uniqueId);

        void setProfileAge(String age);

        void setProfileImage(String baseEntityId);

    }

    interface Presenter extends BaseProfileContract.Presenter {

        FamilyProfileContract.View getView();

        void startForm(String formName, String entityId, String metadata, String currentLocationId) throws Exception;

        void saveFamilyMember(String jsonString);

        void fetchProfileData();

        void refreshProfileView();

        void processFormDetailsSave(Intent data, AllSharedPreferences allSharedPreferences);

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void refreshProfileView(String baseEntityId, boolean isForEdit, FamilyProfileContract.InteractorCallBack callback);

        void getNextUniqueId(Triple<String, String, String> triple, FamilyProfileContract.InteractorCallBack callBack);

        void saveRegistration(final Pair<Client, Event> pair, final String jsonString, final boolean isEditMode, final FamilyProfileContract.InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void startFormForEdit(CommonPersonObjectClient client);

        void refreshProfileTopSection(CommonPersonObjectClient client);

        void onUniqueIdFetched(Triple<String, String, String> triple, String entityId);

        void onNoUniqueId();

        void onRegistrationSaved(boolean isEditMode);

    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

        Pair<Client, Event> processMemberRegistration(String jsonString, String familyBaseEntityId);

    }

}
