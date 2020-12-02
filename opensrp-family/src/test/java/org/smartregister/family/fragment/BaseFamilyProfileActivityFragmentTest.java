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
import org.smartregister.family.contract.FamilyProfileActivityContract;
import org.smartregister.family.presenter.BaseFamilyProfileActivityPresenter;
import org.smartregister.family.shadow.BaseFamilyProfileActivityFragmentShadow;
import org.smartregister.family.shadow.CustomFontTextViewShadow;

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
public class BaseFamilyProfileActivityFragmentTest {

    private BaseFamilyProfileActivityFragment familyProfileActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        familyProfileActivity = new BaseFamilyProfileActivityFragmentShadow();
        BaseFamilyProfileActivityPresenter presenter = spy(new BaseFamilyProfileActivityPresenter(Mockito.mock(FamilyProfileActivityContract.View.class),
                Mockito.mock(FamilyProfileActivityContract.Model.class), null, "familybaseid"));
        doNothing().when(presenter).initializeQueries(anyString());
        Whitebox.setInternalState(familyProfileActivity, "presenter", presenter);
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        Whitebox.setInternalState(familyProfileActivity, "searchView", new EditText(activity));
        activity.setContentView(R.layout.activity_family_profile);
        activity.getSupportFragmentManager().beginTransaction().add(familyProfileActivity, "BaseFamilyProfileActivityFragment").commit();

    }

    @Test
    public void getMainCondition() {
        assertEquals(familyProfileActivity.getMainCondition(), " object_relational_id = 'familybaseid' and date_removed is null ");
    }

    @Test
    public void getDefaultSortQuery() {
        assertEquals(familyProfileActivity.getDefaultSortQuery(), "dod, dob ASC ");
    }

    @Test
    public void setUniqueID() {
        familyProfileActivity.setUniqueID("unique");
        assertEquals(familyProfileActivity.searchView.getText().toString(), "unique");
    }

    @Test
    public void presenter() {
        assertNotNull(familyProfileActivity.presenter());
    }
}