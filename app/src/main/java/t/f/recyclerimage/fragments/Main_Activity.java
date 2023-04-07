package t.f.recyclerimage.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

import t.f.recyclerimage.R;
import utils.StatusBarLightModeUtil;

/**
 * Created by Friday on 2018/7/11.
 */

public class Main_Activity extends FragmentActivity {
    private Fragment[] fragments;
    private TextView[] textViews;
    private ImageView[] imageViews;
    private LinearLayout[] linearLayouts;
    private ViewPager vp;
    public static int FRAGEMENTS_COUNT=4;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    protected boolean isCreated = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        init();

        // 标记
        isCreated = true;

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();
        Pager_Adapter pager_adapter=new Pager_Adapter(fragmentManager);
        vp.setAdapter(pager_adapter);
    }

    private void init() {
        vp=(ViewPager)findViewById(R.id.main_viewpager);

        fragments=new Fragment[FRAGEMENTS_COUNT];
        fragments[0]=new Main_Fragment();
        fragments[1]=new FriendsCircle_Fragment();
        fragments[2]=new Trade_Fragment();
        fragments[3]=new Mine_Fragment();

        linearLayouts=new LinearLayout[FRAGEMENTS_COUNT];
        linearLayouts[0]=(LinearLayout)findViewById(R.id.main);
        linearLayouts[1]=(LinearLayout)findViewById(R.id.friend_circle);
        linearLayouts[2]=(LinearLayout)findViewById(R.id.trade);
        linearLayouts[3]=(LinearLayout)findViewById(R.id.mine);

        textViews=new TextView[FRAGEMENTS_COUNT];
        textViews[0]=(TextView)findViewById(R.id.txt_main);
        textViews[1]=(TextView)findViewById(R.id.txt_friend_circle);
        textViews[2]=(TextView)findViewById(R.id.txt_trade);
        textViews[3]=(TextView)findViewById(R.id.txt_mine);

        imageViews=new ImageView[FRAGEMENTS_COUNT];
        imageViews[0]=(ImageView)findViewById(R.id.img_main);
        imageViews[1]=(ImageView)findViewById(R.id.img_friend_circle);
        imageViews[2]=(ImageView)findViewById(R.id.img_trade);
        imageViews[3]=(ImageView)findViewById(R.id.img_mine);

        vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<FRAGEMENTS_COUNT;i++){
                    linearLayouts[i].setClickable(true);
                    textViews[i].setTextColor(getResources().getColor(R.color.no_selected_color));

                    imageViews[0].setImageResource(R.mipmap.main_no_select);
                    imageViews[1].setImageResource(R.mipmap.friend_c_no_select);
                    imageViews[2].setImageResource(R.mipmap.fuli_she_no_select);
                    imageViews[3].setImageResource(R.mipmap.mine_no_select);
                }
                linearLayouts[position].setClickable(false);
                textViews[position].setTextColor(getResources().getColor(R.color.selected_color));

                switch (position){
                    case 0:
                        imageViews[0].setImageResource(R.mipmap.main);
                        imageViews[1].setImageResource(R.mipmap.friend_c_no_select);
                        imageViews[2].setImageResource(R.mipmap.fuli_she_no_select);
                        imageViews[3].setImageResource(R.mipmap.mine_no_select);
                        break;
                    case 1:
                        imageViews[1].setImageResource(R.mipmap.friend_c);
                        imageViews[2].setImageResource(R.mipmap.fuli_she_no_select);
                        imageViews[3].setImageResource(R.mipmap.mine_no_select);
                        imageViews[0].setImageResource(R.mipmap.main_no_select);
                        break;
                    case 2:
                        imageViews[2].setImageResource(R.mipmap.fuli_she);
                        imageViews[0].setImageResource(R.mipmap.main_no_select);
                        imageViews[1].setImageResource(R.mipmap.friend_c_no_select);
                        imageViews[3].setImageResource(R.mipmap.mine_no_select);
                        break;
                    case 3:
                        imageViews[3].setImageResource(R.mipmap.mine);
                        imageViews[0].setImageResource(R.mipmap.main_no_select);
                        imageViews[1].setImageResource(R.mipmap.friend_c_no_select);
                        imageViews[2].setImageResource(R.mipmap.fuli_she_no_select);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        for(int i=0;i<FRAGEMENTS_COUNT;i++){
            linearLayouts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.main:
                            vp.setCurrentItem(0);
                            linearLayouts[0].setClickable(false);
                            textViews[0].setTextColor(getResources().getColor(R.color.selected_color));
                            imageViews[0].setImageResource(R.mipmap.main);
                            for(int j=1;j<FRAGEMENTS_COUNT;j++){
                                linearLayouts[j].setClickable(true);
                                textViews[j].setTextColor(getResources().getColor(R.color.no_selected_color));
                            }
                            imageViews[1].setImageResource(R.mipmap.friend_c_no_select);
                            imageViews[2].setImageResource(R.mipmap.fuli_she_no_select);
                            imageViews[3].setImageResource(R.mipmap.mine_no_select);
                            break;
                        case R.id.friend_circle:
                            vp.setCurrentItem(1);
                            linearLayouts[1].setClickable(false);
                            textViews[1].setTextColor(getResources().getColor(R.color.selected_color));
                            imageViews[1].setImageResource(R.mipmap.friend_c);
                            for(int j=2;j<FRAGEMENTS_COUNT;j++){
                                linearLayouts[j].setClickable(true);
                                textViews[j].setTextColor(getResources().getColor(R.color.no_selected_color));
                            }
                            imageViews[2].setImageResource(R.mipmap.fuli_she_no_select);
                            imageViews[3].setImageResource(R.mipmap.mine_no_select);
                            linearLayouts[0].setClickable(true);
                            textViews[0].setTextColor(getResources().getColor(R.color.no_selected_color));
                            imageViews[0].setImageResource(R.mipmap.main_no_select);
                            break;
                        case R.id.trade:
                            vp.setCurrentItem(2);
                            textViews[2].setTextColor(getResources().getColor(R.color.selected_color));
                            imageViews[2].setImageResource(R.mipmap.fuli_she);
                            for(int j=0;j<2;j++){
                                linearLayouts[j].setClickable(true);
                                textViews[j].setTextColor(getResources().getColor(R.color.no_selected_color));
                            }
                            imageViews[0].setImageResource(R.mipmap.main_no_select);
                            imageViews[1].setImageResource(R.mipmap.friend_c_no_select);
                            linearLayouts[3].setClickable(true);
                            textViews[3].setTextColor(getResources().getColor(R.color.no_selected_color));
                            imageViews[3].setImageResource(R.mipmap.ic_launcher);
                            break;
                        case R.id.mine:
                            vp.setCurrentItem(3);
                            linearLayouts[3].setClickable(false);
                            textViews[3].setTextColor(getResources().getColor(R.color.selected_color));
                            imageViews[3].setImageResource(R.mipmap.mine);
                            for(int j=0;j<3;j++){
                                linearLayouts[j].setClickable(true);
                                textViews[j].setTextColor(getResources().getColor(R.color.no_selected_color));
                            }
                            imageViews[0].setImageResource(R.mipmap.main_no_select);
                            imageViews[1].setImageResource(R.mipmap.friend_c_no_select);
                            imageViews[2].setImageResource(R.mipmap.fuli_she_no_select);
                            break;
                    }
                }
            });
        }
        linearLayouts[0].setClickable(false);
        textViews[0].setTextColor(getResources().getColor(R.color.selected_color));
        imageViews[0].setImageResource(R.mipmap.main);
    }
    class Pager_Adapter extends FragmentPagerAdapter {

        public Pager_Adapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        overridePendingTransition(R.anim.out_actitity, R.anim.in_actitity);// 淡出淡入动画效果
    }
}
