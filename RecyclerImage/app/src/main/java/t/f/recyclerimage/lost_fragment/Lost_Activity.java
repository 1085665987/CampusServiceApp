package t.f.recyclerimage.lost_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import Adapters.Pager_Adapter;
import t.f.recyclerimage.R;

public class Lost_Activity extends FragmentActivity implements View.OnClickListener{

    private Fragment lost_fragment;
    private Fragment found_fragment;
    private ViewPager viewPager;

    private TextView lost_txt,found_txt;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_and_found);
        initViews();
    }

    private void initViews(){
        viewPager=(ViewPager)findViewById(R.id.lost_or_found_viewpager);

        lost_txt=(TextView)findViewById(R.id.lost_txt) ;
        found_txt=(TextView)findViewById(R.id.found_txt);

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();

        lost_fragment=new Lost_Fragment();
        found_fragment=new Found_Fragment();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        lost_txt.setTextColor(getResources().getColor(R.color.cursor_color));
                        found_txt.setTextColor(getResources().getColor(R.color.no_selected_color));
                        break;
                    case 1:
                        found_txt.setTextColor(getResources().getColor(R.color.cursor_color));
                        lost_txt.setTextColor(getResources().getColor(R.color.no_selected_color));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Pager_Adapter pager_adapter=new Pager_Adapter(fragmentManager,lost_fragment,found_fragment);
        viewPager.setAdapter(pager_adapter);

        lost_txt.setTextColor(getResources().getColor(R.color.cursor_color));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lost_txt:
                viewPager.setCurrentItem(0);
                lost_txt.setTextColor(getResources().getColor(R.color.cursor_color));
                found_txt.setTextColor(getResources().getColor(R.color.no_selected_color));
                break;
            case R.id.found_txt:
                viewPager.setCurrentItem(1);
                found_txt.setTextColor(getResources().getColor(R.color.cursor_color));
                lost_txt.setTextColor(getResources().getColor(R.color.no_selected_color));
                break;
        }
    }
}
