package org.smartgresiter.family.interactor;

import android.support.annotation.VisibleForTesting;
import android.util.Pair;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.smartgresiter.family.FamilyLibrary;
import org.smartgresiter.family.contract.FamilyRegisterContract;
import org.smartgresiter.family.util.AppExecutors;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.UniqueId;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.UniqueIdRepository;

/**
 * Created by keyman 12/11/2018.
 */
public class FamilyRegisterInteractor implements FamilyRegisterContract.Interactor {


    public static final String TAG = FamilyRegisterInteractor.class.getName();

    public enum type {SAVED, UPDATED}


    private AppExecutors appExecutors;

    private AllSharedPreferences allSharedPreferences;

    @VisibleForTesting
    FamilyRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public FamilyRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void getNextUniqueId(final Triple<String, String, String> triple, final FamilyRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                UniqueId uniqueId = getUniqueIdRepository().getNextUniqueId();
                final String entityId = uniqueId != null ? uniqueId.getOpenmrsId() : "";
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (StringUtils.isBlank(entityId)) {
                            callBack.onNoUniqueId();
                        } else {
                            callBack.onUniqueIdFetched(triple, entityId);
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(final Pair<Client, Event> pair, final String jsonString, final boolean isEditMode, final FamilyRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                saveRegistration(pair, jsonString, isEditMode);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onRegistrationSaved(isEditMode);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void removeFamilyFromRegister(final String closeFormJsonString, final String providerId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //TODO add functionality to remove family from register
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    private void saveRegistration(Pair<Client, Event> pair, String jsonString, boolean isEditMode) {

        //TODO save registration
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        //TODO set presenter or model to null
    }

    public AllSharedPreferences getAllSharedPreferences() {
        if (allSharedPreferences == null) {
            allSharedPreferences = FamilyLibrary.getInstance().context().allSharedPreferences();
        }
        return allSharedPreferences;
    }

    public void setAllSharedPreferences(AllSharedPreferences allSharedPreferences) {
        this.allSharedPreferences = allSharedPreferences;
    }

    public UniqueIdRepository getUniqueIdRepository() {
        return FamilyLibrary.getInstance().getUniqueIdRepository();
    }
}
