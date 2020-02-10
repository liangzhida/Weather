package com.example.weather.acitivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;
import com.example.weather.bean.Weather;
import com.example.weather.custom.DBHelper;
import com.example.weather.custom.HorizontalListView;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class CitydetailsActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private ImageView imgfanhui;
    private TextView cityname;
    private HorizontalListView mylistview;
    private LinearLayout content;
    private ImageView addcity;
    private HorizontalListView mybuttomlv;
    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citydetails);
        initView();
        init();
        imgfanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mHelper = new DBHelper(this);
        mDatabase = mHelper.getWritableDatabase();





    }



    private void init() {
        Intent intent = getIntent();
        final String city = intent.getStringExtra("city");

        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.CITY, city);
                mDatabase.insert(DBHelper.TABLE_NAME, null, values);
                mDatabase.close();
                Toast.makeText(CitydetailsActivity.this, "插入成功", Toast.LENGTH_SHORT).show();


            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://api.jisuapi.com/weather/query?appkey=3efb7f36fa537acd&city=" + city;
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().get().url(url).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String string = response.body().string();
                    Weather weather = new Gson().fromJson(string, Weather.class);
                    final Weather.ResultBean result = weather.getResult();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cityname.setText(result.getCity() + "");
                            //周几
                            final List<Weather.ResultBean.DailyBean> daily = result.getDaily();
                            mylistview.setAdapter(new BaseAdapter() {



                                class ViewHolder {
                                    public View rootView;
                                    public TextView day;
                                    public TextView date;
                                    public ImageView imgday;
                                    public TextView condition_day;
                                    public ImageView imgbuttom;
                                    public TextView condition_night;
                                    public TextView direction;
                                    public TextView size;

                                    public ViewHolder(View rootView) {
                                        this.rootView = rootView;
                                        this.day = (TextView) rootView.findViewById(R.id.day);
                                        this.date = (TextView) rootView.findViewById(R.id.date);
                                        this.imgday = (ImageView) rootView.findViewById(R.id.imgday);
                                        this.condition_day = (TextView) rootView.findViewById(R.id.condition_day);
                                        this.imgbuttom = (ImageView) rootView.findViewById(R.id.imgbuttom);
                                        this.condition_night = (TextView) rootView.findViewById(R.id.condition_night);
                                        this.direction = (TextView) rootView.findViewById(R.id.direction);
                                        this.size = (TextView) rootView.findViewById(R.id.size);
                                    }

                                }

                                @Override
                                public int getCount() {
                                    return result.getDaily().size();
                                }

                                @Override
                                public Object getItem(int position) {
                                    return result.getDaily().get(position);
                                }

                                @Override
                                public long getItemId(int position) {
                                    return position;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    convertView = LayoutInflater.from(CitydetailsActivity.this).inflate(R.layout.item_city_top, parent, false);
                                    ViewHolder viewHolder = new ViewHolder(convertView);
                                    String week = daily.get(position).getWeek();
                                    String w = week.subSequence(2, week.length()).toString();
                                    String date = daily.get(position).getDate();
                                    String s = date.subSequence(5, date.length()).toString();
                                    viewHolder.day.setText("周" + w);
                                    viewHolder.date.setText(s + "");
                                    viewHolder.condition_day.setText(result.getDaily().get(position).getDay().getWeather() + "");
                                    viewHolder.size.setText(result.getDaily().get(position).getNight().getWindpower() + "");
                                    viewHolder.direction.setText(result.getDaily().get(position).getNight().getWinddirect() + "");
                                    viewHolder.condition_night.setText(result.getDaily().get(position).getNight().getWeather() + "");
                                    return convertView;
                                }
                            });


                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void insertData() {



    }


    private void initView() {
        imgfanhui = (ImageView) findViewById(R.id.imgfanhui);
        cityname = (TextView) findViewById(R.id.cityname);
        mylistview = (HorizontalListView) findViewById(R.id.mylistview);
        content = (LinearLayout) findViewById(R.id.content);
        addcity = (ImageView) findViewById(R.id.addcity);


    }
}
