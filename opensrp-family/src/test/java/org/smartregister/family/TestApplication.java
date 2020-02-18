package org.smartregister.family;

import android.database.sqlite.SQLiteDatabase;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.configurableviews.ConfigurableViewsLibrary;
import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.activity.FamilyWizardFormActivity;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.mock;

public class TestApplication extends DrishtiApplication {

    private FamilyMetadata metadata;

    @Override
    public void onCreate() {
        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        CoreLibrary.init(context);
        ConfigurableViewsLibrary.init(context);

        FamilyLibrary.init(context, getMetadata(), BuildConfig.VERSION_CODE, 2);

        setTheme(R.style.Theme_AppCompat); //or just R.style.Theme_AppCompat
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
    }

    @Override
    public Repository getRepository() {
        repository = mock(Repository.class);
        SQLiteDatabase sqLiteDatabase = mock(SQLiteDatabase.class);
        //when(repository.getWritableDatabase()).thenReturn(sqLiteDatabase);
        //when(repository.getReadableDatabase()).thenReturn(sqLiteDatabase);

        return repository;
    }

    private void sampleUniqueIds() {
        List<String> ids = generateIds(20);
        FamilyLibrary.getInstance().getUniqueIdRepository().bulkInsertOpenmrsIds(ids);
    }

    private List<String> generateIds(int size) {
        List<String> ids = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < size; i++) {
            Integer randomInt = r.nextInt(1000) + 1;
            ids.add(randomInt.toString());
        }

        return ids;
    }

}