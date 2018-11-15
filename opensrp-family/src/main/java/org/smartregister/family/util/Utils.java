package org.smartregister.family.util;

import java.util.ArrayList;

public class Utils extends org.smartregister.util.Utils {
    private static final String TAG = Utils.class.getCanonicalName();
    public static final ArrayList<String> ALLOWED_LEVELS;
    public static final String DEFAULT_LOCATION_LEVEL = "Health Facility";
    public static final String FACILITY = "Dispensary";

    static {
        ALLOWED_LEVELS = new ArrayList<>();
        ALLOWED_LEVELS.add(DEFAULT_LOCATION_LEVEL);
        ALLOWED_LEVELS.add(FACILITY);
    }
}
