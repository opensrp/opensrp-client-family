package org.smartregister.family.fragment;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.family.R;
import org.smartregister.family.TestApplication;
import org.smartregister.family.contract.FamilyProfileMemberContract;
import org.smartregister.family.presenter.BaseFamilyProfileMemberPresenter;
import org.smartregister.family.shadow.BaseFamilyProfileMemberFragmentShadow;
import org.smartregister.family.shadow.CustomFontTextViewShadow;
import org.smartregister.family.util.DBConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

/**
 * @author Elly Kitoto (Nerdstone)
 */

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, shadows = {CustomFontTextViewShadow.class})
public class BaseFamilyProfileMemberFragmentTest {

    private BaseFamilyProfileMemberFragment familyProfileMemberFragment;

    private BaseFamilyProfileMemberPresenter familyProfileMemberPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        familyProfileMemberFragment = new BaseFamilyProfileMemberFragmentShadow();
        familyProfileMemberPresenter = spy(new BaseFamilyProfileMemberPresenter(Mockito.mock(FamilyProfileMemberContract.View.class),
                Mockito.mock(FamilyProfileMemberContract.Model.class), null, "familybaseid", "Uhunye", "RAO"));
        doNothing().when(familyProfileMemberPresenter).initializeQueries(anyString());
        Whitebox.setInternalState(familyProfileMemberFragment, "presenter", familyProfileMemberPresenter);
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        Whitebox.setInternalState(familyProfileMemberFragment, "searchView", new EditText(activity));
        activity.setContentView(R.layout.activity_family_profile);
        activity.getSupportFragmentManager().beginTransaction().add(familyProfileMemberFragment, "BaseFamilyProfileMemberFragment").commit();

    }

    @Test
    public void getMainCondition() {
        assertEquals(familyProfileMemberFragment.getMainCondition(), String.format(" %s = '%s' and %s is null ", DBConstants.KEY.OBJECT_RELATIONAL_ID, "familybaseid", DBConstants.KEY.DATE_REMOVED));
    }

    @Test
    public void getDefaultSortQuery() {
        assertEquals(familyProfileMemberFragment.getDefaultSortQuery(), DBConstants.KEY.DOD + ", " + DBConstants.KEY.DOB + " ASC ");
    }

    @Test
    public void setFamilyHead() {
        String familyHead = "Hommer Simpsons";
        familyProfileMemberFragment.setFamilyHead(familyHead);
        assertEquals(familyHead, familyProfileMemberPresenter.getFamilyHead());
    }

    @Test
    public void setPrimaryCaregiver() {
        String primaryCareGiver = "Marge Simpsons";
        familyProfileMemberFragment.setPrimaryCaregiver(primaryCareGiver);
        assertEquals(primaryCareGiver, familyProfileMemberPresenter.getPrimaryCaregiver());
    }

    @Test
    public void presenter() {
        assertNotNull(familyProfileMemberFragment.presenter());
    }
}