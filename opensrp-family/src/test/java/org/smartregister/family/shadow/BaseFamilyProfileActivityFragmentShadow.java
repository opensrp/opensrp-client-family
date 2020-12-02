package org.smartregister.family.shadow;

import org.smartregister.family.fragment.BaseFamilyProfileActivityFragment;

import java.util.HashMap;

/**
 * Created by samuelgithengi on 12/1/20.
 */
public class BaseFamilyProfileActivityFragmentShadow extends BaseFamilyProfileActivityFragment {
    @Override
    protected void initializePresenter() {//mock method
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {//mock method
    }

    @Override
    public void setTotalPatients() {
        //do nothing
    }
}
