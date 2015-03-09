package com.task.krabiysok.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import json.data.ItemsData;
import json.data.JSONData;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private static final String LIST_ITEM_TEXT = "text";
    private static final String LIST_ITEM_ICON = "icon";
    private static final int[] LIST_VIEWS = {R.id.list_item_forecast_textview, R.id.weather_icon};
    private static final String[] PARAMS = {LIST_ITEM_TEXT, LIST_ITEM_ICON};
    private MySimpleAdapter adapter;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> weekForecast = new ArrayList<>(Arrays.asList(getResources().
                getStringArray(R.array.fake_data)));

        adapter = new MySimpleAdapter(getActivity(), new ArrayList<Map<String, Object>>(),
                R.layout.list_item_forecast, PARAMS, LIST_VIEWS);
        adapter.addAll(weekForecast.toArray());
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class).
                        putExtra(Intent.EXTRA_TEXT,
                                (String) ((HashMap<String, Object>) adapter.
                                        getItem(position)).get(LIST_ITEM_TEXT));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void doShare(Intent shareIntent) {
        // When you want to share set the share intent.
        mShareActionProvider.setShareIntent(shareIntent);
    }

    private void updateWeather() {
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
        SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(getActivity());
        String location = sharedPref.getString(getResources().
                        getString(R.string.pref_location_key), getResources().
                        getString(R.string.pref_location_default));
        String tempSystem = sharedPref.getString(getResources().
                getString(R.string.pref_temperature_system_key), getResources().
                getString(R.string.pref_temperature_system_default));
        fetchWeatherTask.execute(location, tempSystem);
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, ItemsData> {
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected ItemsData doInBackground(String... params) {
            final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String PARAM_IDENT = "q";
            final String PARAM_MODE = "mode";
            final String PARAM_UNITS = "units";
            final String PARAM_CNT = "cnt";
            String mode = "json";
            String cnt = getResources().getString(R.string.cnt_days);
            Uri.Builder uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PARAM_IDENT, params[0]).
                    appendQueryParameter(PARAM_MODE, mode).
                    appendQueryParameter(PARAM_UNITS, params[1]).appendQueryParameter(PARAM_CNT, cnt);
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(uri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }try {
                if (forecastJsonStr == null) {
                    return null;
                } else if (forecastJsonStr.contains("\"cod\":\"404\"")) {
                            return new ItemsData();
                } else return JSONData.getWeatherDataFromJson(forecastJsonStr).dayInfoArrayItems();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ItemsData items) {
            super.onPostExecute(items);
            if (items != null) {
                if (!items.getText().isEmpty()) {
                    adapter.clear();
                    adapter.addAll(items.getText().toArray(), items.getIcon().toArray());
                } else if (isAdded()) {
                            Toast.makeText(getActivity(),
                                getResources().getString(R.string.wrong_settings),
                                Toast.LENGTH_LONG).show();
                        } else Log.e("ERROR", "Fragment not attached to Activity");
            } else {
                if (isAdded()) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.no_internet_conection),
                            Toast.LENGTH_LONG).show();
                } else Log.e("ERROR", "Fragment not attached to Activity");
            }
        }
    }
}
