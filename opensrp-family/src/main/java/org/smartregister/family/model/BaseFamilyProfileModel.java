package org.smartregister.family.model;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;
import org.smartregister.util.FormUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFamilyProfileModel implements FamilyProfileContract.Model {

    private FormUtils formUtils;

    private String familyName;
    private boolean familyHead;
    private boolean primaryCaregiver;

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject form = getFormUtils().getFormJson(formName);
        if (form == null) {
            return null;
        }
        return JsonFormUtils.getFormAsJson(form, formName, entityId, currentLocationId);
    }

    @Override
    public FamilyEventClient processMemberRegistration(String jsonString, String familyBaseEntityId) {
        FamilyEventClient familyEventClient = JsonFormUtils.processFamilyMemberRegistrationForm(FamilyLibrary.getInstance().context().allSharedPreferences(), jsonString, familyBaseEntityId);
        if (familyEventClient == null) {
            return null;
        }

        // Add WRA
        Client client = familyEventClient.getClient();
        Event event = familyEventClient.getEvent();
        if (client != null && event != null && client.getGender().equalsIgnoreCase("female") && client.getBirthdate() != null) {
            DateTime date = new DateTime(client.getBirthdate());
            Years years = Years.yearsBetween(date.toLocalDate(), LocalDate.now());
            int age = years.getYears();
            if (age >= 15 && age <= 49) {
                List<Object> list = new ArrayList<>();
                list.add("true");
                event.addObs(new Obs("concept", "text", "162849AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "",
                        list, new ArrayList<>(), null, "wra"));
            }

        }

        return familyEventClient;
    }

    @Override
    public FamilyEventClient processFamilyRegistrationForm(String jsonString, String familyBaseEntityId) {
        return JsonFormUtils.processFamilyUpdateForm(FamilyLibrary.getInstance().context().allSharedPreferences(), jsonString, familyBaseEntityId);
    }


    private FormUtils getFormUtils() {
        if (formUtils == null) {
            try {
                formUtils = FormUtils.getInstance(Utils.context().applicationContext());
            } catch (Exception e) {
                Log.e(BaseFamilyProfileModel.class.getCanonicalName(), e.getMessage(), e);
            }
        }
        return formUtils;
    }

    public void setFormUtils(FormUtils formUtils) {
        this.formUtils = formUtils;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setFamilyHead(boolean familyHead) {
        this.familyHead = familyHead;
    }

    public void setPrimaryCaregiver(boolean primaryCaregiver) {
        this.primaryCaregiver = primaryCaregiver;
    }


}
