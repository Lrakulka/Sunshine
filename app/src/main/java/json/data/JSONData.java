package json.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by KrabiySok on 2/23/2015.
 */
public class JSONData {
    private CityInfo mCityInfo;
    private ArrayList<DayInfo> mDayInfo;

    public static JSONData getWeatherDataFromJson(String forecastJsonStr)
            throws JSONException {
        final String DATE_TIME = "dt";
        final String CITY = "city";
        final String CITY_NAME = "name";
        final String CITY_COUNTR = "country";
        final String CITY_POPUL = "population";
        final String CITY_COORD = "coord";
        final String CITY_COORD_LON = "lon";
        final String CITY_COORD_LAT = "lat";

        final String DAYS_LIST = "list";
        final String DAY_PRESSURE = "pressure";
        final String DAY_HUMIDITY = "humidity";
        final String DAY_WIND_SPEED = "speed";
        final String DAY_WIND_DEGREE = "deg";
        final String DAY_CLOUDS = "clouds";
        final String DAY_TEMPER = "temp";
        final String DAY_TEMPER_IN_DAY = "day";
        final String DAY_TEMPER_IN_NIGHT = "night";
        final String DAY_TEMPER_IN_EVEN = "eve";
        final String DAY_TEMPER_IN_MORN = "morn";
        final String DAY_TEMPER_MIN = "min";
        final String DAY_TEMPER_MAX = "max";
        final String DAY_WEATHER = "weather";
        final String DAY_WEATHER_MAIN = "main";
        final String DAY_WEATHER_DESCRIP = "description";
        final String DAY_WEATHER_ICON = "icon";

        String[] dateArray = null;
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(DAYS_LIST);
        JSONData result = new JSONData();
        DayInfo dayInfo = null;
        JSONObject jsonObject;
        //------City info
        JSONObject cityInfo = forecastJson.getJSONObject(CITY);
        result.mCityInfo = new CityInfo();
        result.mCityInfo.name = cityInfo.getString(CITY_NAME);
        result.mCityInfo.country = cityInfo.getString(CITY_COUNTR);
        result.mCityInfo.population = cityInfo.getInt(CITY_POPUL);
        jsonObject = cityInfo.getJSONObject(CITY_COORD);
        result.mCityInfo.lat = jsonObject.getDouble(CITY_COORD_LAT);
        result.mCityInfo.lon = jsonObject.getDouble(CITY_COORD_LON);

        //----Days info
        JSONObject jsonObDay;
        for(int i = 0; i < weatherArray.length(); i++) {
            jsonObDay = weatherArray.getJSONObject(i);
            dayInfo = new DayInfo();
            dayInfo.clouds = (byte) jsonObDay.getInt(DAY_CLOUDS);
            dayInfo.humidity = (byte) jsonObDay.getInt(DAY_HUMIDITY);
            // Tag weather is array with one item!
            jsonObject = jsonObDay.getJSONArray(DAY_WEATHER).getJSONObject(0);
            dayInfo.description = jsonObject.getString(DAY_WEATHER_DESCRIP);
            dayInfo.icon = jsonObject.getString(DAY_WEATHER_ICON);
            dayInfo.main = jsonObject.getString(DAY_WEATHER_MAIN);
            dayInfo.pressure = jsonObDay.getDouble(DAY_PRESSURE);
            dayInfo.speed = jsonObDay.getDouble(DAY_WIND_SPEED);
            dayInfo.degree = jsonObDay.getDouble(DAY_WIND_DEGREE);
            dayInfo.dt = jsonObDay.getLong(DATE_TIME);
            jsonObject = jsonObDay.getJSONObject(DAY_TEMPER);
            dayInfo.temperature.day = jsonObject.getDouble(DAY_TEMPER_IN_DAY);
            dayInfo.temperature.evening = jsonObject.getDouble(DAY_TEMPER_IN_EVEN);
            dayInfo.temperature.max = jsonObject.getDouble(DAY_TEMPER_MAX);
            dayInfo.temperature.min = jsonObject.getDouble(DAY_TEMPER_MIN);
            dayInfo.temperature.morning = jsonObject.getDouble(DAY_TEMPER_IN_MORN);
            dayInfo.temperature.night = jsonObject.getDouble(DAY_TEMPER_IN_NIGHT);
            result.putDayInfo(dayInfo);
        }
        return result;
    }

    public ItemsData dayInfoArrayItems() {
        ItemsData itemsData = new ItemsData();
        for(DayInfo dayInfo : mDayInfo) {
            itemsData.addData(getReadableDateString(dayInfo.dt)  + " - " +
                    dayInfo.description + " - " + dayInfo.temperature.max +
                    "/" + dayInfo.temperature.min, dayInfo.icon);
        }
        return itemsData;
    }

    private String getReadableDateString(long time){
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time * 1000);
    }

    public void putDayInfo(DayInfo dayInfo) {
        if (mDayInfo == null) mDayInfo = new ArrayList<>();
        mDayInfo.add(dayInfo);
    }

    public CityInfo getmCityInfo() {
        return mCityInfo;
    }

    public void setmCityInfo(CityInfo mCityInfo) {
        this.mCityInfo = mCityInfo;
    }
}
