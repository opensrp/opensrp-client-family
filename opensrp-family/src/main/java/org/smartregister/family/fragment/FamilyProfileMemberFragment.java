package org.smartregister.family.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.R;
import org.smartregister.family.activity.FamilyProfileActivity;
import org.smartregister.family.contract.FamilyProfileMemberContract;
import org.smartregister.family.presenter.FamilyProfileMemberPresenter;
import org.smartregister.family.provider.FamilyRegisterProvider;
import org.smartregister.family.util.Constants;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Set;

/**
 * Created by keyman on 23/11/2018.
 */
public class FamilyProfileMemberFragment extends BaseRegisterFragment implements FamilyProfileMemberContract.View {

    public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    public static final String CLICK_VIEW_DOSAGE_STATUS = "click_view_dosage_status";

    public static FamilyProfileMemberFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        FamilyProfileMemberFragment fragment = new FamilyProfileMemberFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_member, container, false);
        rootView = view;//handle to the root

        setupViews(view);
        return view;
    }

    @Override
    protected void initializePresenter() {
        String baseEntityId = getArguments().getString(Constants.INTENT_KEY.BASE_ENTITY_ID);
        presenter = new FamilyProfileMemberPresenter(this, null, baseEntityId);
    }

    @Override
    public void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns) {
        FamilyRegisterProvider familyRegisterProvider = new FamilyRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, familyRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    public void setUniqueID(String s) {
    }

    @Override
    protected String getMainCondition() {
        return ((FamilyProfileMemberPresenter) presenter).getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return ((FamilyProfileMemberPresenter) presenter).getDefaultSortQuery();
    }

    @Override
    protected void startRegistration() {
        ((FamilyProfileActivity) getActivity()).startFormActivity(Constants.JSON_FORM.FAMILY_MEMBER_REGISTER, null, null);
    }

    @Override
    protected void onViewClicked(View view) {
        if (getActivity() == null) {
            return;
        }
    }

    @Override
    public void showNotFoundPopup(String s) {
        // TODO add implementation
    }

}
