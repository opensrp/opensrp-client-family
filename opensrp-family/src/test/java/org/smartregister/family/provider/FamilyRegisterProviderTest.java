package org.smartregister.family.provider;

import android.database.Cursor;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.view.contract.SmartRegisterClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.view.View.OnClickListener;

@Ignore("Not completed test - FamilyRegisterProvider")
public class FamilyRegisterProviderTest extends BaseUnitTest {

    private FamilyRegisterProvider familyRegisterProvider;

    @Mock
    private OnClickListener onClickListener;

    @Mock
    private OnClickListener paginationClickListener;

    @Before
    public void setUp() {
        familyRegisterProvider = new FamilyRegisterProvider(RuntimeEnvironment.systemContext,
                null, Mockito.spy(Set.class), onClickListener, paginationClickListener);
    }

    @Test
    @Ignore("Not complete")
    public void getView() {
        Map<String, String> detailsMap = new HashMap<>();
        detailsMap.put("village_town", "Virginia");
        detailsMap.put("family_head", "10929091-2-129019221");
        SmartRegisterClient smartRegisterClient = new CommonPersonObjectClient("some-case-id", detailsMap, "Cynthia Rothrock");
        familyRegisterProvider.getView(Mockito.mock(Cursor.class), smartRegisterClient, Mockito.mock(FamilyRegisterProvider.RegisterViewHolder.class));
        Assert.assertNotNull(familyRegisterProvider);
    }


    @Test
    public void isFooterViewHolder() {
        Assert.assertFalse(familyRegisterProvider.isFooterViewHolder(null));
    }

    @Test
    public void fillValue() {
        TextView textView = new TextView(RuntimeEnvironment.systemContext);
        FamilyRegisterProvider.fillValue(textView, "Good text");
        Assert.assertEquals(textView.getText().toString(), "Good text");
    }
}