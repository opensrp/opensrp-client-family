package org.smartregister.family.interactor;

import org.smartregister.family.contract.FamilyProfileContract;

/**
 * Created by keyman on 19/11/2018.
 */
public class FamilyProfileInteractor implements FamilyProfileContract.Interactor {
    private FamilyProfileContract.Presenter mProfilePresenter;

    public FamilyProfileInteractor(FamilyProfileContract.Presenter loginPresenter) {
        this.mProfilePresenter = loginPresenter;
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mProfilePresenter = null;
        }
    }

    @Override
    public void refreshProfileView(String baseEntityId) {
        //new FetchProfileDataTask(false).execute(baseEntityId);
    }

    public FamilyProfileContract.View getProfileView() {
        return mProfilePresenter.getProfileView();
    }
}
