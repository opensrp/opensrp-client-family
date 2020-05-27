package org.smartregister.family.provider;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.util.DBConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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


}
