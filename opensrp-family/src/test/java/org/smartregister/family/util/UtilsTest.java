package org.smartregister.family.util;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.domain.FamilyMetadata;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Mock
    private FamilyLibrary familyLibrary;

    @Mock
    private FamilyMetadata familyMetadata;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getProfileImageResourceIDentifier() {
        assertEquals(Utils.getProfileImageResourceIdentifier(), R.mipmap.ic_family_white);
    }

    @Test
    public void getProfileImageTwoResourceIDentifier() {
        assertEquals(Utils.getProfileImageResourceIdentifier(), R.mipmap.ic_family_white);
    }

    @Test
    public void getMemberProfileImageResourceIDentifier() {
        Mockito.when(familyLibrary.metadata()).thenReturn(familyMetadata);
        assertEquals(Utils.getMemberProfileImageResourceIdentifier(""), R.mipmap.ic_member);
     /*   Mockito.doReturn(familyRegister).when(familyMetadata.familyRegister);
        familyMetadata.updateFamilyRegister("forname", "family_table",
                "", "", "", "", "");
        assertEquals(Utils.getMemberProfileImageResourceIdentifier("family_table"), R.mipmap.ic_member);
        assertEquals(Utils.getMemberProfileImageResourceIdentifier("child"), R.mipmap.ic_child);*/
    }

    @Test
    public void getName() {
        assertEquals(Utils.getName("John", "Samuel", "Doe"), "John Samuel Doe");
        assertEquals(Utils.getName("John", null, "Doe"), "John Doe");
    }

    @Test
    @Ignore("Need mock to CoreLib")
    public void getDuration(){
        assertEquals(Utils.getDuration("2020-02-11","2020-04-13"), 60);
    }
}