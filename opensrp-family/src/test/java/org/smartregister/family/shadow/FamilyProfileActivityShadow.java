package org.smartregister.family.shadow;

import androidx.viewpager.widget.ViewPager;

import org.smartregister.family.activity.BaseFamilyProfileActivity;
import org.smartregister.family.contract.FamilyProfileContract;

import static org.mockito.Mockito.mock;

/**
 * Created by samuelgithengi on 4/28/20.
 */
public class FamilyProfileActivityShadow extends BaseFamilyProfileActivity {
    @Override
    protected void initializePresenter() {
        presenter = mock(FamilyProfileContract.Presenter.class);
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return viewPager;
    }
}
