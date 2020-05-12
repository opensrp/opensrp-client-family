package org.smartregister.family.adapter;

import net.sqlcipher.Cursor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.family.BaseUnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 5/12/20.
 */
public class FamilyRecyclerViewCustomAdapterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private FamilyRecyclerViewCustomAdapter adapter;

    @Mock
    private Cursor cursor;

    @Before
    public void setUp() {
        when(cursor.getCount()).thenReturn(2);
        when(cursor.getColumnIndex("_id")).thenReturn(1);
        adapter = new FamilyRecyclerViewCustomAdapter(cursor, null, null, true);
    }

    @Test
    public void testIsFooterWithPaginationEnabled() {
        assertFalse(adapter.isFooter(1));
        assertTrue(adapter.isFooter(2));
    }

    @Test
    public void testIsFooterWithPaginationDisabled() {
        Whitebox.setInternalState(adapter, "showPagination", false);
        assertFalse(adapter.isFooter(1));
        assertFalse(adapter.isFooter(2));
    }

    @Test
    public void testGetItemCountWithPaginationEnabled() {
        assertEquals(3, adapter.getItemCount());
    }

    @Test
    public void testGetItemCountWithPaginationDisabled() {
        Whitebox.setInternalState(adapter, "showPagination", false);
        assertEquals(2, adapter.getItemCount());
    }
}
