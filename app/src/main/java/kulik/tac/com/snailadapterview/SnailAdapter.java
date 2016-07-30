package kulik.tac.com.snailadapterview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by kulik on 29.07.16.
 */
public class SnailAdapter extends BaseAdapter {

    private final Context mCtx;
    int[] colors = new int[]{
            android.R.color.black,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_orange_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_dark,
            android.R.color.holo_green_light,
            android.R.color.holo_red_light,
            android.R.color.holo_red_dark,
            android.R.color.tertiary_text_dark,
            android.R.color.darker_gray,
            android.R.color.holo_blue_bright,
            android.R.color.holo_purple

    };

    public SnailAdapter(Context ctx) {
        super();
        mCtx = ctx;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int i) {
        return colors[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mCtx, R.layout.temp_item, null);
        }
        TextView v = (TextView) view.findViewById(R.id.tv);
        v.setBackgroundColor(mCtx.getResources().getColor(colors[i]));
        v.setText(String.valueOf(i));

        return v;
    }
}
