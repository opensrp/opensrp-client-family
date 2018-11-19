package org.smartregister.family.contract;

import android.content.Intent;

import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.contract.BaseProfileContract;

import java.util.Map;

public interface FamilyProfileContract {

    interface View extends BaseProfileContract.View {

        void setProfileName(String fullName);

        void setProfileID(String ancId);

        void setProfileAge(String age);

        void setProfileImage(String baseEntityId);

        void setPhoneNumber(String phoneNumber);

    }

    interface Presenter extends BaseProfileContract.Presenter {

        FamilyProfileContract.View getProfileView();

        void refreshProfileView(String baseEntityId);

        void processFormDetailsSave(Intent data, AllSharedPreferences allSharedPreferences);

        void refreshProfileTopSection(Map<String, String> client);
    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void refreshProfileView(String baseEntityId);

    }

}
