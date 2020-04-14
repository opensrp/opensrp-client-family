package org.smartregister.family.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.Context;
import org.smartregister.family.BaseUnitTest;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.domain.FamilyMetadata;

import timber.log.Timber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.smartregister.family.util.JsonFormUtils.METADATA;
import static org.smartregister.util.JsonFormUtils.ENCOUNTER_LOCATION;

/**
 * Created by samuelgithengi on 4/14/20.
 */
public class JsonFormUtilsTest extends BaseUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Context context;

    @Before
    public void setUp() {
        FamilyLibrary.init(context, null, 1, 1);
        FamilyLibrary.getInstance().setMetadata(getMetadata());
    }

    @Test
    public void getFormAsJson_ShouldReturnNullWithNullForm() throws Exception {
        assertNull(JsonFormUtils.getFormAsJson(null, null, null, null));
    }

    @Test
    public void getFormAsJson_ShouldPopulateEncounterLocation() throws Exception {

        JSONObject originalForm = new JSONObject();
        originalForm.put(METADATA, new JSONObject());
        JSONObject form = JsonFormUtils.getFormAsJson(originalForm, null, null, "location1");
        assertNotNull(form);
        assertEquals("location1", form.getJSONObject(METADATA).getString(ENCOUNTER_LOCATION));
    }
}
