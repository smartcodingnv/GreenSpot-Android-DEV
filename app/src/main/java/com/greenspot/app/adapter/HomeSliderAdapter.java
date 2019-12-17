package com.greenspot.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.greenspot.app.R;
import com.greenspot.app.responce.home.ResponeHome;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeSliderAdapter extends SliderViewAdapter<HomeSliderAdapter.HomeSliderVH> {

    private Context context;
    List<ResponeHome.DataBean.BannerBean> bannerBeanArrayList;

    public HomeSliderAdapter(Context context, ArrayList<ResponeHome.DataBean.BannerBean> bannerList) {
        this.context = context;
        this.bannerBeanArrayList = bannerList;
    }

    @Override
    public HomeSliderVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_slider_layout_item, null);
        return new HomeSliderVH(inflate);
    }

    @Override
    public void onBindViewHolder(HomeSliderVH viewHolder, int position) {
//        viewHolder.textViewDescription.setText("This is slider item " + position);


        final ResponeHome.DataBean.BannerBean entity = bannerBeanArrayList.get(position);

        Glide.with(context)
                .load(entity.getImage())
                .placeholder(R.drawable.background)
                .centerCrop()
                .into(viewHolder.imageViewBackground);

 /*       switch (position) {
            case 0:
//                Glide.with(viewHolder.itemView)
//                        .load("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
//                        .fitCenter()
//                        .into(new LinearLayoutTarget(this.getApplicationContext(), (LinearLayout) yourLinearLayoutInstanceHere)));
                Glide.with(context)
                        .load("https://images.pexels.com/photos/95425/pexels-photo-95425.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260")
                        .centerCrop()
                        .into(viewHolder.imageViewBackground);
//                Glide.with(viewHolder.itemView)
//                        .load("https://images.pexels.com/photos/95425/pexels-photo-95425.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260")
//                        .into(new CustomTarget<Drawable>() {
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                viewHolder.mLinayerlayout.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });
                break;
            case 1:
                Glide.with(context)
                        .load("https://images.pexels.com/photos/2733918/pexels-photo-2733918.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
                        .centerCrop()
                        .into(viewHolder.imageViewBackground);
//                Glide.with(viewHolder.itemView)
//                        .load("https://images.pexels.com/photos/2733918/pexels-photo-2733918.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
//
//                        .into(new CustomTarget<Drawable>() {
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                viewHolder.mLinayerlayout.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });


                break;
            case 2:

                Glide.with(context)
                        .load("https://images.pexels.com/photos/128402/pexels-photo-128402.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
                        .centerCrop()
                        .into(viewHolder.imageViewBackground);

//                Glide.with(viewHolder.itemView)
//                        .load("https://images.pexels.com/photos/128402/pexels-photo-128402.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
//
//                        .into(new CustomTarget<Drawable>() {
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                viewHolder.mLinayerlayout.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });


                break;
            default:
                Glide.with(context)
                        .load("https://images.pexels.com/photos/1400172/pexels-photo-1400172.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
                        .centerCrop()
                        .into(viewHolder.imageViewBackground);


//                Glide.with(viewHolder.itemView)
//                        .load("https://images.pexels.com/photos/1400172/pexels-photo-1400172.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
//                        .centerCrop()
//                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                        .into(new CustomTarget<Drawable>() {
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                viewHolder.mLinayerlayout.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });


                break;

        }*/

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return bannerBeanArrayList.size();
    }

    class HomeSliderVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public HomeSliderVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.img_slider);
            this.itemView = itemView;
        }
    }
}