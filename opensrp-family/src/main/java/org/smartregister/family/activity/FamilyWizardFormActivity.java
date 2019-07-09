package org.smartregister.family.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;

import org.json.JSONObject;
import org.smartregister.family.R;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.util.LangUtils;

public class FamilyWizardFormActivity extends JsonWizardFormActivity {
    private String TAG = FamilyWizardFormActivity.class.getCanonicalName();

    private boolean enableOnCloseDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableOnCloseDialog = getIntent().getBooleanExtra(Constants.WizardFormActivity.EnableOnCloseDialog, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setConfirmCloseTitle(getString(R.string.confirm_form_close));
        setConfirmCloseMessage(getString(R.string.confirm_form_close_explanation));

        try {
            JSONObject form = new JSONObject(currentJsonState());
            String et = form.getString(JsonFormUtils.ENCOUNTER_TYPE);
            if (et.trim().toLowerCase().contains("update")) {
                setConfirmCloseMessage(getString(R.string.any_changes_you_make));
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setContentInsetStartWithNavigation(0);
        }
        super.setSupportActionBar(toolbar);
    }

    /**
     * Conditionaly display the confirmation dialog
     */
    @Override
    public void onBackPressed() {
        if (enableOnCloseDialog) {
            super.onBackPressed();
        } else {
            FamilyWizardFormActivity.this.finish();
        }
    }

    @Override
    protected void attachBaseContext(android.content.Context base) {
        // get language from prefs
        String lang = LangUtils.getLanguage(base.getApplicationContext());
        super.attachBaseContext(LangUtils.setAppLocale(base, lang));
    }
}

