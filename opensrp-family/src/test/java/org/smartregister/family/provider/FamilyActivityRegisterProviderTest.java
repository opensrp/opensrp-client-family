package org.smartregister.family.provider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.provider.FamilyActivityRegisterProvider.FooterViewHolder;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by samuelgithengi on 5/12/20.
 */
public class FamilyActivityRegisterProviderTest extends BaseUnitTest {

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

    private FamilyActivityRegisterProvider.RegisterViewHolder viewHolder;

    private CommonPersonObjectClient client = TestDataUtils.getCommonPersonObjectClient();


    private FamilyActivityRegisterProvider familyActivityRegisterProvider;

    private View rootView;

    private String ageString;

    @Before
    public void setUp() {
        familyActivityRegisterProvider = new FamilyActivityRegisterProvider(context, commonRepository, visibleColumns, onClickListener, paginationClickListener);
        rootView = familyActivityRegisterProvider.inflater().inflate(R.layout.family_activity_register_list_row, null);
        viewHolder = new FamilyActivityRegisterProvider.RegisterViewHolder(rootView);

        String dob = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.DOB, false);
        String dobString = Utils.getDuration(dob);
        ageString = dobString.contains("y") ? dobString.substring(0, dobString.indexOf("y")) : dobString;
    }

    @Test
    public void testPopulatePatientColumn() {
        LinearLayout patientColumn = rootView.findViewById(R.id.patient_column);
        TextView textView = new TextView(context);
        textView.setId(R.id.gender);
        patientColumn.addView(textView);
        viewHolder = new FamilyActivityRegisterProvider.RegisterViewHolder(rootView);
        familyActivityRegisterProvider.getView(cursor, client, viewHolder);
        assertEquals("Charity Otala, " + ageString, viewHolder.patientNameAge.getText());
        assertEquals(Color.BLACK, viewHolder.patientNameAge.getCurrentTextColor());
        assertEquals("Female", viewHolder.gender.getText());
        assertEquals("Completed on 10 July 2018", viewHolder.lastVisit.getText());
    }


    @Test
    public void testPopulatePatientColumnWithDod() {
        String dod = "2019-03-05T00:00:00.000+03:00";
        client.getColumnmaps().put(DBConstants.KEY.DOD, dod);

        String dobString = Utils.getDuration(dod, client.getColumnmaps().get(DBConstants.KEY.DOB));
        dobString = dobString.contains("y") ? dobString.substring(0, dobString.indexOf("y")) : dobString;

        familyActivityRegisterProvider.getView(cursor, client, viewHolder);
        assertEquals(String.format("Charity Otala, %s (deceased)", dobString), viewHolder.patientNameAge.getText());
        assertEquals(Color.GRAY, viewHolder.patientNameAge.getCurrentTextColor());
        assertEquals("Completed on 10 July 2018", viewHolder.lastVisit.getText());
    }

    @Test
    public void testStatusOnClick() {
        familyActivityRegisterProvider.getView(cursor, client, viewHolder);
        viewHolder.status.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }

    @Test
    public void testPatientColumnOnClick() {
        familyActivityRegisterProvider.getView(cursor, client, viewHolder);
        viewHolder.patientColumn.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }

    @Test
    public void testAttachClickListener() {
        familyActivityRegisterProvider.getView(cursor, client, viewHolder);
        assertEquals(client, viewHolder.patientColumn.getTag());
        assertEquals(BaseFamilyProfileMemberFragment.CLICK_VIEW_NORMAL, viewHolder.patientColumn.getTag(R.id.VIEW_ID));
    }


    @Test
    public void testRegisterColumnOnClick() {
        familyActivityRegisterProvider.getView(cursor, client, viewHolder);
        viewHolder.registerColumns.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }

    @Test
    public void testUpdateClients() {
        viewHolder = spy(viewHolder);
        assertNull(familyActivityRegisterProvider.updateClients(null, null, null, null));
        verifyZeroInteractions(viewHolder);
    }

    @Test
    public void testOnServiceModeSelected() {
        viewHolder = spy(viewHolder);
        familyActivityRegisterProvider.onServiceModeSelected(null);
        verifyZeroInteractions(viewHolder);
    }

    @Test
    public void testNewFormLauncher() {
        viewHolder = spy(viewHolder);
        assertNull(familyActivityRegisterProvider.newFormLauncher(null, null, null));
        verifyZeroInteractions(viewHolder);
    }

    @Test
    public void testCreateViewHolder() {
        viewHolder = familyActivityRegisterProvider.createViewHolder(null);
        assertNotNull(viewHolder);
        assertNotNull(viewHolder.patientNameAge);
    }

    @Test
    public void testCreateFooterHolderIsHidden() {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        FooterViewHolder footer = (FooterViewHolder) familyActivityRegisterProvider.createFooterHolder(null);
        assertNotNull(footer);
        assertNotNull(footer.nextPageView);
        assertNotNull(footer.previousPageView);
        assertNotNull(footer.pageInfoView);
        assertEquals(View.GONE, footer.itemView.getVisibility());
    }


    @Test
    public void testCreateFooterHolderIsShown() {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        Whitebox.setInternalState(Utils.metadata().familyActivityRegister, "showPagination", true);
        RecyclerView.ViewHolder footer = familyActivityRegisterProvider.createFooterHolder(null);
        assertNotNull(footer);
        assertEquals(View.VISIBLE, footer.itemView.getVisibility());
    }

    @Test
    public void testIsFooterViewHolder() {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        assertTrue(familyActivityRegisterProvider.isFooterViewHolder(familyActivityRegisterProvider.createFooterHolder(null)));
        assertFalse(familyActivityRegisterProvider.isFooterViewHolder(familyActivityRegisterProvider.createViewHolder(null)));
    }

    @Test
    public void testGetFooterView(){
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        FooterViewHolder footer = (FooterViewHolder) familyActivityRegisterProvider.createFooterHolder(null);
        familyActivityRegisterProvider.getFooterView(footer,2,12,true,true);
        assertEquals("Page 2 of 12",footer.pageInfoView.getText());
        assertEquals(View.VISIBLE,footer.nextPageView.getVisibility());
        assertEquals(View.VISIBLE,footer.nextPageView.getVisibility());
        footer.nextPageView.performClick();
        verify(paginationClickListener).onClick(footer.nextPageView);

        footer.previousPageView.performClick();
        verify(paginationClickListener).onClick(footer.previousPageView);

        //test if no previous and next page then controls are not rendered
        familyActivityRegisterProvider.getFooterView(footer,2,12,false,false);
        assertEquals(View.INVISIBLE,footer.nextPageView.getVisibility());
        assertEquals(View.INVISIBLE,footer.nextPageView.getVisibility());


    }
}
