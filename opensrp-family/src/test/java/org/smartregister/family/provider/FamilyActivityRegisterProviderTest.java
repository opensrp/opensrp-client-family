package org.smartregister.family.provider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.R;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

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

    String ageString;

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
}
