package org.smartregister.family.activity;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.family.BaseUnitTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FamilyWizardFormActivityTest extends BaseUnitTest {

    private ActivityController<FamilyWizardFormActivity> activityController;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(FamilyWizardFormActivity.class);
    }

    @Test
    public void testIsInstanceOfJsonWizardFormActivity() {
        assertThat(activityController.get(), is(instanceOf(JsonWizardFormActivity.class)));
    }

    @Test
    public void testOnBackPressedWithEnableOnCloseDialogFalseCallsFinish() {
        FamilyWizardFormActivity activity = spy(activityController.get());
        Whitebox.setInternalState(activity, "enableOnCloseDialog", false);
        activity.onBackPressed();
        verify(activity).finish();
    }

    @Test
    public void testOnBackPressedWithEnableOnCloseDialogDoesNotCallFinish() {
        FamilyWizardFormActivity activity = spy(activityController.get());
        Whitebox.setInternalState(activity, "enableOnCloseDialog", true);
        activity.onBackPressed();
        verify(activity, never()).finish();
    }

}
