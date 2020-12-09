package org.smartregister.family.dao;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.repository.Repository;

@RunWith(MockitoJUnitRunner.class)
public class ChildDaoTest extends ChildDao {
    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setRepository(repository);
    }

    @Test
    public void testIsPhysicallyChallenged() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        Boolean isPhysicallyChallenged = ChildDao.isPhysicallyChallenged("1432345");
        Assert.assertEquals(isPhysicallyChallenged, false);
    }
}
