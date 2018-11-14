package org.smartgresiter.family.presenter;

import org.apache.commons.lang3.StringUtils;
import org.smartgresiter.family.contract.FamilyRegisterFragmentContract;
import org.smartgresiter.family.model.FamilyRegisterFramentModel;
import org.smartgresiter.family.util.DBConstants;
import org.smartregister.configurableviews.model.Field;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.ViewConfiguration;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FamilyRegisterFragmentPresenter implements FamilyRegisterFragmentContract.Presenter {

    private WeakReference<FamilyRegisterFragmentContract.View> viewReference;

    private FamilyRegisterFragmentContract.Model model;

    private RegisterConfiguration config;

    protected Set<org.smartregister.configurableviews.model.View> visibleColumns = new TreeSet<>();
    private String viewConfigurationIdentifier;

    public FamilyRegisterFragmentPresenter(FamilyRegisterFragmentContract.View view, String viewConfigurationIdentifier) {
        this.viewReference = new WeakReference<>(view);
        this.model = new FamilyRegisterFramentModel();
        this.viewConfigurationIdentifier = viewConfigurationIdentifier;
        this.config = model.defaultRegisterConfiguration();

    }

    @Override
    public void processViewConfigurations() {
        if (StringUtils.isBlank(viewConfigurationIdentifier)) {
            return;
        }

        ViewConfiguration viewConfiguration = model.getViewConfiguration(viewConfigurationIdentifier);
        if (viewConfiguration != null) {
            config = (RegisterConfiguration) viewConfiguration.getMetadata();
            setVisibleColumns(model.getRegisterActiveColumns(viewConfigurationIdentifier));
        }

        if (config.getSearchBarText() != null && getView() != null) {
            getView().updateSearchBarHint(config.getSearchBarText());
        }
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = DBConstants.FAMILY_TABLE_NAME;

        String countSelect = model.countSelect(tableName, mainCondition);
        String mainSelect = model.mainSelect(tableName, mainCondition);

        getView().initializeQueryParams(tableName, countSelect, mainSelect);
        getView().initializeAdapter(visibleColumns);

        getView().countExecute();
        getView().filterandSortInInitializeQueries();
    }

    @Override
    public void startSync() {
        //ServiceTools.startSyncService(getActivity());
    }

    @Override
    public void updateSortAndFilter(List<Field> filterList, Field sortField) {
        String filterText = model.getFilterText(filterList, getView().getString(org.smartregister.R.string.filter));
        String sortText = model.getSortText(sortField);

        getView().updateFilterAndFilterStatus(filterText, sortText);
    }

    @Override
    public void searchGlobally(String ancId) {
    }

    protected FamilyRegisterFragmentContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    private void setVisibleColumns(Set<org.smartregister.configurableviews.model.View> visibleColumns) {
        this.visibleColumns = visibleColumns;
    }

    public void setModel(FamilyRegisterFragmentContract.Model model) {
        this.model = model;
    }


}