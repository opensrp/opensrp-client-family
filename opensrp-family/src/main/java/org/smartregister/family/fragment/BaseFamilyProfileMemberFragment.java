package org.smartregister.family.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.R;
import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.contract.FamilyProfileMemberContract;
import org.smartregister.family.provider.FamilyRegisterProvider;
import org.smartregister.family.util.Utils;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Set;

/**
 * Created by keyman on 23/11/2018.
 */
public abstract class BaseFamilyProfileMemberFragment extends BaseRegisterFragment implements FamilyProfileMemberContract.View {

    //public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    //public static final String CLICK_VIEW_STATUS = "click_view_status";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_member, container, false);
        rootView = view;//handle to the root

        setupViews(view);
        return view;
    }

    @Override
    public void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns) {
        FamilyRegisterProvider familyRegisterProvider = new FamilyRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, familyRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    protected String getMainCondition() {
        return presenter().getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter().getDefaultSortQuery();
    }

    @Override
    protected void startRegistration() {
        ((BaseFamilyProfileActivity) getActivity()).startFormActivity(Utils.metadata().familyMemberRegister.formName, null, null);
    }

    @Override
    protected void onViewClicked(View view) {
        if (getActivity() == null) {
            return;
        }
    }

    @Override
    public void setUniqueID(String s) {
        if (getSearchView() != null) {
            getSearchView().setText(s);
        }
    }

    @Override
    public void showNotFoundPopup(String uniqueId) {
        if (getActivity() == null) {
            return;
        }
        NoMatchDialogFragment.launchDialog((BaseRegisterActivity) getActivity(), DIALOG_TAG, uniqueId);
    }

    @Override
    public FamilyProfileMemberContract.Presenter presenter() {
        return (FamilyProfileMemberContract.Presenter) presenter;
    }
}
