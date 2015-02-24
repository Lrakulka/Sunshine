package com.task.krabiysok.sunshine;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KrabiySok on 2/24/2015.
 */
public class MySimpleAdapter extends SimpleAdapter {
    List<Map<String, Object>> data;
    Map<String, Object> m;
    String[] from;
    int[] to;
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public MySimpleAdapter(Context context, List<Map<String, Object>> data, int resource,
                           String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data = data;
        this.from = from;
        this.to = to;
    }

    public void clear() {
        data.removeAll(data);
    }

    public void addAll(Object[] textItems, Object[] iconItems) {
        for(int i = 0; i < textItems.length; i++) {
            m = new HashMap<>();
            m.put(from[0], textItems[i]);
            m.put(from[1], iconItems[i]);
            data.add(m);
        }
        notifyDataSetChanged();
    }

    public void addAll(Object[] textItems) {
        for(int i = 0; i < textItems.length; i++) {
            m = new HashMap<>();
            m.put(from[0], textItems[i]);
            data.add(m);
        }
        notifyDataSetChanged();
    }
}
