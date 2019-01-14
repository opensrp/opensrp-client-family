package org.smartregister.family.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.R;
import org.smartregister.family.contract.FamilyProfileDueContract;
import org.smartregister.family.provider.FamilyDueRegisterProvider;
import org.smartregister.family.provider.FamilyMemberRegisterProvider;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Set;

/**
 * Created by ndegwamartin on 12/07/2018.
 */
public abstract class BaseFamilyProfileDueFragment extends BaseRegisterFragment implements FamilyProfileDueContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_due, container, false);
        rootView = view;//handle to the root

        setupViews(view);
        return view;
    }


    @Override
    public void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns, String familyHead, String primaryCaregiver) {
        FamilyDueRegisterProvider familyDueRegisterProvider = new FamilyDueRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler, familyHead, primaryCaregiver);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, familyDueRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    protected void onViewClicked(View view) {
        if (getActivity() == null) {
            return;
        }
    }
}
