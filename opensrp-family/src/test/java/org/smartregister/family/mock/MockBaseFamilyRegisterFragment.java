package org.smartregister.family.mock;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.fragment.BaseFamilyRegisterFragment;

import java.util.HashMap;

import static org.mockito.Mockito.mock;

/**
 * Created by samuelgithengi on 3/31/20.
 */
public class MockBaseFamilyRegisterFragment extends BaseFamilyRegisterFragment {
    @Override
    protected void initializePresenter() {//do nothing
        clientAdapter= mock(RecyclerViewPaginatedAdapter.class);
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> advancedSearchFormData) {//do nothing
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
