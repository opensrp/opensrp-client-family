package org.smartregister.family.interactor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.TestDataUtils;
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.family.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


public class FamilyOtherMemberProfileInteractorTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FamilyOtherMemberContract.InteractorCallBack callback;

    @Mock
    private FamilyMetadata.FamilyMemberRegister familyMemberRegister;

    @Mock
    private CommonRepository commonRepository;

    @Captor
    private ArgumentCaptor<CommonPersonObjectClient> commonPersonObjectClientArgumentCaptor;

    private CommonPersonObject commonPersonObject;

    private FamilyOtherMemberContract.Interactor familyMemberInteractor;


    private AppExecutors appExecutors;

    @Before
    public void setUp() {
        appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor());
        familyMemberInteractor = new FamilyOtherMemberProfileInteractor(appExecutors);
        commonPersonObject = new CommonPersonObject("some-crazy-base-entity-id", "", null, "");
        commonPersonObject.setColumnmaps(TestDataUtils.getCommonPersonObjectClient().getColumnmaps());
    }

    @Test
    public void testRefreshProfileView() {
        String tableName = "fam-table";
        Map<String, CommonRepository> mapOfCommonRepository = new HashMap<>();
        mapOfCommonRepository.put(tableName, commonRepository);
        ReflectionHelpers.setField(FamilyLibrary.getInstance().context(), "MapOfCommonRepository", mapOfCommonRepository);
        when(commonRepository.findByBaseEntityId(commonPersonObject.getCaseId())).thenReturn(commonPersonObject);
        ReflectionHelpers.setField(familyMemberRegister, "tableName", tableName);
        ReflectionHelpers.setField(Utils.metadata(), "familyMemberRegister", familyMemberRegister);

        familyMemberInteractor.refreshProfileView(commonPersonObject.getCaseId(), callback);
        verify(callback, timeout(ASYNC_TIMEOUT)).refreshProfileTopSection(commonPersonObjectClientArgumentCaptor.capture());
        assertEquals(commonPersonObject.getCaseId(), commonPersonObjectClientArgumentCaptor.getValue().getCaseId());
        assertEquals(commonPersonObject.getColumnmaps(), commonPersonObjectClientArgumentCaptor.getValue().getColumnmaps());
    }

    @Test
    public void testOnDestroy() {
        appExecutors = spy(appExecutors);
        familyMemberInteractor.onDestroy(false);
        verifyNoMoreInteractions(appExecutors);
    }
}