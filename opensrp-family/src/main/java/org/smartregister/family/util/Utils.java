package org.smartregister.family.util;

import org.smartregister.Context;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.domain.FamilyMetadata;

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

    public static int getProfileImageResourceIDentifier() {
        return R.mipmap.ic_family_white;
    }

    public static Context context() {
        return FamilyLibrary.getInstance().context();
    }

    public static FamilyMetadata metadata() {
        return FamilyLibrary.getInstance().metadata();
    }
}
