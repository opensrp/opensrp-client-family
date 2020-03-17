package org.smartregister.family.activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.smartregister.Context;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.contract.FamilyRegisterContract;

import static org.junit.Assert.assertEquals;

public class BaseFamilyRegisterActivityTest extends BaseUnitTest {

    @Mock
    private FamilyRegisterContract.Presenter presenter;

    private BaseFamilyRegisterActivity familyRegisterActivity;

    @Before
    public void setUp() {
        Context context = Mockito.mock(Context.class);
        FamilyLibrary.init(context, getMetadata(), 1, 1);
        MockitoAnnotations.initMocks(this);
        familyRegisterActivity = Mockito.mock(BaseFamilyRegisterActivity.class, Mockito.CALLS_REAL_METHODS);
        Whitebox.setInternalState(familyRegisterActivity, "presenter", presenter);
    }

    @Test
    public void presenter() {
        FamilyRegisterContract.Presenter presenterInstance = familyRegisterActivity.presenter();
        assertEquals(presenter, presenterInstance);
    }

}