package org.smartregister.family.activity;

import com.vijay.jsonwizard.activities.NoLocaleFormConfigurationJsonWizardFormActivity;

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


public class NoLocaleFamilyWizardFormActivityTest extends BaseUnitTest {

    private ActivityController<NoLocaleFamilyWizardFormActivity> activityController;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(NoLocaleFamilyWizardFormActivity.class);
    }

    @Test
    public void testIsInstanceOfNoLocaleFormConfigurationJsonWizardFormActivity() {
        assertThat(activityController.get(), is(instanceOf(NoLocaleFormConfigurationJsonWizardFormActivity.class)));
    }

    @Test
    public void testOnBackPressedWithEnableOnCloseDialogFalseCallsFinish() {
        NoLocaleFamilyWizardFormActivity activity = spy(activityController.get());
        Whitebox.setInternalState(activity, "enableOnCloseDialog", false);
        activity.onBackPressed();
        verify(activity).finish();
    }

    @Test
    public void testOnBackPressedWithEnableOnCloseDialogDoesNotCallFinish() {
        NoLocaleFamilyWizardFormActivity activity = spy(activityController.get());
        Whitebox.setInternalState(activity, "enableOnCloseDialog", true);
        activity.onBackPressed();
        verify(activity, never()).finish();
    }

}
