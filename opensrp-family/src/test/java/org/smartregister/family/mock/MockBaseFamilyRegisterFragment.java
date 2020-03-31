package org.smartregister.family.mock;

import org.smartregister.family.fragment.BaseFamilyRegisterFragment;

import java.util.HashMap;

/**
 * Created by samuelgithengi on 3/31/20.
 */
public class MockBaseFamilyRegisterFragment extends BaseFamilyRegisterFragment {
    @Override
    protected void initializePresenter() {

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
}
