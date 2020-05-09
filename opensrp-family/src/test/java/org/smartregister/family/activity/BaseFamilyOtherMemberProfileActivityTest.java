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
import org.robolectric.shadows.ShadowActivity;
import org.smartregister.Context;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.shadow.ShadowFamilyOtherMemberProfileActivity;
import org.smartregister.family.util.Utils;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.service.UserService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.smartregister.AllConstants.FORM_NAME_PARAM;
import static org.smartregister.AllConstants.FORM_SUCCESSFULLY_SUBMITTED_RESULT_CODE;

public class BaseFamilyOtherMemberProfileActivityTest extends BaseUnitTest {


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyOtherMemberContract.Presenter presenter;

    @Mock
    private UserService userService;

    @Mock
    private ImageRenderHelper imageRenderHelper;

    @Mock
    private CircleImageView circleImageView;

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
    public void testOnCreationAndSetUpViews() {
        Whitebox.setInternalState(Context.getInstance(), "userService", userService);
        when(userService.hasSessionExpired()).thenReturn(false);
        familyOtherMemberProfileActivity = Robolectric.buildActivity(ShadowFamilyOtherMemberProfileActivity.class).create().visible().get();

        assertNotNull(familyOtherMemberProfileActivity);
        assertNotNull(familyOtherMemberProfileActivity.findViewById(R.id.textview_detail_one));
        assertNotNull(familyOtherMemberProfileActivity.findViewById(R.id.textview_name));
        assertNotNull(familyOtherMemberProfileActivity.findViewById(R.id.imageview_profile));
        assertNotNull(familyOtherMemberProfileActivity.presenter());

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

    @Test
    public void testOnResumption() {
        familyOtherMemberProfileActivity.onResumption();
        verify(presenter).refreshProfileView();
    }

    @Test
    public void testOnDestroy() {
        familyOtherMemberProfileActivity.onDestroy();
        verify(presenter).onDestroy(false);
    }


    @Test
    public void testOnOptionsItemSelectedStartsForm() {
        shadowOf(familyOtherMemberProfileActivity).clickMenuItem(R.id.add_member);
        ShadowActivity.IntentForResult intent = shadowOf(familyOtherMemberProfileActivity).getNextStartedActivityForResult();
        assertNotNull(intent);
        assertEquals(FORM_SUCCESSFULLY_SUBMITTED_RESULT_CODE, intent.requestCode);
        assertEquals(Utils.metadata().familyMemberRegister.formName, intent.intent.getStringExtra(FORM_NAME_PARAM));
    }

    @Test
    public void testSetProfileImage() {
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "imageRenderHelper", imageRenderHelper);
        Whitebox.setInternalState(familyOtherMemberProfileActivity, "imageView", circleImageView);
        familyOtherMemberProfileActivity.setProfileImage("user1", Utils.metadata().familyMemberRegister.registerEventType);
        verify(imageRenderHelper).refreshProfileImage("user1", circleImageView, R.mipmap.ic_child);
    }

}