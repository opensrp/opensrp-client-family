package org.smartregister.family.util;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.Context;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

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

    public static int getProfileImageTwoResourceIDentifier() {
        return R.mipmap.ic_family;
    }

    public static int getMemberProfileImageResourceIDentifier() {
        return R.mipmap.ic_member;
    }

    public static int getActivityProfileImageResourceIDentifier() {
        return R.mipmap.ic_activity_visited;
    }

    public static int getDueProfileImageResourceIDentifier() {
        return R.color.due_profile_blue;
    }

    public static int getChildProfileImageResourceIDentifier() {
        return R.mipmap.ic_child;
    }

    public static String getDuration(String date1, String date2) {
        try {
            DateTime dateTime1 = toDateTime(date1);
            DateTime dateTime2 = toDateTime(date2);

            if (dateTime1 == null || dateTime2 == null) {
                return "";
            }

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateTime1.toDate());
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.set(Calendar.MILLISECOND, 0);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(dateTime2.toDate());
            calendar2.set(Calendar.HOUR_OF_DAY, 0);
            calendar2.set(Calendar.MINUTE, 0);
            calendar2.set(Calendar.SECOND, 0);
            calendar2.set(Calendar.MILLISECOND, 0);


            long timeDiff = Math.abs(calendar1.getTimeInMillis() - calendar2.getTimeInMillis());
            return DateUtil.getDuration(timeDiff);

        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
        return "";
    }

    public static DateTime toDateTime(String date) {
        if (StringUtils.isNotBlank(date)) {
            try {
                return new DateTime(date);
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
        }
        return null;
    }

    public static String getName(String firstName, String middleName, String lastName) {
        firstName = firstName.trim();
        middleName = middleName.trim();
        lastName = lastName.trim();
        if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
            if (StringUtils.isNotBlank(middleName)) {
                return firstName + " " + middleName + " " + lastName;
            }
            return firstName + " " + lastName;

        } else {
            if (StringUtils.isNotBlank(firstName)) {
                if (StringUtils.isNotBlank(middleName)) {
                    return firstName + " " + middleName;
                }
                return firstName;

            } else if (StringUtils.isNotBlank(lastName)) {
                if (StringUtils.isNotBlank(middleName)) {
                    return middleName + " " + lastName;
                }
                return lastName;
            }
        }

        return "";
    }

    public static Context context() {
        return FamilyLibrary.getInstance().context();
    }

    public static FamilyMetadata metadata() {
        return FamilyLibrary.getInstance().metadata();
    }
}
