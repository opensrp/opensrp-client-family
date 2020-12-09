package org.smartregister.family.dao;

import android.database.Cursor;

import org.smartregister.dao.AbstractDao;

import java.util.List;

/**
 * Created by Qazi Abubakar
 */
public class ChildDao extends AbstractDao {

    public static boolean isPhysicallyChallenged(String baseEntityId) {
        String sql = "select count(*) count FROM ec_child\n" +
                "    where base_entity_id = '" + baseEntityId + "'" +
                "    and physically_challenged = 'Yes'";

        AbstractDao.DataMap<Integer> dataMap = new DataMap<Integer>() {
            @Override
            public Integer readCursor(Cursor cursor) {
                return getCursorIntValue(cursor, "count");
            }
        };

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() == 0 || res.get(0) == 0)
            return false;

        res.size();
        return true;
    }
}
