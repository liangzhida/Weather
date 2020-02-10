package com.example.weather.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;
import com.example.weather.custom.KeyBoardUtils;
import com.example.weather.custom.SPUtils;
import com.example.weather.custom.ZFlowLayout;

public class CityActivity extends AppCompatActivity implements View.OnClickListener {
    ZFlowLayout historyFl;
    EditText autoSearch;
    Button button_search;
    private ImageView imgempty;
    private ZFlowLayout history_fl;
    private Button tvcitybeijin;
    private Button tvcityshangahi;
    private Button tvcityguangzhou;
    private Button tvcityshengzheng;
    private Button tvcityzhuhai;
    private Button tvcityfoshang;
    private Button tvcitynanjing;
    private Button tvcitysuzhou;
    private Button tvcitywuhan;
    private Button tvcityqingdao;
    private Button tvcityxian;
    private Button tvcitytaiyuan;
    private SPUtils spUtils = new SPUtils();
    private ImageView imgclear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        historyFl = findViewById(R.id.history_fl);
        autoSearch = findViewById(R.id.autoSearch);
        button_search = findViewById(R.id.button_search);

        initHistory();
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyBoardUtils.isSoftShowing(CityActivity.this)) {
                    KeyBoardUtils.hintKeyboard(CityActivity.this);
                }
                String searchKey = autoSearch.getText().toString();
                if (!isNullorEmpty(searchKey)) {
//                    if (RegularUtils.hasEmoji(autoSearch.getText().toString())) {
//                        //含有非法字符串
//                    } else {
                    //搜索
                    if (autoSearch.getText().toString().equals("")){
                        Toast.makeText(CityActivity.this, "输入为空!", Toast.LENGTH_SHORT).show();
                    }else {
                        String keyWord = autoSearch.getText().toString();
                        if (!isNullorEmpty(keyWord)) {
                            SPUtils.getInstance(CityActivity.this).save(autoSearch.getText().toString());
                        }
                        initHistory();
                    }
                    Intent intent = new Intent(CityActivity.this, CitydetailsActivity.class);

                    intent.putExtra("city",autoSearch.getText().toString().trim());
                    startActivity(intent);


                } else {
                    Toast.makeText(CityActivity.this, "输入为空!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean isNullorEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 初始化  历史记录列表
     */
    private void initHistory() {

        final String[] data = SPUtils.getInstance(CityActivity.this).getHistoryList();
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        historyFl.removeAllViews();
        for (int i = 0; i < data.length; i++) {
            if (isNullorEmpty(data[i])) {

                return;
            }
            //有数据往下走
            final int j = i;
            //添加分类块
            View paramItemView = getLayoutInflater().inflate(R.layout.adapter_search_keyword, null);
            TextView keyWordTv = paramItemView.findViewById(R.id.tv_content);
            keyWordTv.setText(data[j]);
            historyFl.addView(paramItemView, layoutParams);

            keyWordTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (KeyBoardUtils.isSoftShowing(CityActivity.this)) {
                        KeyBoardUtils.hintKeyboard(CityActivity.this);
                    }
                    autoSearch.setText(data[j]);
                    autoSearch.setSelection(data[j].length());//光标在最后
                    if (!isNullorEmpty(data[j])) {
                        SPUtils.getInstance(CityActivity.this).save(autoSearch.getText().toString());
                    }
                    //点击事件
                }
            });
            // initautoSearch();
        }
    }

    private void initView() {
        imgempty = (ImageView) findViewById(R.id.imgempty);
        history_fl = (ZFlowLayout) findViewById(R.id.history_fl);
        tvcitybeijin = (Button) findViewById(R.id.tvcitybeijin);
        tvcityshangahi = (Button) findViewById(R.id.tvcityshangahi);
        tvcityguangzhou = (Button) findViewById(R.id.tvcityguangzhou);
        tvcityshengzheng = (Button) findViewById(R.id.tvcityshengzheng);
        tvcityzhuhai = (Button) findViewById(R.id.tvcityzhuhai);
        tvcityfoshang = (Button) findViewById(R.id.tvcityfoshang);
        tvcitynanjing = (Button) findViewById(R.id.tvcitynanjing);
        tvcitysuzhou = (Button) findViewById(R.id.tvcitysuzhou);
        tvcitywuhan = (Button) findViewById(R.id.tvcitywuhan);
        tvcityqingdao = (Button) findViewById(R.id.tvcityqingdao);
        tvcityxian = (Button) findViewById(R.id.tvcityxian);
        tvcitytaiyuan = (Button) findViewById(R.id.tvcitytaiyuan);

        tvcitybeijin.setOnClickListener(this);
        tvcityshangahi.setOnClickListener(this);
        tvcityguangzhou.setOnClickListener(this);
        tvcityshengzheng.setOnClickListener(this);
        tvcityzhuhai.setOnClickListener(this);
        tvcityfoshang.setOnClickListener(this);
        tvcitynanjing.setOnClickListener(this);
        tvcitysuzhou.setOnClickListener(this);
        tvcitywuhan.setOnClickListener(this);
        tvcityqingdao.setOnClickListener(this);
        tvcityxian.setOnClickListener(this);
        tvcitytaiyuan.setOnClickListener(this);
        imgempty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spUtils.cleanHistory();
                spUtils.getHistoryList();
            }
        });
        imgclear = (ImageView) findViewById(R.id.imgclear);
        imgclear.setOnClickListener(this);
        imgclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSearch.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvcitybeijin:
                autoSearch.setText("北京");
                break;
            case R.id.tvcityshangahi:
                autoSearch.setText("上海");
                break;
            case R.id.tvcityguangzhou:
                autoSearch.setText("广州");
                break;
            case R.id.tvcityshengzheng:
                autoSearch.setText("深圳");
                break;
            case R.id.tvcityzhuhai:
                autoSearch.setText("珠海");

                break;
            case R.id.tvcityfoshang:
                autoSearch.setText("佛山");

                break;
            case R.id.tvcitynanjing:
                autoSearch.setText("南京 ");

                break;
            case R.id.tvcitysuzhou:
                autoSearch.setText("苏州");

                break;
            case R.id.tvcitywuhan:
                autoSearch.setText("武汉");

                break;
            case R.id.tvcityqingdao:
                autoSearch.setText("青岛");

                break;
            case R.id.tvcityxian:
                autoSearch.setText("西安");

                break;
            case R.id.tvcitytaiyuan:
                autoSearch.setText("太原");

                break;
        }
    }
}
