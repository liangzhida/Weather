package com.example.weather.acitivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.HTTP.HttpUtils;
import com.example.weather.R;
import com.example.weather.bean.City;
import com.example.weather.bean.Weather;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CityAddActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgfanhui;
    private ListView lv;
    private FloatingActionButton fab;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_add);
        initView();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CityAddActivity.this, CityActivity.class));
            }
        });
        init();

    }

    private void init() {
        imgfanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    City city1 = new City();
                    ArrayList<City> citys=new ArrayList<>();
                    city1.setCity("高州");
                    City city2 = new City();
                    city2.setCity("深圳");
                    citys.add(city1);
                    citys.add(city2);
                    String s = HttpUtils.get1();
                    final Weather weather = new Gson().fromJson(s, Weather.class);


                    SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                   String city=sharedPreferences.getString("city","");
                    final List<String> list=new ArrayList<>();
                    list.add("city");   
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lv.setAdapter(new BaseAdapter() {

                                class ViewHolder {
                                    public View rootView;
                                    public ImageView imgweather;
                                    public TextView tvcity;
                                    public TextView tvweather;
                                    public TextView tvtemp;

                                    public ViewHolder(View rootView) {
                                        this.rootView = rootView;
                                        this.imgweather = (ImageView) rootView.findViewById(R.id.imgweather);
                                        this.tvcity = (TextView) rootView.findViewById(R.id.tvcity);
                                        this.tvweather = (TextView) rootView.findViewById(R.id.tvweather);
                                        this.tvtemp = (TextView) rootView.findViewById(R.id.tvtemp);
                                    }

                                }

                                @Override
                                public int getCount() {
                                    return 2;
                                }

                                @Override
                                public Object getItem(int position) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int position) {
                                    return position;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    convertView = LayoutInflater.from(CityAddActivity.this).inflate(R.layout.item_city, parent, false);
                                    ViewHolder viewHolder = new ViewHolder(convertView);
                                    viewHolder.tvcity.setText(weather.getResult().getCity()+"");
                                    viewHolder.tvtemp.setText(weather.getResult().getTemp()+"°");
                                    viewHolder.tvweather.setText("空气"+weather.getResult().getAqi().getQuality()+weather.getResult().getTemphigh()+"°"+"/"+weather.getResult().getTemplow()+"°");
                                    return convertView;
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void initView() {
        imgfanhui = (ImageView) findViewById(R.id.imgfanhui);
        lv = (ListView) findViewById(R.id.lv);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                break;
        }
    }
}
