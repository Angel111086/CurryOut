package com.curryout;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {
    private Context context;
    private List<ImageSliderObject> imageSliderObjectsList;
    private LayoutInflater layoutInflater;
    public SliderPagerAdapter(Context context, List<ImageSliderObject> imageSliderObjectsList) {
        this.context = context;
        this.imageSliderObjectsList = imageSliderObjectsList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public SliderPagerAdapter(FragmentActivity supportFragmentManager) {

    }

    @Override
    public int getCount() {
        return imageSliderObjectsList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.image_slider_layout, container, false);
        ImageSliderObject imgSliderObject = imageSliderObjectsList.get(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.im_slider);
        imageView.setImageResource(imgSliderObject.getImgId());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(context, ImageZoomActivity.class);
//                context.startActivity(i);
            }
        });
        container.addView(view);
        return view;
    }
}
