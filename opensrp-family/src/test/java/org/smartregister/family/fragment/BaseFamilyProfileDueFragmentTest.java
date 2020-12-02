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
import org.smartregister.family.contract.FamilyProfileDueContract;
import org.smartregister.family.presenter.BaseFamilyProfileDuePresenter;
import org.smartregister.family.shadow.BaseFamilyProfileDueFragmentShadow;
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
public class BaseFamilyProfileDueFragmentTest {

    private BaseFamilyProfileDueFragment familyProfileDueFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        familyProfileDueFragment = new BaseFamilyProfileDueFragmentShadow();
        BaseFamilyProfileDuePresenter familyProfileDuePresenter = spy(new BaseFamilyProfileDuePresenter(Mockito.mock(FamilyProfileDueContract.View.class),
                Mockito.mock(FamilyProfileDueContract.Model.class), null, "familybaseid"));
        doNothing().when(familyProfileDuePresenter).initializeQueries(anyString());
        Whitebox.setInternalState(familyProfileDueFragment, "presenter", familyProfileDuePresenter);
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        Whitebox.setInternalState(familyProfileDueFragment, "searchView", new EditText(activity));
        activity.setContentView(R.layout.activity_family_profile);
        activity.getSupportFragmentManager().beginTransaction().add(familyProfileDueFragment, "BaseFamilyProfileDueFragment").commit();
    }


    @Test
    public void getMainCondition() {
        assertEquals(familyProfileDueFragment.getMainCondition(), String.format(" %s = '%s' and %s is null ",
                DBConstants.KEY.OBJECT_RELATIONAL_ID, "familybaseid", DBConstants.KEY.DATE_REMOVED));
    }

    @Test
    public void getDefaultSortQuery() {
        assertEquals(familyProfileDueFragment.getDefaultSortQuery(), DBConstants.KEY.DOD + ", " + DBConstants.KEY.DOB + " ASC ");
    }

    @Test
    public void setUniqueID() {
        familyProfileDueFragment.setUniqueID("unique");
        assertEquals(familyProfileDueFragment.searchView.getText().toString(), "unique");
    }

    @Test
    public void presenter() {
        assertNotNull(familyProfileDueFragment.presenter());
    }
}