package org.smartregister.family;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.smartregister.family.activity.FamilyWizardFormActivity;
import org.smartregister.family.domain.FamilyMetadata;
import org.smartregister.family.shadow.CustomFontTextViewShadow;
import org.smartregister.util.DateTimeTypeConverter;
import org.smartregister.view.activity.BaseProfileActivity;


@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, shadows = {CustomFontTextViewShadow.class}, sdk = Build.VERSION_CODES.P)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public abstract class BaseUnitTest {

    protected static final String DUMMY_USERNAME = "myusername";
    protected static final String DUMMY_PASSWORD = "mypassword";

    protected final int ASYNC_TIMEOUT = 2000;
    private FamilyMetadata metadata;

    protected static Gson taskGson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter("yyyy-MM-dd'T'HHmm"))
            .serializeNulls().create();

    protected static String getString(int stringResourceId) {
        return RuntimeEnvironment.application.getResources().getString(stringResourceId);
    }

    protected FamilyMetadata getMetadata() {
        BaseProfileActivity activity = Mockito.mock(BaseProfileActivity.class);
        if(metadata == null){
            metadata = new FamilyMetadata(FamilyWizardFormActivity.class, FamilyWizardFormActivity.class, activity.getClass(), "UNIQUE_IDENTIFIER_KEY", true);
            metadata.updateFamilyRegister("FAMILY_REGISTER", "FAMILY", "FAMILY_REGISTRATION", "UPDATE_FAMILY_REGISTRATION", "FAMILY_REGISTER", "FAMILY_HEAD", "PRIMARY_CAREGIVER");
            metadata.updateFamilyMemberRegister("FAMILY_MEMBER_REGISTER", "FAMILY_MEMBER", "FAMILY_MEMBER_REGISTRATION", "UPDATE_FAMILY_MEMBER_REGISTRATION", "FAMILY_MEMBER_REGISTER", "FAMILY");
            metadata.updateFamilyDueRegister("FAMILY_MEMBER", 20, true);
            metadata.updateFamilyActivityRegister("FAMILY_MEMBER", Integer.MAX_VALUE, false);
            metadata.updateFamilyOtherMemberRegister("FAMILY_MEMBER", Integer.MAX_VALUE, false);
        }

        return metadata;
    }

}

