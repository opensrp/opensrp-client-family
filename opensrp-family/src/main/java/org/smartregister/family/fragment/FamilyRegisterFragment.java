package org.smartregister.family.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.R;
import org.smartregister.family.activity.FamilyProfileActivity;
import org.smartregister.family.activity.FamilyRegisterActivity;
import org.smartregister.family.contract.FamilyRegisterFragmentContract;
import org.smartregister.family.presenter.FamilyRegisterFragmentPresenter;
import org.smartregister.family.provider.FamilyRegisterProvider;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Set;

public class FamilyRegisterFragment extends BaseRegisterFragment implements FamilyRegisterFragmentContract.View {
    private static final String TAG = FamilyRegisterFragment.class.getCanonicalName();
    public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    public static final String CLICK_VIEW_DOSAGE_STATUS = "click_view_dosage_status";

    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }

        String viewConfigurationIdentifier = ((BaseRegisterActivity) getActivity()).getViewIdentifiers().get(0);
        presenter = new FamilyRegisterFragmentPresenter(this, viewConfigurationIdentifier);
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
        return "";
    }

    @Override
    protected String getDefaultSortQuery() {
        return DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ";
    }

    @Override
    protected void startRegistration() {
        ((FamilyRegisterActivity) getActivity()).startFormActivity(Constants.JSON_FORM.FAMILY_REGISTER, null, null);
    }


    @Override
    public void showNotFoundPopup(String uniqueId) {
        if (getActivity() == null) {
            return;
        }
        NoMatchDialogFragment.launchDialog((BaseRegisterActivity) getActivity(), DIALOG_TAG, uniqueId);
    }

    @Override
    protected void onViewClicked(View view) {

        if (getActivity() == null) {
            return;
        }

        FamilyRegisterActivity familyRegisterActivity = (FamilyRegisterActivity) getActivity();

        if (view.getTag() != null && view.getTag(R.id.VIEW_ID) == CLICK_VIEW_NORMAL) {
            goToPatientDetailActivity((CommonPersonObjectClient) view.getTag(), false);
        } else if (view.getTag() != null && view.getTag(R.id.VIEW_ID) == CLICK_VIEW_DOSAGE_STATUS) {
            CommonPersonObjectClient pc = (CommonPersonObjectClient) view.getTag();
            String baseEntityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.BASE_ENTITY_ID, true);

            if (StringUtils.isNotBlank(baseEntityId)) {
                //proceedToContact(baseEntityId);
            }
        }
    }

    private void goToPatientDetailActivity(CommonPersonObjectClient patient,
                                           boolean launchDialog) {
        if (launchDialog) {
            Log.i(FamilyRegisterFragment.TAG, patient.name);
        }

        Intent intent = new Intent(getActivity(), FamilyProfileActivity.class);
        intent.putExtra(Constants.INTENT_KEY.BASE_ENTITY_ID, patient.getCaseId());
        startActivity(intent);
    }

}
