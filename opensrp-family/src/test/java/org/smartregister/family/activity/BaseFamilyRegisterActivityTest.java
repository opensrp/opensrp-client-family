package org.smartregister.family.activity;

import android.content.Intent;
import android.widget.Toast;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowToast;
import org.smartregister.Context;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.contract.FamilyRegisterContract;
import org.smartregister.family.shadow.FamilyRegisterActivityShadow;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class BaseFamilyRegisterActivityTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyRegisterContract.Presenter presenter;

    private BaseFamilyRegisterActivity familyRegisterActivity;

    @Before
    public void setUp() {
        Context.bindtypes = new ArrayList<>();
        FamilyLibrary.init(Context.getInstance(), getMetadata(), 1, 1);
        Intent intent = new Intent();
        familyRegisterActivity = Robolectric.buildActivity(FamilyRegisterActivityShadow.class, intent).create().start().resume().get();
        Whitebox.setInternalState(familyRegisterActivity, "presenter", presenter);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
    }

    @Test
    public void testGetPresenter() {
        FamilyRegisterContract.Presenter presenterInstance = familyRegisterActivity.presenter();
        assertEquals(presenter, presenterInstance);
    }

    @Test
    public void testStartRegistration() throws Exception {
        familyRegisterActivity.startRegistration();
        verify(presenter).startForm("FAMILY_REGISTER", null, null, "");

    }

    @Test
    public void testStartFormActivityDisplaysToastOnErrorOnError() throws Exception {
        doThrow(new RuntimeException()).when(presenter).startForm(anyString(), anyString(), anyString(), anyString());
        familyRegisterActivity.startFormActivity("FAMILY_REGISTER", "1234", "location3");
        verify(presenter).startForm("FAMILY_REGISTER", "1234", "location3", "");
        Toast toast = ShadowToast.getLatestToast();
        assertNotNull(toast);
        assertEquals(familyRegisterActivity.getString(R.string.error_unable_to_start_form), ShadowToast.getTextOfLatestToast());
        assertEquals(Toast.LENGTH_LONG, toast.getDuration());


    }

}