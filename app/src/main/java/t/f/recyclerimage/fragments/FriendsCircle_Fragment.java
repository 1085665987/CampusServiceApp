package t.f.recyclerimage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import Adapters.Pager_Adapter;
import t.f.recyclerimage.R;
import t.f.recyclerimage.UploadForum_Activity;
import t.f.recyclerimage.say_fragments.AllSchool_Fragment;
import t.f.recyclerimage.say_fragments.Attention_SB_Fragment;
import t.f.recyclerimage.say_fragments.ThisSchool_Fragment;

/**
 * Created by Friday on 2018/7/11.
 */

public class FriendsCircle_Fragment extends Fragment {
    private ViewPager viewPager;
    private TextView[] items;
    private ImageView write;
    private View view,tabLine;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment[] fragments;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {view = inflater.inflate(R.layout.friends_circle, null);}
        initView();
        setOnClick();
        fragmentManager=getChildFragmentManager();

        Pager_Adapter pager_adapter=new Pager_Adapter(fragmentManager,fragments);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();
        viewPager.setAdapter(pager_adapter);
        return view;
    }

    private void initView() {

        fragments=new Fragment[3];
        items=new TextView[3];

        items[0]=(TextView) view.findViewById(R.id.this_school);
        items[1]=(TextView) view.findViewById(R.id.all_school);
        items[2]=(TextView) view.findViewById(R.id.interesting);

        write=(ImageView)view.findViewById(R.id.wirte_say);

        viewPager=(ViewPager) view.findViewById(R.id.say_vp);


        fragments[0]=new ThisSchool_Fragment();
        fragments[1]=new AllSchool_Fragment();
        fragments[2]=new Attention_SB_Fragment();


        tabLine=(View)view.findViewById(R.id.page_tab_line);
    }
    private void setOnClick(){

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(),"进入新界面",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), UploadForum_Activity.class);
                getActivity().startActivity(intent);
            }
        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams)tabLine.getLayoutParams();
                layoutParams.leftMargin=(int)((position+positionOffset)*tabLine.getWidth());
                tabLine.setLayoutParams(layoutParams);
            }
            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<3;i++){
                    items[i].setClickable(true);
                    items[i].setTextColor(getResources().getColor(R.color.black));
                }
                items[position].setTextColor(getResources().getColor(R.color.selected_color));
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        for (int i=0;i<3;i++){
            items[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.this_school:
                            viewPager.setCurrentItem(0);
                            for(int i=0;i<3;i++){
                                items[i].setClickable(true);
                                items[i].setTextColor(getResources().getColor(R.color.black));
                            }
                            items[0].setClickable(false);
                            items[0].setTextColor(getResources().getColor(R.color.selected_color));
                            break;
                        case R.id.all_school:
                            viewPager.setCurrentItem(1);
                            for(int i=0;i<3;i++){
                                items[i].setClickable(true);
                                items[i].setTextColor(getResources().getColor(R.color.black));
                            }
                            items[1].setClickable(false);
                            items[1].setTextColor(getResources().getColor(R.color.selected_color));
                            break;
                        case R.id.interesting:
                            viewPager.setCurrentItem(2);
                            for(int i=0;i<3;i++){
                                items[i].setClickable(true);
                                items[i].setTextColor(getResources().getColor(R.color.black));
                            }
                            items[2].setClickable(false);
                            items[2].setTextColor(getResources().getColor(R.color.selected_color));
                            break;
                    }
                }
            });
        }
    }
}
