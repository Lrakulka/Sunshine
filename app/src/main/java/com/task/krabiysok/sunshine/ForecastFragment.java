package com.task.krabiysok.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    ActionMode actionMode;
    private ActionMode.Callback callback = new ActionMode.Callback() {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.forecastfragment, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_refresh) {
                FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
                fetchWeatherTask.execute("696877");
                return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }

    };

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        actionMode = getActivity().startActionMode(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actionMode.finish();
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
        return rootView;
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            fetchWeatherTask.execute("696877");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public class FetchWeatherTask extends AsyncTask<String, Void, ItemsData> {
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected ItemsData doInBackground(String... params) {
            final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String PARAM_IDENT = "id";
            final String PARAM_MODE = "mode";
            final String PARAM_UNITS = "units";
            final String PARAM_CNT = "cnt";
            String mode = "json";
            String units = "metric";
            String cnt = getResources().getString(R.string.cnt_days);
            Uri.Builder uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PARAM_IDENT, params[0]).
                    appendQueryParameter(PARAM_MODE, mode).
                    appendQueryParameter(PARAM_UNITS, units).appendQueryParameter(PARAM_CNT, cnt);
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
                return JSONData.getWeatherDataFromJson(forecastJsonStr).dayInfoArrayItems();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ItemsData items) {
            super.onPostExecute(items);
            if (items != null && !items.getText().isEmpty()) {
                adapter.clear();
                adapter.addAll(items.getText().toArray(), items.getIcon().toArray());
            }
        }
    }
}
