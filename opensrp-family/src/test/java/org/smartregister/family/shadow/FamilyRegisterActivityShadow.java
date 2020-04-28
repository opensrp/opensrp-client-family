package org.smartregister.family.shadow;

import android.support.v4.app.Fragment;
import android.view.View;

import org.smartregister.family.activity.BaseFamilyRegisterActivity;
import org.smartregister.family.model.BaseFamilyRegisterModel;
import org.smartregister.family.presenter.BaseFamilyRegisterPresenter;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;

/**
 * Created by samuelgithengi on 4/28/20.
 */
public class FamilyRegisterActivityShadow extends BaseFamilyRegisterActivity {
    @Override
    protected void initializePresenter() {
        presenter = new BaseFamilyRegisterPresenter(this, new BaseFamilyRegisterModel());
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new BaseRegisterFragment() {
            @Override
            protected void initializePresenter() {

            }

            @Override
            public void setUniqueID(String qrCode) {

            }

            @Override
            public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {

            }

            @Override
            protected String getMainCondition() {
                return null;
            }

            @Override
            protected String getDefaultSortQuery() {
                return null;
            }

            @Override
            protected void startRegistration() {

            }

            @Override
            protected void onViewClicked(View view) {

            }

            @Override
            public void showNotFoundPopup(String opensrpId) {

            }
        };
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }
}
