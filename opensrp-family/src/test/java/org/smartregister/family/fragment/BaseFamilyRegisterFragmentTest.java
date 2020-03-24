package org.smartregister.family.fragment;



import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.configurableviews.model.View;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.BaseUnitTest;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 3/24/20.
 */
public class BaseFamilyRegisterFragmentTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Context context;

    @Mock
    private CommonRepository commonRepository;

    @Mock
    public RecyclerView clientsView;

    @Captor
    private ArgumentCaptor<RecyclerViewPaginatedAdapter> adapterArgumentCaptor;

    private BaseFamilyRegisterFragment registerFragment;

    private AppCompatActivity activity;

    @Before
    public void setUp(){
        registerFragment = Mockito.mock(BaseFamilyRegisterFragment.class, Mockito.CALLS_REAL_METHODS);
        CoreLibrary.init(context);
        when(context.commonrepository(anyString())).thenReturn(commonRepository);
        activity= Robolectric.buildActivity(AppCompatActivity.class).create().get();
        when(registerFragment.getActivity()).thenReturn(activity);
        Context.bindtypes= new ArrayList<>();
        Whitebox.setInternalState(registerFragment,"clientsView",clientsView);
    }

    @Test
    public void testInitializeAdapter(){
        registerFragment.initializeAdapter(new HashSet<View>());
        verify(clientsView).setAdapter(adapterArgumentCaptor.capture());
        assertNotNull(adapterArgumentCaptor.getValue());
        assertEquals(20,adapterArgumentCaptor.getValue().currentlimit);

    }
}
