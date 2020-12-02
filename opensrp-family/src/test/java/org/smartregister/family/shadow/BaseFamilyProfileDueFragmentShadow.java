package org.smartregister.family.shadow;

import org.smartregister.family.fragment.BaseFamilyProfileDueFragment;

import java.util.HashMap;

/**
 * Created by samuelgithengi on 12/1/20.
 */
public class BaseFamilyProfileDueFragmentShadow  extends BaseFamilyProfileDueFragment {
    @Override
    protected void initializePresenter() {//do nothing
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {//mock class
    }

    @Override
    public void setTotalPatients() {
        //do nothing
    }
}
