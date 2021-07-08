package org.smartregister.family.activity;

import com.vijay.jsonwizard.utils.FormUtils;

public class NoLocaleFamilyWizardFormActivity extends FamilyWizardFormActivity{
    @Override
    protected FormUtils getFormUtils() {
        return FormUtils.newInstanceWithNoLocale();
    }
}
