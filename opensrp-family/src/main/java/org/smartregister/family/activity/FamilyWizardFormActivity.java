package org.smartregister.family.activity;

import android.support.v4.app.Fragment;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.smartregister.family.fragment.FamilyWizardFormFragment;

import java.util.List;

public class FamilyWizardFormActivity extends JsonWizardFormActivity {

    @Override
    public void initializeFormFragment() {
        initializeFormFragmentCore();
    }

    protected void initializeFormFragmentCore() {
        FamilyWizardFormFragment familyWizardFormFragment = FamilyWizardFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(com.vijay.jsonwizard.R.id.container, familyWizardFormFragment).commit();
    }

    @Override
    public void writeValue(String stepName, String parentKey, String childObjectKey, String childKey, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId, boolean popup) throws JSONException {
        super.writeValue(stepName, parentKey, childObjectKey, childKey, value, openMrsEntityParent, openMrsEntity, openMrsEntityId, popup);
        validateActivateNext();
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId, boolean popup) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId, popup);
        validateActivateNext();
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        validateActivateNext();
    }

    @Override
    public void writeValue(String stepName, String parentKey, String childObjectKey, String childKey, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, parentKey, childObjectKey, childKey, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        validateActivateNext();
    }

    public void validateActivateNext() {
        Fragment fragment = getVisibleFragment();
        if (fragment != null && fragment instanceof FamilyWizardFormFragment) {
            ((FamilyWizardFormFragment) fragment).validateActivateNext();
        }
    }

    public Fragment getVisibleFragment() {
        List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}

