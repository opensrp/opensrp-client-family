package org.smartregister.family.interactor;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.TestApplication;
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.family.util.Utils;

import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
@Ignore("Not completed")
public class FamilyOtherMemberProfileInteractorTest {

    private final int ASYNC_TIMEOUT = 8000;

    @Mock
    private FamilyOtherMemberContract.InteractorCallBack callback;

    private FamilyOtherMemberContract.Interactor familyMemberInteractor;

    @Before
    public void setUp() {
        AppExecutors appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor());
        MockitoAnnotations.initMocks(this);
        familyMemberInteractor = new FamilyOtherMemberProfileInteractor(appExecutors);
    }

    @Test
    public void testRefreshProfileView() {
        familyMemberInteractor.refreshProfileView("some-crazy-base-entity-id", callback);
        FamilyMetadata.FamilyMemberRegister familyMemberRegister = mock(FamilyMetadata.FamilyMemberRegister.class);
        ReflectionHelpers.setField(familyMemberRegister, "tableName", "fam-table");
        ReflectionHelpers.setField(Utils.metadata(), "familyMemberRegister", familyMemberRegister);
        verify(callback, timeout(ASYNC_TIMEOUT)).refreshProfileTopSection(any(CommonPersonObjectClient.class));
    }
}