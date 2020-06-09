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
import org.smartregister.family.R;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.helper.ImageRenderHelper;

import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by samuelgithengi on 6/9/20.
 */
public class FamilyMemberRegisterProviderTest extends BaseUnitTest {

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
    public void tesRegisterColumnOnClick() {
        provider.getView(cursor, client, viewHolder);
        viewHolder.registerColumns.performClick();
        verify(onClickListener).onClick(viewHolder.patientColumn);
    }





}
