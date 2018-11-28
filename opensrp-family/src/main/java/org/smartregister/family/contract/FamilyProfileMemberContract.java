package org.smartregister.family.contract;

import org.smartregister.view.contract.BaseRegisterFragmentContract;

import java.util.Set;

public interface FamilyProfileMemberContract {

    interface View extends BaseRegisterFragmentContract.View {

        void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns);

        FamilyProfileMemberContract.Presenter presenter();
    }

    interface Presenter extends BaseRegisterFragmentContract.Presenter {

        String getMainCondition();

        String getDefaultSortQuery();

    }
}
