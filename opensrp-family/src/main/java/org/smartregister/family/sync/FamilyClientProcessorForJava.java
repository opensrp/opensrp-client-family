package org.smartregister.family.sync;

import android.content.Context;
import android.util.Log;

import org.smartregister.domain.db.Client;
import org.smartregister.domain.db.Event;
import org.smartregister.sync.ClientProcessorForJava;

import java.util.Map;

public class FamilyClientProcessorForJava extends ClientProcessorForJava {

    public FamilyClientProcessorForJava(Context context) {
        super(context);
    }

    public static ClientProcessorForJava getInstance(Context context) {
        if (instance == null) {
            instance = new FamilyClientProcessorForJava(context);
        }
        return instance;
    }

    @Override
    public void updateClientDetailsTable(Event event, Client client) {
        // Do nothing like jon snow
        Log.d(TAG, "Started updateClientDetailsTable");

        event.addDetails(detailsUpdated, Boolean.TRUE.toString());

        Log.d(TAG, "Finished updateClientDetailsTable");
    }
}
