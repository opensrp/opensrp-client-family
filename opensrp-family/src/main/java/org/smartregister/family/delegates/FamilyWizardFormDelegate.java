package org.smartregister.family.delegates;


import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Consumer;

import com.vijay.jsonwizard.activities.JsonFormActivity;

import org.json.JSONObject;
import org.smartregister.family.R;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.util.LangUtils;

import timber.log.Timber;

public class FamilyWizardFormDelegate<T extends JsonFormActivity> {
    private final boolean enableOnCloseDialog;

    public FamilyWizardFormDelegate(boolean enableOnCloseDialog) {
        this.enableOnCloseDialog = enableOnCloseDialog;
    }

    public void onResume(@NonNull  T activity){
        activity.setConfirmCloseTitle(activity.getString(R.string.confirm_form_close));
        activity.setConfirmCloseMessage(activity.getString(R.string.confirm_form_close_explanation));

        try {
            JSONObject form = new JSONObject(activity.currentJsonState());
            String et = form.getString(JsonFormUtils.ENCOUNTER_TYPE);
            if (et.trim().toLowerCase().contains("update")) {
                activity.setConfirmCloseMessage(activity.getString(R.string.any_changes_you_make));
            }
        } catch (Exception e) {
            Timber.e(e.toString());
        }
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar){
        if (toolbar != null){
            toolbar.setContentInsetStartWithNavigation(0);
        }
    }

    public void onBackPressed(T activity){
        if (this.enableOnCloseDialog){
            activity.onBackPressed();
        }else {
            activity.finish();
        }
    }

    public void attachBaseContext(T activity, Context baseContext, Consumer<Context> superOnAttachBaseContext){
        // get language from prefs
        String lang = LangUtils.getLanguage(baseContext.getApplicationContext());
        Configuration newConfiguration = LangUtils.setAppLocale(baseContext, lang);

        superOnAttachBaseContext.accept(baseContext);

        activity.applyOverrideConfiguration(newConfiguration);
    }

}
