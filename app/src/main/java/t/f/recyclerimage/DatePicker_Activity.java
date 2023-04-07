package t.f.recyclerimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;

import utils.StatusBarLightModeUtil;

public class DatePicker_Activity extends Activity {

    private CalendarView calendarView ;
    private TextView current_day;

    private ImageView n_month;
    private ImageView p_month;

    private int year,month,day;
    private int [] currentDate;

    public static final String YEAR_KEY="year_key";
    public static final String MONTH_KEY="month_key";
    public static final String DAY_KEY="day_key";


    //https://github.com/SheHuan/CalendarView    控件地址

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datepicker);
        initView();
    }
    private void initView(){
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);

        current_day=(TextView)findViewById(R.id.current_day);

        n_month=(ImageView)findViewById(R.id.n_month);
        p_month=(ImageView)findViewById(R.id.p_month);

        currentDate=CalendarUtil.getCurrentDate();

        calendarView = (CalendarView) findViewById(R.id.calendar);

        year=currentDate[0];
        month=currentDate[1];
        day=currentDate[2];

        current_day.setText(year+"-"+month+"-"+day);

        //日历init，年月日之间用点号隔开
        calendarView.setStartEndDate(year+"."+month,(5+year)+"."+month)
                .setInitDate(year+"."+month)
                .setSingleDate(year+"."+month+"."+day)
                .init();

        //月份切换回调
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {

            }
        });
        //单选回调
        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean dateBean) {

//                for (int i=0;i<dateBean.getLunar().length;i++){
////                    Log.i("选中的日期",(dateBean.getLunar())[i]);
//                }
//                Log.i("选中的日期",dateBean.getTerm());
//                Log.i("选中的日期：",dateBean.getSolar());
//                Log.i("选中的日期：",dateBean.getSolarHoliday()+"");
                if (dateBean.getType() == 1) {
                    Log.i("当前选中的日期" , dateBean.getSolar()[0] + "年" + dateBean.getSolar()[1] + "月" + dateBean.getSolar()[2] + "日");

                    year=currentDate[0];
                    month=currentDate[1];
                    day=currentDate[2];
//                    if (dateBean.getSolar()[0]<currentDate[0]&&dateBean.getSolar()[1]<currentDate[1]&&dateBean.getSolar()[2]<currentDate[2]) {
//
//                        return;
//                    }
                }
                year=dateBean.getSolar()[0];
                month=dateBean.getSolar()[1];
                day=dateBean.getSolar()[2];

                current_day.setText(year+"-"+month+"-"+day);


                Intent intent=new Intent();
                intent.putExtra(YEAR_KEY,year);
                intent.putExtra(MONTH_KEY,month);
                intent.putExtra(DAY_KEY,day);
                setResult(RESULT_OK,intent);
                DatePicker_Activity.this.finish();
            }
        });

        p_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (month!=currentDate[0]&&year!=currentDate[0])
                    calendarView.lastMonth();
            }
        });
        /**
         * nextMonth()跳转到下个月 lastMonth()跳转到上个月
         */
        n_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (month!=currentDate[0]&&year!=(currentDate[0]+5)) {
                    calendarView.nextMonth();
                    Toast.makeText(DatePicker_Activity.this, "最多不能超过5年", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
