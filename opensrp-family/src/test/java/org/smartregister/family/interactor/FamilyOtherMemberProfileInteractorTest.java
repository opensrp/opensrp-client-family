package org.smartregister.family.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.util.AppExecutors;

import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class FamilyOtherMemberProfileInteractorTest {

    private final int ASYNC_TIMEOUT = 3000;

    @Mock
    private FamilyOtherMemberContract.InteractorCallBack callback;

    private FamilyOtherMemberContract.Interactor familyMemberInteractor;

    @Before
    public void setUp(){
        AppExecutors appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor());
        MockitoAnnotations.initMocks(this);
        familyMemberInteractor = new FamilyOtherMemberProfileInteractor(appExecutors);
    }

    @Test
    public void testRefreshProfileView() {
        familyMemberInteractor.refreshProfileView("some-crazy-base-entity-id", callback);
        verify(callback, timeout(ASYNC_TIMEOUT)).refreshProfileTopSection(any(CommonPersonObjectClient.class));
    }
}