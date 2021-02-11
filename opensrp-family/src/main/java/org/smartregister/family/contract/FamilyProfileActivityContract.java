package org.smartregister.family.contract;

import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import java.util.Set;

public interface FamilyProfileActivityContract {

    interface View extends BaseRegisterFragmentContract.View {

        FamilyProfileActivityContract.Presenter presenter();
    }

    interface Presenter extends BaseRegisterFragmentContract.Presenter {

        String getMainCondition();

        String getDefaultSortQuery();

        String getQueryTable();
    }

    interface Model extends BaseRegisterFragmentContract.Model{

        RegisterConfiguration defaultRegisterConfiguration();

        String countSelect(String tableName, String mainCondition);

        String mainSelect(String tableName, String mainCondition);

    }

}
