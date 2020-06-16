package com.example.pqq.indicatordemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.pqq.indicatordemo.view.beizer.BeizerView;
import com.example.pqq.indicatordemo.view.beizer.BeizerViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BeizerView beizerView;
    private BeizerViewPager viewPager;
    private List<Object> imgList;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beizerView = (BeizerView) findViewById(R.id.beizer_view);
        viewPager = (BeizerViewPager) findViewById(R.id.viewPager);
        imageView = (ImageView) findViewById(R.id.imgview);

        initImgList();
        beizerView.setCount(imgList.size());
        beizerView.setOnSelectListener(new BeizerView.OnSelectListener() {
            @Override
            public void onSelect(int pos) {
                if (viewPager != null) {
                    viewPager.setCurrentItem(pos);
                }

            }

            @Override
            public void onAnim(boolean isAnim) {
                if (isAnim) {
                    viewPager.setCanTouch(false);
                } else {
                    viewPager.setCanTouch(true);
                }
            }
        });
        viewPager.setAdapter(new MyPagerAdapter(this, imgList));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled", "position:  " + position +
                        "\n positionOffset   " + positionOffset +
                        "\n  positionOffsetPixels   " + positionOffsetPixels);
                beizerView.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected", "position:  " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("onPageStateChanged", "state:  " + state);
            }
        });
    }


    public void initImgList() {
        imgList = new ArrayList<>();
        imgList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2946550071,381041431&fm=11&gp=0.jpg");
        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490984320392&di=8290126f83c2a2c0d45be41e3f88a6d0&imgtype=0&src=http%3A%2F%2Ffile.mumayi.com%2Fforum%2F201307%2F19%2F152440r9ov9ololkzdcz7d.jpg");
        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490984407478&di=729b187f4939710e8b2436f9f1306dff&imgtype=0&src=http%3A%2F%2Ffile.mumayi.com%2Fforum%2F201505%2F05%2F172352jrr66rda0dwdwdwz.jpg");
        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490984407478&di=729b187f4939710e8b2436f9f1306dff&imgtype=0&src=http%3A%2F%2Ffile.mumayi.com%2Fforum%2F201505%2F05%2F172352jrr66rda0dwdwdwz.jpg");
        imgList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490984407478&di=729b187f4939710e8b2436f9f1306dff&imgtype=0&src=http%3A%2F%2Ffile.mumayi.com%2Fforum%2F201505%2F05%2F172352jrr66rda0dwdwdwz.jpg");

    }

    public class MyPagerAdapter extends PagerAdapter {
        private List<Object> list;
        private Context context;
        private LayoutInflater mLayoutInflater;

        public MyPagerAdapter(Context context, List<Object> list) {
            this.context = context;
            this.list = list;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.pager_item_layout, container, false);
            container.addView(view);
            ImageView img = (ImageView) view.findViewById(R.id.img);
            Glide.with(context).load(list.get(position))
                    .placeholder(R.mipmap.ic_launcher).into(img);
            return view;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
