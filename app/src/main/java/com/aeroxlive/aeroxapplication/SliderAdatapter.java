package com.aeroxlive.aeroxapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.aeroxlive.aeroxapplication.R;

public class SliderAdatapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdatapter(Context context){
        this.context = context;
    }

    int images[] = {
            R.raw.uploading,
            R.raw.payment,
            R.raw.printing

    };

    int headings[] = {
            R.string.firest_slide_title,
            R.string.second_slide_title,
            R.string.third_slide_title
    };

    int desc[] = {
            R.string.firestdesc,
            R.string.secdesc,
            R.string.thirddesc

    };




    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout,container,false);
        LottieAnimationView lottieAnimationView  = view.findViewById(R.id.animationpic);
        TextView heading = view.findViewById(R.id.sliderheader);
        TextView des = view.findViewById(R.id.sliderdesc);

        lottieAnimationView.setAnimation(images[position]);
        heading.setText(headings[position]);
        des.setText(desc[position]);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
