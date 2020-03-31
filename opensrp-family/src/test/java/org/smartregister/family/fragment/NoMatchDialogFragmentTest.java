package org.smartregister.family.fragment;

import android.app.FragmentManager;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.activity.BaseFamilyRegisterActivity;
import org.smartregister.view.activity.BaseRegisterActivity;

@Ignore("Not yet completed")
public class NoMatchDialogFragmentTest extends BaseUnitTest {

    @Test
    public void launchDialog() {
        BaseRegisterActivity familyRegisterActivity = Mockito.mock(BaseFamilyRegisterActivity.class, Mockito.CALLS_REAL_METHODS);
        FragmentManager fragmentManager = Mockito.mock(FragmentManager.class);
        Mockito.when(familyRegisterActivity.getFragmentManager()).thenReturn(fragmentManager);
        NoMatchDialogFragment noMatchDialogFragment = NoMatchDialogFragment.launchDialog(familyRegisterActivity, NoMatchDialogFragment.class.getName(), "who-anc-id");
        Assert.assertNotNull(noMatchDialogFragment);
    }
}