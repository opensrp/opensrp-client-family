package org.smartregister.family.shadow;

import android.support.v4.view.ViewPager;

import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.presenter.BaseFamilyOtherMemberProfileActivityPresenter;

import static org.mockito.Mockito.mock;

/**
 * Created by samuelgithengi on 4/28/20.
 */
public class FamilyProfileActivityShadow extends BaseFamilyProfileActivity {
    @Override
    protected void initializePresenter() {
        presenter = mock(BaseFamilyOtherMemberProfileActivityPresenter.class);
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return viewPager;
    }
}
