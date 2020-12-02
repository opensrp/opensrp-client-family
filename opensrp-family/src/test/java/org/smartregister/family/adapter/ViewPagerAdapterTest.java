package org.smartregister.family.adapter;

import android.database.DataSetObserver;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Pair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.family.BaseUnitTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by samuelgithengi on 5/12/20.
 */
public class ViewPagerAdapterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FragmentManager manager;

    @Mock
    private Fragment fragment;

    @Mock
    private DataSetObserver dataSetObserver;

    private ViewPagerAdapter viewPagerAdapter;


    @Before
    public void setUp() {
        viewPagerAdapter = new ViewPagerAdapter(manager);
        viewPagerAdapter.addFragment(fragment, "fragment0");
    }

    @Test
    public void testGetItem() {
        assertEquals(fragment, viewPagerAdapter.getItem(0));
    }


    @Test
    public void testGetCount() {
        assertEquals(1, viewPagerAdapter.getCount());
    }

    @Test
    public void testAddFragment() {
        assertEquals(1, viewPagerAdapter.getCount());
        viewPagerAdapter.addFragment(new Fragment(), "fragment1");
        assertEquals(2, viewPagerAdapter.getCount());
    }

    @Test
    public void testGetPageTitle() {
        assertEquals("fragment0", viewPagerAdapter.getPageTitle(0));
    }

    @Test
    public void testUpdateCount() {
        Pair<Integer, Integer> pair = new Pair<>(10, 12);
        viewPagerAdapter.registerDataSetObserver(dataSetObserver);
        viewPagerAdapter.updateCount(pair);
        assertEquals(pair, Whitebox.getInternalState(viewPagerAdapter, "mPositionCountPair"));
        verify(dataSetObserver).onChanged();
    }

}
