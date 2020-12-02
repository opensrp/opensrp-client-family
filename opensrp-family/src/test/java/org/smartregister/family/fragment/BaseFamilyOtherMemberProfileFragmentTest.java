package org.smartregister.family.fragment;

import androidx.appcompat.app.AppCompatActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.R;
import org.smartregister.family.contract.FamilyOtherMemberProfileFragmentContract;
import org.smartregister.family.shadow.BaseFamilyOtherMemberProfileFragmentShadow;

import java.util.ArrayList;

/**
 * Created by rkodev
 */
public class BaseFamilyOtherMemberProfileFragmentTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FamilyOtherMemberProfileFragmentContract.Presenter presenter;

    private BaseFamilyOtherMemberProfileFragment fragment;

    @Before
    public void setUp() {
        org.smartregister.Context.bindtypes = new ArrayList<>();
        fragment = new BaseFamilyOtherMemberProfileFragmentShadow();
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        activity.setContentView(R.layout.activity_family_profile);
        Whitebox.setInternalState(fragment, "presenter", presenter);
        activity.getSupportFragmentManager().beginTransaction().add(fragment, "Presenter").commit();

    }

    @Test
    public void testPresenter() {
        FamilyOtherMemberProfileFragmentContract.Presenter presenterInstance = fragment.presenter();
        Assert.assertEquals(presenter, presenterInstance);
    }
}
