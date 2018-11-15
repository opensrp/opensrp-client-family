package org.smartregister.family.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.smartregister.family.FamilyLibrary;
import org.smartregister.domain.UniqueId;
import org.smartregister.family.sample.R;
import org.smartregister.family.activity.FamilyRegisterActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleUniqueIds();

        startActivity(new Intent(this, FamilyRegisterActivity.class));

        finish();
    }


    private void sampleUniqueIds() {
        List<String> ids = generateIds(20);
        FamilyLibrary.getInstance().getUniqueIdRepository().bulkInserOpenmrsIds(ids);
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
