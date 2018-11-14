package org.smartgresiter.family.util;

/**
 * Created by keyman on 13/11/2018.
 */
public class DBConstants {

    public static final String FAMILY_TABLE_NAME = "ec_family";
    public static final String FAMILY_MEMBER_TABLE_NAME = "ec_family_member";

    public static final class KEY {
        public static final String ID = "_ID";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String BASE_ENTITY_ID = "base_entity_id";
        public static final String DOB = "dob";//Date Of Birth
        public static final String DOB_UNKNOWN = "dob_unknown";
        public static final String GENDER = "gender";
        public static final String UNIQUE_ID = "unique_id";
        public static final String LAST_INTERACTED_WITH = "last_interacted_with";
        public static final String CONTACT_STATUS = "contact_status";
    }
}
