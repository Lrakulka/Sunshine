package json.data;

import com.task.krabiysok.sunshine.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KrabiySok on 2/24/2015.
 */
public class ItemsData {
    private ArrayList<String> itemsText;
    private ArrayList<Integer> itemsIcon;
    private HashMap<String, Integer> iconMap = new HashMap<String, Integer>() {{
        put("01d", R.drawable.d01);
        put("01n", R.drawable.d01n);
        put("02d", R.drawable.d02);
        put("02n", R.drawable.d02n);
        put("03d", R.drawable.d03);
        put("03n", R.drawable.d03n);
        put("04d", R.drawable.d04);
        put("04n", R.drawable.d04n);
        put("09d", R.drawable.d09);
        put("09n", R.drawable.d09n);
        put("10d", R.drawable.d10);
        put("10n", R.drawable.d10n);
        put("11d", R.drawable.d11);
        put("11n", R.drawable.d11n);
        put("13d", R.drawable.d13);
        put("13n", R.drawable.d13n);
        put("50d", R.drawable.d50);
        put("50n", R.drawable.d50n);
    }};

    public ArrayList<Integer> getIcon() {return itemsIcon;}
    public ArrayList<String> getText() {return itemsText;}
    public void addData(String text, String icon) {
        if (itemsText == null) itemsText = new ArrayList<>();
        itemsText.add(text);
        if (itemsIcon == null) itemsIcon = new ArrayList<>();
        itemsIcon.add(iconMap.get(icon));
    }
}
