package org.smartregister.family.provider;

import android.content.Context;
import android.database.Cursor;
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
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.R;
import org.smartregister.family.TestDataUtils;
import org.smartregister.view.contract.SmartRegisterClient;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

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

    private SmartRegisterClient client = TestDataUtils.getCommonPersonObjectClient();


    private FamilyActivityRegisterProvider familyActivityRegisterProvider;

    private View rootView;

    @Before
    public void setUp() {
        familyActivityRegisterProvider = new FamilyActivityRegisterProvider(context, commonRepository, visibleColumns, onClickListener, paginationClickListener);
        rootView = familyActivityRegisterProvider.inflater.inflate(R.layout.family_activity_register_list_row, null);
        viewHolder = new FamilyActivityRegisterProvider.RegisterViewHolder(rootView);
    }

    @Test
    public void testPopulatePatientColumn() {
        LinearLayout patientColumn = rootView.findViewById(R.id.patient_column);
        TextView textView = new TextView(context);
        textView.setId(R.id.gender);
        patientColumn.addView(textView);
        viewHolder = new FamilyActivityRegisterProvider.RegisterViewHolder(rootView);
        familyActivityRegisterProvider.getView(cursor, client, viewHolder);
        assertEquals("Charity Otala, 38", viewHolder.patientNameAge.getText());
        assertEquals("Female", viewHolder.gender.getText());
        assertEquals("Completed on 10 July 2018", viewHolder.lastVisit.getText());
    }
}
