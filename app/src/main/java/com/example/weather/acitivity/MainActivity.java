package com.example.weather.acitivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.weather.R;
import com.example.weather.bean.Weather;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView imgcitylist;
    private TextView tvlocation;
    private ImageView imgmore;
    private TextView todayweather;
    private TextView tvtodaySituation;
    private ImageView imgtodayweather;
    private TextView tvtodayweather;
    private TextView tvtodayquality;
    private TextView tvtodaytemperature;
    private ImageView imgtomorrowweather;
    private TextView tvtomorrowweather;
    private TextView tvtomorrwquality;
    private TextView tvtomorrowtemperature;
    private ImageView imgthirdweather;
    private TextView tvthirdweather;
    private TextView tvthirdtemperature;
    private LinearLayout ll;
    private TextView tvquality;
    private TextView tvqingkuan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        init();
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Intent intent = getIntent();
//                    String city1 = intent.getStringExtra("city");

                    String city="高州";
                    String url="https://api.jisuapi.com/weather/query?appkey=3efb7f36fa537acd&city="+city;
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().get().url(url).build();
                    String string = okHttpClient.newCall(request).execute().body().string();
                    Weather weather = new Gson().fromJson(string, Weather.class);
                    final Weather.ResultBean result = weather.getResult();
                    if (weather.getMsg().equals("ok")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                tvlocation.setText(result.getCity()+"");
                                todayweather.setText(result.getTemp() + "℃");
                                tvtodaySituation.setText(result.getWeather() + "");
                                tvqingkuan.setText(result.getWinddirect()+"");
                                tvquality.setText("空气   "+result.getAqi().getQuality()+"  "+result.getAqi().getAqi());

                                tvtodayweather.setText("今天."+result.getWeather()+"");
                                tvtodaytemperature.setText(result.getTemphigh()+"°"+"/"+result.getTemplow()+"°");

                                tvtomorrowweather.setText("明天."+result.getWeather()+"");
                                tvtomorrowtemperature.setText(result.getDaily().get(1).getDay().getTemphigh()+"°"+"/"+result.getDaily().get(1).getNight().getTemplow()+"°");


                                String week = result.getDaily().get(2).getWeek();
                                String s1 = week.subSequence(2, week.length()).toString();

                                tvthirdweather.setText("周"+s1+"."+result.getWeather()+"");
                                tvthirdtemperature.setText(result.getDaily().get(2).getDay().getTemphigh()+"°"+"/"+result.getDaily().get(2).getNight().getTemplow()+"°");

                            }
                        });

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initListener() {
        imgcitylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CityAddActivity.class));
            }
        });
        imgmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SetActivity.class));
            }
        });

    }

    private void initGPS() {
        Criteria c = new Criteria();
        c.setPowerRequirement(Criteria.POWER_LOW);//设置耗电量为低耗电
        c.setBearingAccuracy(Criteria.ACCURACY_COARSE);//设置精度标准为粗糙
        c.setAltitudeRequired(false);//设置海拔不需要
        c.setBearingRequired(false);//设置导向不需要
        c.setAccuracy(Criteria.ACCURACY_LOW);//设置精度为低
        c.setCostAllowed(false);//设置成本为不需要
//... Criteria 还有其他属性
        LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = manager.getBestProvider(c, true);
//得到定位信息
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = null;
        if (!TextUtils.isEmpty(bestProvider)) {
            location = manager.getLastKnownLocation(bestProvider);
        }
        if (null == location) {
            //如果没有最好的定位方案则手动配置
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else if (manager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (null == location) {
            Log.i("msg1", "获取定位失败!");
            return;
        }
//通过地理编码的到具体位置信息
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.CHINESE);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() <= 0) {
            Log.i("msg", "获取地址失败!");
        }
        Address address = addresses.get(0);
        String country = address.getCountryName();//得到国家
        String locality = address.getLocality();//得到城市
//要获得哪些信息自己看咯
        Log.i("city", country + locality);
        todayweather.setText(country + locality + "34234");


    }


    private void initView() {
        imgcitylist = (ImageView) findViewById(R.id.imgcitylist);
        tvlocation = (TextView) findViewById(R.id.tvlocation);
        imgmore = (ImageView) findViewById(R.id.imgmore);
        todayweather = (TextView) findViewById(R.id.todayweather);
        tvtodaySituation = (TextView) findViewById(R.id.tvtodaySituation);
        imgtodayweather = (ImageView) findViewById(R.id.imgtodayweather);
        tvtodayweather = (TextView) findViewById(R.id.tvtodayweather);

        tvtodaytemperature = (TextView) findViewById(R.id.tvtodaytemperature);
        imgtomorrowweather = (ImageView) findViewById(R.id.imgtomorrowweather);
        tvtomorrowweather = (TextView) findViewById(R.id.tvtomorrowweather);

        tvtomorrowtemperature = (TextView) findViewById(R.id.tvtomorrowtemperature);
        imgthirdweather = (ImageView) findViewById(R.id.imgthirdweather);
        tvthirdweather = (TextView) findViewById(R.id.tvthirdweather);
        tvthirdtemperature = (TextView) findViewById(R.id.tvthirdtemperature);
        ll = (LinearLayout) findViewById(R.id.ll);
        tvquality = (TextView) findViewById(R.id.tvquality);

        tvqingkuan = (TextView) findViewById(R.id.tvqingkuan);

    }
}
