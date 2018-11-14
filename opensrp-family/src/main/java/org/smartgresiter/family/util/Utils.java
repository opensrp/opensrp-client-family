package org.smartgresiter.family.util;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartgresiter.family.FamilyLibrary;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.DateUtil;

import java.util.ArrayList;

public class Utils {
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
