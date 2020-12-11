package org.smartregister.family.provider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;

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
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.helper.ImageRenderHelper;

import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by samuelgithengi on 6/9/20.
 */
public class FamilyMemberRegisterProviderTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Context context = RuntimeEnvironment.application;

    @Mock
    private CommonRepository commonRepository;

    private Set<String> visibleColumns = new HashSet<>();

    @Mock
    private View.OnClickListener onClickListener;

    @Mock
    private View.OnClickListener paginationClickListener;

    @Mock
    private Cursor cursor;

    @Mock
    private ImageRenderHelper imageRenderHelper;

    private CommonPersonObjectClient client = TestDataUtils.getCommonPersonObjectClient();

    private FamilyMemberRegisterProvider.RegisterViewHolder viewHolder;

    private FamilyMemberRegisterProvider provider;

    private String ageString;

    @Before
    public void setUp() {
        provider = new FamilyMemberRegisterProvider(context, commonRepository, visibleColumns, onClickListener, paginationClickListener, "123", "124");
        Whitebox.setInternalState(provider, "imageRenderHelper", imageRenderHelper);
        View rootView = provider.inflater().inflate(R.layout.family_member_register_list_row, null);
        viewHolder = new FamilyMemberRegisterProvider.RegisterViewHolder(rootView);
        String dob = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.DOB, false);
        String dobString = Utils.getDuration(dob);
        ageString = dobString.contains("y") ? dobString.substring(0, dobString.indexOf("y")) : dobString;
    }

    @Test
    public void testPopulatePatientColumn() {
        provider.getView(cursor, client, viewHolder);
        assertEquals("Charity Otala, " + ageString, viewHolder.patientNameAge.getText());
        assertEquals("Female", viewHolder.gender.getText());
        assertEquals(Color.BLACK, viewHolder.patientNameAge.getCurrentTextColor());
        assertEquals(View.VISIBLE, viewHolder.nextArrow.getVisibility());
        verify(imageRenderHelper).refreshProfileImage(eq(client.getCaseId()),any(CircleImageView.class),eq(R.mipmap.ic_member));
    }


    @Test
    public void testPopulatePatientColumnDeceased() {
        String dod = "2019-03-05T00:00:00.000+03:00";
        client.getColumnmaps().put(DBConstants.KEY.DOD, dod);

        String dobString = Utils.getDuration(dod, client.getColumnmaps().get(DBConstants.KEY.DOB));
        dobString = dobString.contains("y") ? dobString.substring(0, dobString.indexOf("y")) : dobString;

        provider.getView(cursor, client, viewHolder);

        assertEquals(String.format("Charity Otala, %s (deceased)" ,dobString), viewHolder.patientNameAge.getText());
        assertEquals("Female", viewHolder.gender.getText());
        assertEquals(Color.GRAY, viewHolder.patientNameAge.getCurrentTextColor());
        assertEquals(View.GONE, viewHolder.nextArrow.getVisibility());
        verify(imageRenderHelper,never()).refreshProfileImage(eq(client.getCaseId()),any(CircleImageView.class),eq(R.mipmap.ic_member));
    }

    @Test
    public void testNextArrowOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.nextArrowColumn.performClick();
        verify(onClickListener).onClick(viewHolder.nextArrow);
    }


    @Test
    public void testProfileOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.profile.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }

    @Test
    public void testRegisterColumnOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.registerColumns.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }


    @Test
    public void testAttachPatientOnclickListener() {
        provider.getView(cursor, client, viewHolder);
        assertEquals(client,viewHolder.patientColumn.getTag());
        assertEquals(BaseFamilyProfileMemberFragment.CLICK_VIEW_NORMAL,viewHolder.patientColumn.getTag(R.id.VIEW_ID));
    }

    @Test
    public void testAttachNextArrowOnclickListener() {
        provider.getView(cursor, client, viewHolder);
        assertEquals(client,viewHolder.nextArrow.getTag());
        assertEquals(BaseFamilyProfileMemberFragment.CLICK_VIEW_NEXT_ARROW,viewHolder.nextArrow.getTag(R.id.VIEW_ID));
    }

    @Test
    public void testPopulateIdentifierColumn(){
        provider.getView(cursor, client, viewHolder);
        assertEquals(View.GONE,viewHolder.familyHead.getVisibility());
        assertEquals(View.GONE,viewHolder.physicallyChallenged.getVisibility());
        assertEquals(View.GONE,viewHolder.primaryCaregiver.getVisibility());
        Whitebox.setInternalState(provider,"familyHead",client.getCaseId());
        Whitebox.setInternalState(provider,"primaryCaregiver",client.getCaseId());
        provider.getView(cursor, client, viewHolder);
        assertEquals(View.VISIBLE,viewHolder.familyHead.getVisibility());
        assertEquals(View.VISIBLE,viewHolder.primaryCaregiver.getVisibility());

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
        assertNotNull(viewHolder.patientNameAge);
    }

    @Test
    public void testCreateFooterHolder() {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        Whitebox.setInternalState(Utils.metadata().familyDueRegister, "showPagination", false);
        FamilyMemberRegisterProvider.FooterViewHolder footer = (FamilyMemberRegisterProvider.FooterViewHolder) provider.createFooterHolder(null);
        assertNotNull(footer);
        assertNotNull(footer.nextPageView);
        assertNotNull(footer.previousPageView);
        assertNotNull(footer.pageInfoView);
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
        FamilyMemberRegisterProvider.FooterViewHolder footer = (FamilyMemberRegisterProvider.FooterViewHolder) provider.createFooterHolder(null);
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
