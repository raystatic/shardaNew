package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shardatech.shardauniversity.R;

/**
 * Created by rahul on 8/5/18.
 */

public class SliderViewPagerAdapter extends PagerAdapter {

    Context context;
    int[] layouts={R.layout.main_slider_layout_1,R.layout.main_slider_layout_2,R.layout.main_slider_layout_3,R.layout.main_slider_layout_4,R.layout.main_slider_layout_5,R.layout.main_slider_layout_6,R.layout.main_slider_layout_7,R.layout.main_slider_layout_8,R.layout.main_slider_layout_9};

    public SliderViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        ViewGroup layout= (ViewGroup) layoutInflater.inflate(layouts[position],container,false);
        container.addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
