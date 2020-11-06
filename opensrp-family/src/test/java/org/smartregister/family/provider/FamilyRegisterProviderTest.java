package org.smartregister.family.provider;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by samuelgithengi on 5/26/20.
 */
public class FamilyRegisterProviderTest extends BaseUnitTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Context context = RuntimeEnvironment.application;

    @Mock
    private CommonRepository commonRepository;

    private Set visibleColumns = new HashSet<>();

    @Mock
    private View.OnClickListener onClickListener;

    @Mock
    private View.OnClickListener paginationClickListener;

    @Mock
    private Cursor cursor;

    private CommonPersonObjectClient client = TestDataUtils.getCommonPersonObjectClient();

    private FamilyRegisterProvider.RegisterViewHolder viewHolder;

    private FamilyRegisterProvider provider;

    @Before
    public void setUp() {
        org.smartregister.Context.bindtypes = new ArrayList<>();
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        CommonPersonObject commonPersonObject = new CommonPersonObject("1234", "", null, "");
        commonPersonObject.setColumnmaps(new HashMap<String, String>());
        commonPersonObject.getColumnmaps().put(DBConstants.KEY.FIRST_NAME, "Alexia");
        provider = new FamilyRegisterProvider(context, commonRepository, visibleColumns, onClickListener, paginationClickListener);
        View rootView = provider.inflater().inflate(R.layout.family_register_list_row, null);
        viewHolder = new FamilyRegisterProvider.RegisterViewHolder(rootView);
        when(commonRepository.findByBaseEntityId(anyString())).thenReturn(commonPersonObject);
        client.getColumnmaps().put(DBConstants.KEY.VILLAGE_TOWN, "Village A");
        client.getColumnmaps().put(DBConstants.KEY.FAMILY_HEAD, commonPersonObject.getCaseId());

    }

    @Test
    public void testGetView() {
        provider.getView(cursor, client, viewHolder);
        assertEquals("Charity Family", viewHolder.patientName.getText());
        assertEquals("Village A", viewHolder.villageTown.getText());
        assertEquals(View.VISIBLE, viewHolder.dueButton.getVisibility());
        assertEquals("Home Visit", viewHolder.dueButton.getText());
    }

    @Test
    public void testGetViewWithFamilyHeadNameEnabled() {
        provider.familyHeadFirstNameEnabled = true;
        provider.familyMemberRegisterRepository = commonRepository;
        provider.getView(cursor, client, viewHolder);
        assertEquals("Alexia Charity Family", viewHolder.patientName.getText());
        assertEquals("Village A", viewHolder.villageTown.getText());
        assertEquals(View.VISIBLE, viewHolder.dueButton.getVisibility());
        assertEquals("Home Visit", viewHolder.dueButton.getText());
    }


    @Test
    public void testDueButtonOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.dueButton.performClick();
        verify(onClickListener).onClick(viewHolder.dueButton);
    }

    @Test
    public void testPatientColumnOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.patientColumn.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }

    @Test
    public void testRegisterColumnsOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.registerColumns.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }

    @Test
    public void testDueWrapperOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.dueWrapper.performClick();
        verify(onClickListener).onClick(viewHolder.dueButton);
    }

    @Test
    public void testUpdateClients() {
        viewHolder = spy(viewHolder);
        assertNull(provider.updateClients(null, null, null, null));
        verifyZeroInteractions(viewHolder);
    }

    @Test
    public void testOnServiceModeSelected() {
        viewHolder = spy(viewHolder);
        provider.onServiceModeSelected(null);
        verifyZeroInteractions(viewHolder);
    }

    @Test
    public void testNewFormLauncher() {
        viewHolder = spy(viewHolder);
        assertNull(provider.newFormLauncher(null, null, null));
        verifyZeroInteractions(viewHolder);
    }


    @Test
    public void testCreateViewHolder() {
        viewHolder = provider.createViewHolder(null);
        assertNotNull(viewHolder);
        assertNotNull(viewHolder.patientName);
        assertNotNull(viewHolder.villageTown);
    }

    @Test
    public void testCreateFooterHolderIsHidden() {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        FamilyRegisterProvider.FooterViewHolder footer = (FamilyRegisterProvider.FooterViewHolder) provider.createFooterHolder(null);
        assertNotNull(footer);
        assertNotNull(footer.nextPageView);
        assertNotNull(footer.previousPageView);
        assertNotNull(footer.pageInfoView);
    }


    @Test
    public void testCreateFooterHolderIsShown() {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        Whitebox.setInternalState(Utils.metadata().familyActivityRegister, "showPagination", true);
        RecyclerView.ViewHolder footer = provider.createFooterHolder(null);
        assertNotNull(footer);
        assertEquals(View.VISIBLE, footer.itemView.getVisibility());
    }

    @Test
    public void testIsFooterViewHolder() {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        assertTrue(provider.isFooterViewHolder(provider.createFooterHolder(null)));
        assertFalse(provider.isFooterViewHolder(provider.createViewHolder(null)));
    }

    @Test
    public void testGetFooterView(){
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        FamilyRegisterProvider.FooterViewHolder footer = (FamilyRegisterProvider.FooterViewHolder) provider.createFooterHolder(null);
        provider.getFooterView(footer,2,12,true,true);
        assertEquals("Page 2 of 12",footer.pageInfoView.getText());
        assertEquals(View.VISIBLE,footer.nextPageView.getVisibility());
        assertEquals(View.VISIBLE,footer.nextPageView.getVisibility());
        footer.nextPageView.performClick();
        verify(paginationClickListener).onClick(footer.nextPageView);

        footer.previousPageView.performClick();
        verify(paginationClickListener).onClick(footer.previousPageView);

        //test if no previous and next page then controls are not rendered
        provider.getFooterView(footer,2,12,false,false);
        assertEquals(View.INVISIBLE,footer.nextPageView.getVisibility());
        assertEquals(View.INVISIBLE,footer.nextPageView.getVisibility());


    }



}
