package com.example.asaditasgourmet.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import ss.com.bannerslider.SlideType;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;
import com.example.asaditasgourmet.GlobalInfo;
/**
 * @author S.Shahini
 * @since 2/12/18
 */

public class MainSliderAdapter extends SliderAdapter {

    public static final String MY_PREFS_NAME = "MySession";
    public static final String banner1 = "banner1";
    public static final String banner2 = "banner2";
    public static final String banner3 = "banner3";
    Context context;
    String path = GlobalInfo.PATH_IP;


    public MainSliderAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {

        SharedPreferences shared =  context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String va1 = (shared.getString(banner1, ""));
        String va2 = (shared.getString(banner2, ""));
        String va3 = (shared.getString(banner3, ""));

        switch (position) {
            case 0:
                viewHolder.bindImageSlide(path+"pages/configuracion/upload/"+va1);
                break;
            case 1:
                viewHolder.bindImageSlide(path+"pages/configuracion/upload/"+va2);
                break;
            case 2:
                viewHolder.bindImageSlide(path+"pages/configuracion/upload/"+va3);
                break;
        }
    }
}
