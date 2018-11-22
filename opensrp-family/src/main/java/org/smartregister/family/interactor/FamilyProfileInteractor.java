package org.smartregister.family.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.family.util.DBConstants;

/**
 * Created by keyman on 19/11/2018.
 */
public class FamilyProfileInteractor implements FamilyProfileContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    FamilyProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public FamilyProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
    }

    @Override
    public void refreshProfileView(final String baseEntityId, final boolean isForEdit, final FamilyProfileContract.InteractorCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final CommonPersonObject personObject = FamilyLibrary.getInstance().context().commonrepository(DBConstants.FAMILY_TABLE_NAME).findByBaseEntityId(baseEntityId);
                final CommonPersonObjectClient pClient = new CommonPersonObjectClient(personObject.getCaseId(),
                        personObject.getDetails(), "");
                pClient.setColumnmaps(personObject.getColumnmaps());

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isForEdit) {
                            callback.startFormForEdit(pClient);
                        } else {
                            callback.refreshProfileTopSection(pClient);
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

}
