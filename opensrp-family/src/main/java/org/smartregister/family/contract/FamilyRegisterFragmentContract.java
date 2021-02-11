package org.smartregister.family.contract;

import org.json.JSONArray;
import org.smartregister.configurableviews.model.Field;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.domain.Response;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import java.util.List;
import java.util.Set;

public interface FamilyRegisterFragmentContract {


    interface View extends BaseRegisterFragmentContract.View {

        FamilyRegisterFragmentContract.Presenter presenter();

    }

    interface Presenter extends BaseRegisterFragmentContract.Presenter {

        String getMainCondition();

        String getDefaultSortQuery();

        String getQueryTable();

    }

    interface Model extends BaseRegisterFragmentContract.Model {

        RegisterConfiguration defaultRegisterConfiguration();

        String countSelect(String tableName, String mainCondition);

        String mainSelect(String tableName, String mainCondition);

        JSONArray getJsonArray(Response<String> response);

    }


}
