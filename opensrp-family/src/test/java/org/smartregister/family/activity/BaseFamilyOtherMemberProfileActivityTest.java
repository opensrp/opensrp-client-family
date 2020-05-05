package org.smartregister.family.activity;

import android.view.View;
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
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.shadow.ShadowFamilyOtherMemberProfileActivity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BaseFamilyOtherMemberProfileActivityTest extends BaseUnitTest {


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private FamilyOtherMemberContract.Presenter presenter;

    private BaseFamilyOtherMemberProfileActivity familyOtherMemberProfileActivity;

    @Before
    public void setUp() {
        Context.bindtypes = new ArrayList<>();
        FamilyLibrary.init(Context.getInstance(), getMetadata(), 1, 1);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        familyOtherMemberProfileActivity = Robolectric.buildActivity(ShadowFamilyOtherMemberProfileActivity.class).create().visible().get();
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "presenter", presenter);
    }

    @Test
    public void fetchProfileData() {
        familyOtherMemberProfileActivity.fetchProfileData();
        Mockito.verify(presenter).fetchProfileData();
    }

    @Test
    public void setProfileName() {
        TextView nameView = new TextView(RuntimeEnvironment.systemContext);
        String name = "Jammie Fox";
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "nameView", nameView);
        familyOtherMemberProfileActivity.setProfileName(name);
        assertEquals(nameView.getText(), name);
    }

    @Test
    public void setProfileDetailOne() {
        TextView detailOneView = new TextView(RuntimeEnvironment.systemContext);
        String name = "Vancouver, Canada";
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "detailOneView", detailOneView);
        familyOtherMemberProfileActivity.setProfileDetailOne(name);
        assertEquals(detailOneView.getText(), name);
    }

    @Test
    public void setProfileDetailTwo() {
        TextView detailTwoView = new TextView(RuntimeEnvironment.systemContext);
        String name = "28years";
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "detailTwoView", detailTwoView);
        familyOtherMemberProfileActivity.setProfileDetailTwo(name);
        assertEquals(detailTwoView.getText(), name);
    }

    @Test
    public void setProfileDetailThree() {
        TextView detailThreeView = new TextView(RuntimeEnvironment.systemContext);
        String name = "Primary Caregiver";
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "detailThreeView", detailThreeView);
        familyOtherMemberProfileActivity.setProfileDetailThree(name);
        assertEquals(detailThreeView.getText(), name);
    }

    @Test
    public void toggleFamilyHead() {
        View familyHead = new View(RuntimeEnvironment.systemContext);
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "familyHead", familyHead);
        familyOtherMemberProfileActivity.toggleFamilyHead(true);
        assertEquals(familyHead.getVisibility(), View.VISIBLE);
        familyOtherMemberProfileActivity.toggleFamilyHead(false);
        assertEquals(familyHead.getVisibility(), View.GONE);
    }

    @Test
    public void togglePrimaryCaregiver() {
        View primaryCaregiver = new View(RuntimeEnvironment.systemContext);
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "primaryCaregiver", primaryCaregiver);
        familyOtherMemberProfileActivity.togglePrimaryCaregiver(true);
        assertEquals(primaryCaregiver.getVisibility(), View.VISIBLE);
        familyOtherMemberProfileActivity.togglePrimaryCaregiver(false);
        assertEquals(primaryCaregiver.getVisibility(), View.GONE);
    }

    @Test
    public void presenter() {
        FamilyOtherMemberContract.Presenter presenterInstance = familyOtherMemberProfileActivity.presenter();
        assertEquals(presenter, presenterInstance);
    }
}