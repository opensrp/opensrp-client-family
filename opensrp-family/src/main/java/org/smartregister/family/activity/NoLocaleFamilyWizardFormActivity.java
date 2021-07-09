package org.smartregister.family.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.vijay.jsonwizard.activities.NoLocaleFormConfigurationJsonWizardFormActivity;

import org.smartregister.family.delegates.FamilyWizardFormDelegate;
import org.smartregister.family.util.Constants;

public class NoLocaleFamilyWizardFormActivity extends NoLocaleFormConfigurationJsonWizardFormActivity {

    private FamilyWizardFormDelegate<NoLocaleFamilyWizardFormActivity> delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean enableOnCloseDialog = getIntent()
                .getBooleanExtra(Constants.WizardFormActivity.EnableOnCloseDialog, true);
        delegate = new FamilyWizardFormDelegate<>(enableOnCloseDialog);
    }

    @Override
    protected void onResume() {
        super.onResume();
        delegate.onResume(this);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        delegate.setSupportActionBar(toolbar);
        super.setSupportActionBar(toolbar);
    }

    /**
     * Conditionaly display the confirmation dialog
     */
    @Override
    public void onBackPressed() {
        delegate.onBackPressed(this);
    }

    @Override
    protected void attachBaseContext(android.content.Context base) {
        // get language from prefs
        delegate.attachBaseContext(this, base, context -> NoLocaleFamilyWizardFormActivity.super.attachBaseContext(context));
    }

}
