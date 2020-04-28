package org.smartregister.family.activity;

import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.Context;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.shadow.FamilyProfileActivityShadow;
import org.smartregister.service.UserService;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class BaseFamilyProfileActivityTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyProfileContract.Presenter presenter;


    @Mock
    private UserService userService;

    @Mock
    protected ViewPagerAdapter adapter;

    private BaseFamilyProfileActivity familyProfileActivity;

    @Before
    public void setUp() {
        Context.bindtypes = new ArrayList<>();
        FamilyLibrary.init(Context.getInstance(), getMetadata(), 1, 1);
        Whitebox.setInternalState(Context.getInstance(),"userService",userService);
        when(userService.hasSessionExpired()).thenReturn(false);
        familyProfileActivity = Robolectric.buildActivity(FamilyProfileActivityShadow.class).create().start().resume().get();
        BaseFamilyProfileMemberFragment baseFamilyProfileMemberFragment = Mockito.mock(BaseFamilyProfileMemberFragment.class, Mockito.CALLS_REAL_METHODS);
        Whitebox.setInternalState(familyProfileActivity, "presenter", presenter);
        Whitebox.setInternalState(familyProfileActivity, "adapter", adapter);
        when(adapter.getItem(0)).thenReturn(baseFamilyProfileMemberFragment);
    }

    @Test
    public void testActivityCreation() {
        assertNotNull(familyProfileActivity);
        assertNotNull(familyProfileActivity.findViewById(R.id.textview_detail_one));
        assertNotNull(familyProfileActivity.findViewById(R.id.textview_name));
        assertNotNull(familyProfileActivity.findViewById(R.id.imageview_profile));

    }

    @Test
    public void onResumption() {
        familyProfileActivity.onResumption();
        Mockito.verify(presenter).refreshProfileView();
    }


    @Test
    public void fetchProfileData() {
        familyProfileActivity.fetchProfileData();
        Mockito.verify(presenter).fetchProfileData();
    }

    @Test
    public void setProfileName() {
        TextView nameView = new TextView(RuntimeEnvironment.systemContext);
        String name = "Jammie Fox";
        Whitebox.setInternalState(familyProfileActivity, "nameView", nameView);
        familyProfileActivity.setProfileName(name);
        assertEquals(nameView.getText(), name);
    }

    @Test
    public void setProfileDetailOne() {
        TextView detailOneView = new TextView(RuntimeEnvironment.systemContext);
        String name = "Vancouver, Canada";
        Whitebox.setInternalState(familyProfileActivity, "detailOneView", detailOneView);
        familyProfileActivity.setProfileDetailOne(name);
        assertEquals(detailOneView.getText(), name);
    }

    @Test
    public void setProfileDetailTwo() {
        TextView detailTwoView = new TextView(RuntimeEnvironment.systemContext);
        String name = "28years";
        Whitebox.setInternalState(familyProfileActivity, "detailTwoView", detailTwoView);
        familyProfileActivity.setProfileDetailTwo(name);
        assertEquals(detailTwoView.getText(), name);
    }

    @Test
    public void setProfileDetailThree() {
        TextView detailThreeView = new TextView(RuntimeEnvironment.systemContext);
        String name = "Primary Caregiver";
        Whitebox.setInternalState(familyProfileActivity, "detailThreeView", detailThreeView);
        familyProfileActivity.setProfileDetailThree(name);
        assertEquals(detailThreeView.getText(), name);
    }

    @Test
    public void getProfileMemberFragment() {
        assertNotNull(familyProfileActivity.getProfileMemberFragment());
    }

    @Test
    public void presenter() {
        FamilyProfileContract.Presenter presenterInstance = familyProfileActivity.presenter();
        assertEquals(presenter, presenterInstance);
    }
}