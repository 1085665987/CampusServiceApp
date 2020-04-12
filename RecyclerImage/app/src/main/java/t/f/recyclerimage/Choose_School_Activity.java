package t.f.recyclerimage;

import android.Manifest;
import android.app.Activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infos.UserInfo;
import okhttp3.Call;
import utils.LimitEdit;
import utils.Property;
import utils.StatusBarLightModeUtil;

/**
 * Created by Friday on 2018/7/28.
 */

public class Choose_School_Activity extends Activity implements OnGetSuggestionResultListener ,OnGetPoiSearchResultListener {
//    实现定位接口

    private ImageView back;         //返回键
    private EditText edit_school;   //学校的输入框
    private ListView listView;      //显示的学校列表
    private LinearLayout orientation;   //用来定位的LinearLayout 的按钮

    private SuggestionSearch mSuggestionSearch = null;
    private SimpleAdapter simpleAdapter=null;

    private String this_city;
    LatLng center = new LatLng(39.92235, 116.380338);
    int radius = 100000;
    // 定位相关
    LocationClient mLocClient;
    private MyLocationListenner myListener ;
    private static final int BAIDU_READ_PHONE_STATE =100;


    private static String TAG="Choose_School_Activity";
    private ArrayList<Map<String,String>> suggest,mPoiResult;

    private PoiSearch mPoiSearch = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.choose_school);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        initViews();
        setOnClick();
    }

    private void initViews() {
        back=(ImageView)findViewById(R.id.back);
        orientation=(LinearLayout)findViewById(R.id.orientation);
        edit_school=(EditText)findViewById(R.id.school_edit) ;
        edit_school.addTextChangedListener(textWatcher);

        listView=(ListView)findViewById(R.id.school_list);

        if(this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions( new String[]{ Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },BAIDU_READ_PHONE_STATE );
        }

        // 定位初始化
        myListener = new MyLocationListenner();

        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.restart();

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

    }
    private void setOnClick(){

        Log.i(TAG,edit_school.getText().toString().trim());
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="?phone="+new UserInfo(Choose_School_Activity.this).getLongInfo(Property.NO)+"&school_name="+edit_school.getText().toString().trim();
                url=getResources().getString(R.string.select_school)+url;
                final String finalUrl = url;
                OkHttpUtils.get().url(finalUrl).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i(TAG,response);
                        if(response.toString().trim().equals("1")){
                            Toast.makeText(Choose_School_Activity.this,"绑定成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        if(suggest!=null&&suggest.size()!=0)
            return;
        suggest = new ArrayList();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                Map <String ,String>map =new HashMap<>();
                map.put("school",info.key);
                suggest.add(map);
            }
        }
        simpleAdapter = new SimpleAdapter(Choose_School_Activity.this,
                suggest,
                R.layout.school_item,
                new String[]{"school"}, new int[]{R.id.school_txt});
        listView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(Choose_School_Activity.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        if(mPoiResult!=null&&mPoiResult.size()!=0)
            return;
        mPoiResult=new ArrayList<>();

        List<PoiInfo> adds = result.getAllPoi();
        for(PoiInfo p:adds){
            Map<String,String> map=new HashMap<>();
            map.put("school",p.name);
            mPoiResult.add(map);
        }
        simpleAdapter = new SimpleAdapter(Choose_School_Activity.this,
                mPoiResult,
                R.layout.school_item,
                new String[]{"school"}, new int[]{R.id.school_txt});
        listView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        private String addr,city,country,province,district,street;

        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            addr = location.getAddrStr();    //获取详细地址信息
            country = location.getCountry();    //获取国家
            province = location.getProvince();    //获取省份
            city = location.getCity();    //获取城市
            district = location.getDistrict();    //获取区县
            street = location.getStreet();    //获取街道信息

            if(this_city==null)
                this_city=city;

            /**
             * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
             */
//            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword("学校").city(city));
            Log.i(TAG,"城市名："+city);

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            center = new LatLng(latitude, longitude);
            Log.i("经度：" , longitude+"");
            Log.i("纬度：",latitude+"");
            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword("学校"
                    .toString()).sortType(PoiSortType.distance_from_near_to_far).location(center)
                    .radius(radius).pageNum(0);
            mPoiSearch.searchNearby(nearbySearchOption);
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
        public String getCity(){
            return this.city;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        switch(requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 1:
                BAIDU_READ_PHONE_STATE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到权限，做相应处理
                    //调用定位SDK应确保相关权限均被授权，否则会引起定位失败
                } else{
                    //没有获取到权限，做特殊处理
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        super.onDestroy();
    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()<0)
                return;
            if (suggest!=null)
                suggest.clear();
            /**
             * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
             */
            if(this_city!=null)
            mSuggestionSearch
                    .requestSuggestion((new SuggestionSearchOption())
                            .keyword("学校").city(edit_school.getText().toString()));
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    };
}
