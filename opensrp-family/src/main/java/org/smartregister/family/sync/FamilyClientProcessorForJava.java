package org.smartregister.family.sync;

import android.content.Context;

import org.smartregister.domain.db.Client;
import org.smartregister.domain.db.Event;
import org.smartregister.sync.ClientProcessorForJava;

public class FamilyClientProcessorForJava extends ClientProcessorForJava {

    private static FamilyClientProcessorForJava instance;

    public FamilyClientProcessorForJava(Context context) {
        super(context);
    }

    public static FamilyClientProcessorForJava getInstance(Context context) {
        if (instance == null) {
            instance = new FamilyClientProcessorForJava(context);
        }
        return instance;
    }

    @Override
    public void updateClientDetailsTable(Event event, Client client) {
        // Do nothing like jon snow
    }
}
