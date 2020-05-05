package org.smartregister.family.shadow;

import android.support.v4.view.ViewPager;

import org.smartregister.family.activity.BaseFamilyOtherMemberProfileActivity;
import org.smartregister.family.contract.FamilyOtherMemberContract;

import static org.mockito.Mockito.mock;

/**
 * Created by samuelgithengi on 5/5/20.
 */
public class ShadowFamilyOtherMemberProfileActivity extends BaseFamilyOtherMemberProfileActivity {
    @Override
    protected void initializePresenter() {
        presenter = mock(FamilyOtherMemberContract.Presenter.class);
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return viewPager;
    }
}
