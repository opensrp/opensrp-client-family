package org.smartregister.family.contract;

import android.content.Context;
import android.content.Intent;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.contract.BaseProfileContract;

public interface FamilyProfileContract {

    interface View extends BaseProfileContract.View {

        Context getApplicationContext();

        void startFormForEdit(int jsonFormActivityRequestCode, String metaData);

        void setProfileName(String fullName);

        void setProfileID(String uniqueId);

        void setProfileAge(String age);

        void setProfileImage(String baseEntityId);

        void setPhoneNumber(String phoneNumber);

    }

    interface Presenter extends BaseProfileContract.Presenter {

        FamilyProfileContract.View getProfileView();

        void fetchProfileData(String baseEntityId);

        void refreshProfileView(String baseEntityId);

        void processFormDetailsSave(Intent data, AllSharedPreferences allSharedPreferences);

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void refreshProfileView(String baseEntityId, boolean isForEdit, FamilyProfileContract.InteractorCallback callback);

    }

    interface InteractorCallback {

        void startFormForEdit(CommonPersonObjectClient client);

        void refreshProfileTopSection(CommonPersonObjectClient client);
    }

}
