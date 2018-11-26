package org.smartregister.family.model;

import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.util.FormUtils;

public class FamilyProfileModel implements FamilyProfileContract.Model {

    private FormUtils formUtils;

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject form = getFormUtils().getFormJson(formName);
        if (form == null) {
            return null;
        }
        return JsonFormUtils.getFormAsJson(form, formName, entityId, currentLocationId);
    }

    @Override
    public Pair<Client, Event> processMemberRegistration(String jsonString, String familyBaseEntityId) {
        return JsonFormUtils.processFamilyMemberRegistrationForm(FamilyLibrary.getInstance().context().allSharedPreferences(), jsonString, familyBaseEntityId);
    }


    private FormUtils getFormUtils() {
        if (formUtils == null) {
            try {
                formUtils = FormUtils.getInstance(FamilyLibrary.getInstance().context().applicationContext());
            } catch (Exception e) {
                Log.e(FamilyProfileModel.class.getCanonicalName(), e.getMessage(), e);
            }
        }
        return formUtils;
    }

    public void setFormUtils(FormUtils formUtils) {
        this.formUtils = formUtils;
    }

}
