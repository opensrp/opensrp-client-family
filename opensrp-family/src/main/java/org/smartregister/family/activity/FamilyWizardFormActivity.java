package org.smartregister.family.activity;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.smartregister.family.fragment.FamilyWizardFormFragment;

public class FamilyWizardFormActivity extends JsonWizardFormActivity {

    @Override
    public void initializeFormFragment() {
        initializeFormFragmentCore();
    }

    protected void initializeFormFragmentCore() {
        FamilyWizardFormFragment contactJsonFormFragment = FamilyWizardFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(com.vijay.jsonwizard.R.id.container, contactJsonFormFragment).commit();
    }

}
