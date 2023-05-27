package esme.fr.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class DonneeMeteo {
    private String mTemperature, mIcon, mVille, mTypeMeteo;
    private int mCondition;

    public static DonneeMeteo fromJson(JSONObject jsonObject){
        try
        {
            DonneeMeteo dMeteo = new DonneeMeteo();
            dMeteo.mVille=jsonObject.getString("name");
            dMeteo.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            dMeteo.mTypeMeteo=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            dMeteo.mIcon=updateWeatherIcon(dMeteo.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int valeurArrondie=(int)Math.rint(tempResult);
            dMeteo.mTemperature=Integer.toString(valeurArrondie);
            return dMeteo;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition){

        if(condition>=0 && condition<=300){
            return "tempete";
        }
        else if(condition>=300 && condition<=500){
            return "pluvieux";
        }
        else if(condition>=500 && condition<=600){
            return "pluie";
        }
        else if(condition>=600 && condition<=700){
            return "neige";
        }
        else if(condition>=701 && condition<=771){
            return "nuageux2";
        }
        else if(condition>=772 && condition<=800){
            return "nuage";
        }
        else if(condition == 800){
            return "soleil";
        }
        else if(condition>=801 && condition<=804){
            return "nuageux";
        }
        else if(condition>=900 && condition<=902){
            return "tempete";
        }
        else if(condition == 903){
            return "neige1";
        }
        else if(condition == 904){
            return "soleil";
        }
        else if(condition>=905 && condition<=1000){
            return "tempete1";
        }
        return "dunno";

    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmVille() {
        return mVille;
    }

    public String getmTypeMeteo() {
        return mTypeMeteo;
    }
}
