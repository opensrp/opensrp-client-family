package org.smartregister.family;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;

import org.robolectric.Robolectric;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.activity.FamilyWizardFormActivity;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

import timber.log.Timber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestApplication extends DrishtiApplication {

    private FamilyMetadata metadata;

    @Override
    public void onCreate() {
        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        SyncConfiguration syncConfig = mock(SyncConfiguration.class);
        when(syncConfig.getEncryptionParam()).thenReturn(SyncFilter.LOCATION_ID);
        CoreLibrary.init(context, syncConfig);
        ConfigurableViewsLibrary.init(context);

        FamilyLibrary.init(context, getMetadata(), BuildConfig.VERSION_CODE, 2);

        setTheme(R.style.FamilyTheme_NoActionBar); //or just R.style.Theme_AppCompat
    }

    public FamilyMetadata getMetadata() {

        if (metadata != null) {
            return metadata;
        }

        metadata = new FamilyMetadata(FamilyWizardFormActivity.class, JsonWizardFormActivity.class, BaseFamilyProfileActivity.class, "opensrp_id", true);

        return metadata;
    }


    @Override
    public void logoutCurrentUser() {
        Timber.v("Logout");
    }

    @Override
    public Repository getRepository() {
        repository = mock(Repository.class);
        return repository;
    }

    @Override
    public void onTerminate() {
        Robolectric.flushBackgroundThreadScheduler();
    }
}