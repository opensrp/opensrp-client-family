package org.smartregister.family.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
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
import org.robolectric.shadows.ShadowToast;
import org.smartregister.Context;
import org.smartregister.domain.FetchStatus;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.adapter.ViewPagerAdapter;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.shadow.FamilyProfileActivityShadow;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.service.UserService;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

public class BaseFamilyProfileActivityTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyProfileContract.Presenter presenter;

    @Mock
    private BaseFamilyProfileMemberFragment memberFragment;

    @Mock
    private UserService userService;

    @Mock
    protected ViewPagerAdapter adapter;

    @Mock
    private ImageRenderHelper imageRenderHelper;

    private BaseFamilyProfileActivity familyProfileActivity;

    private AppExecutors appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor());

    @Before
    public void setUp() {
        Context.bindtypes = new ArrayList<>();
        FamilyLibrary.init(Context.getInstance(), getMetadata(), 1, 1);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        Whitebox.setInternalState(Context.getInstance(), "userService", userService);
        when(userService.hasSessionExpired()).thenReturn(false);
        familyProfileActivity = Robolectric.buildActivity(FamilyProfileActivityShadow.class).create().visible().get();
        Whitebox.setInternalState(familyProfileActivity, "presenter", presenter);
        Whitebox.setInternalState(familyProfileActivity, "adapter", adapter);
        when(adapter.getItem(0)).thenReturn(memberFragment);
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
        verify(presenter).fetchProfileData();
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


    @Test
    public void testStartRegistration() throws Exception {
        familyProfileActivity.startFormActivity("FAMILY_REGISTER", "1234", "team:3");
        verify(presenter).startForm("FAMILY_REGISTER", "1234", "team:3", "");
    }

    @Test
    public void testStartFormActivityDisplaysToastOnErrorOnError() throws Exception {
        doThrow(new RuntimeException()).when(presenter).startForm(anyString(), anyString(), anyString(), anyString());
        familyProfileActivity.startFormActivity("FAMILY_REGISTER", "1234", "location3");
        verify(presenter).startForm("FAMILY_REGISTER", "1234", "location3", "");
        Toast toast = ShadowToast.getLatestToast();
        assertNotNull(toast);
        assertEquals(familyProfileActivity.getString(R.string.error_unable_to_start_form), ShadowToast.getTextOfLatestToast());
        assertEquals(Toast.LENGTH_SHORT, toast.getDuration());


    }

    @Test
    public void testStartFormActivity() throws JSONException {
        JSONObject form = new JSONObject(TestDataUtils.FAMILY_MEMBER_FORM);
        familyProfileActivity.startFormActivity(form);
        Intent intent = shadowOf(familyProfileActivity).getNextStartedActivity();
        assertNotNull(intent);
        assertEquals(FamilyWizardFormActivity.class, shadowOf(intent).getIntentClass());
        assertEquals(intent.getStringExtra(Constants.JSON_FORM_EXTRA.JSON), form.toString());
        assertNotNull(intent.getSerializableExtra(JsonFormConstants.JSON_FORM_KEY.FORM));
    }


    @Test
    public void testOnActivitySavesFamilyMember() throws JSONException {
        JSONObject form = new JSONObject(TestDataUtils.FILLED_FAMILY_FORM);
        form.put(JsonFormUtils.ENCOUNTER_TYPE, Utils.metadata().familyMemberRegister.registerEventType);
        Intent intent = new Intent();
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, form.toString());
        familyProfileActivity.onActivityResult(JsonFormUtils.REQUEST_CODE_GET_JSON, Activity.RESULT_OK, intent);
        verify(presenter).saveFamilyMember(form.toString());

    }

    @Test
    public void testOnActivityUpdatesFamily() throws JSONException {
        FamilyLibrary.getInstance().setMetadata(getMetadata());
        JSONObject form = new JSONObject(TestDataUtils.FILLED_FAMILY_FORM);
        form.put(JsonFormUtils.ENCOUNTER_TYPE, Utils.metadata().familyRegister.updateEventType);
        Intent intent = new Intent();
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, form.toString());
        familyProfileActivity.onActivityResult(JsonFormUtils.REQUEST_CODE_GET_JSON, Activity.RESULT_OK, intent);
        verify(presenter).updateFamilyRegister(form.toString());

    }

    @Test
    public void testOnDestroy() {
        familyProfileActivity.onDestroy();
        verify(presenter).onDestroy(false);
    }

    @Test
    public void testOnOptionsItemSelected() throws Exception {
        shadowOf(familyProfileActivity).clickMenuItem(R.id.add_member);
        verify(presenter).startForm(Utils.metadata().familyMemberRegister.formName, null, null, "");
    }

    @Test
    public void testRefreshMemberList() {
        familyProfileActivity = spy(familyProfileActivity);
        when(familyProfileActivity.getProfileMemberFragment()).thenReturn(memberFragment);
        familyProfileActivity.refreshMemberList(FetchStatus.fetched);
        verify(memberFragment, timeout(ASYNC_TIMEOUT)).refreshListView();
    }

    @Test
    public void testRefreshMemberListWithWorkerThread() {
        familyProfileActivity = spy(familyProfileActivity);
        when(familyProfileActivity.getProfileMemberFragment()).thenReturn(memberFragment);
        Whitebox.setInternalState(familyProfileActivity, "appExecutors", appExecutors);
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                familyProfileActivity.refreshMemberList(FetchStatus.fetched);
            }
        });
        verify(memberFragment, timeout(ASYNC_TIMEOUT)).refreshListView();
    }

    @Test
    public void testDisplayShortToast() {
        familyProfileActivity.displayShortToast(R.string.no_unique_id);
        Toast toast = ShadowToast.getLatestToast();
        assertNotNull(toast);
        assertEquals(Toast.LENGTH_SHORT, toast.getDuration());
        assertEquals(familyProfileActivity.getString(R.string.no_unique_id), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testSetProfileImage() {
        Whitebox.setInternalState(familyProfileActivity, "imageRenderHelper", imageRenderHelper);
        familyProfileActivity.setProfileImage("user1");
        verify(imageRenderHelper).refreshProfileImage(eq("user1"), any(CircleImageView.class), eq(R.mipmap.ic_family_white));
    }


}