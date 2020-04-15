package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    List<String> types;
    Spinner spinner_high, spinner_low;
    TextView textHigh, textLow;
    ArrayAdapter<String> adapter;
    String left, right, left_right, right_left;
    int text_state, last_state;
    float high, low;
    float current_rate = 1;
    String text_rate;
    TextView textView_rate;
    Map<String, String> currency_to_unit;
    Map<String, Float> rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Type of money--------------------
        types = new ArrayList<>();
        types.add("United States - Dollar");
        types.add("Vietnam - Dong");
        types.add("Russia - Rouble");
        types.add("Europe - Euro");
        //--------------------------

        //-------Spinner----------------
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinner_high = findViewById(R.id.spinner_high);
        spinner_low = findViewById(R.id.spinner_low);
        spinner_high.setAdapter(adapter);
        spinner_low.setAdapter(adapter);
        textHigh = findViewById(R.id.text_high);
        textLow = findViewById(R.id.text_low);
        textView_rate = findViewById(R.id.text_rate);
        //--------------------------------

        //-------------Init-----------------
        high = low = 0;
        text_state = 1;
        last_state = 1;
        current_rate = 1;
        text_rate = "1 USD = 1 USD";

        left = right = "USD";
        left_right = right_left = "USD USD";
        rate = new HashMap<String, Float>();
        currency_to_unit = new HashMap<String, String>();
        //-----------------Currency Unit------------//
        currency_to_unit.put("United States - Dollar", "USD");
        currency_to_unit.put("Vietnam - Dong", "VND");
        currency_to_unit.put("Russia - Rouble", "RUB");
        currency_to_unit.put("Europe - Euro", "EUR");
        //-------------------------------------

        //---------------Rate------------------//
        rate.put("USD USD", 1.0f);
        rate.put("USD VND", 23471f);
        rate.put("USD RUB", 72.812f);
        rate.put("USD EUR", 0.9116f);

        rate.put("VND VND", 1f);
        rate.put("VND EUR", 0.0000389f);
        rate.put("VND USD", 0.0000426f);
        rate.put("VND RUB", 0.003102f);

        rate.put("EUR EUR", 1f);
        rate.put("EUR USD", 1.097f);
        rate.put("EUR VND", 25747f);
        rate.put("EUR RUB", 79.873f);

        rate.put("RUB RUB", 1f);
        rate.put("RUB USD", 0.01373f);
        rate.put("RUB EUR", 0.01252f);
        rate.put("RUB VND", 322.3503f);


        //--------------------------------------
        spinner_high.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                left = currency_to_unit.get(types.get(position));
                left_right = left + " " + right;
                right_left = right + " " + left;
                update();
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_low.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                right = currency_to_unit.get(types.get(position));
                left_right = left + " " + right;
                right_left = right + " " + left;
                update();
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.digit_0).setOnClickListener(this);
        findViewById(R.id.digit_1).setOnClickListener(this);
        findViewById(R.id.digit_2).setOnClickListener(this);
        findViewById(R.id.digit_3).setOnClickListener(this);
        findViewById(R.id.digit_4).setOnClickListener(this);
        findViewById(R.id.digit_5).setOnClickListener(this);
        findViewById(R.id.digit_6).setOnClickListener(this);
        findViewById(R.id.digit_7).setOnClickListener(this);
        findViewById(R.id.digit_8).setOnClickListener(this);
        findViewById(R.id.digit_9).setOnClickListener(this);
        findViewById(R.id.text_low).setOnClickListener(this);
        findViewById(R.id.text_high).setOnClickListener(this);
        findViewById(R.id.CE).setOnClickListener(this);
        findViewById(R.id.BS).setOnClickListener(this);
        //--------------------------------------------
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.text_high){
            text_state = 1;
            current_rate = rate.get(left_right);
            text_rate = "1 " + left + " = " + String.valueOf(current_rate) + " " + right;
            textView_rate.setText(text_rate);
        }
        else if(id == R.id.text_low){
            text_state = 2;
            current_rate = rate.get(right_left);
            text_rate = "1 " + right + " = " + String.valueOf(current_rate) + " " +left;

            textView_rate.setText(text_rate);
        }
        else if(id == R.id.digit_1) addDigit(1);
        else if(id == R.id.digit_2) addDigit(2);
        else if(id == R.id.digit_3) addDigit(3);
        else if(id == R.id.digit_4) addDigit(4);
        else if(id == R.id.digit_5) addDigit(5);
        else if(id == R.id.digit_6) addDigit(6);
        else if(id == R.id.digit_7) addDigit(7);
        else if(id == R.id.digit_8) addDigit(8);
        else if(id == R.id.digit_9) addDigit(9);
        else if(id == R.id.digit_0) addDigit(0);
        else if(id == R.id.CE) clearEverything();
        else if(id == R.id.BS) backSpace();
    }

    private void update(){
        if(text_state == 1){
            //high low
            current_rate = rate.get(left_right);
            low = high*current_rate;
            textLow.setText(String.valueOf(low));
            //text_rate
            text_rate = "1 " + left + " = " + String.valueOf(current_rate) + " " + right;
            textView_rate.setText(text_rate);
        }else{
            current_rate = rate.get(right_left);
            high = low*current_rate;
            textHigh.setText(String.valueOf(high));

            text_rate = "1 " + right + " = " + String.valueOf(current_rate) + " " +left;
            textView_rate.setText(text_rate);


        }
    }

    private void clearEverything(){
        high = low = 0;
        textHigh.setText(String.valueOf(high));
        textLow.setText(String.valueOf(low));
    }

    private  void backSpace(){
        if(text_state == 1) {
            high = high / 10;
            textHigh.setText(String.valueOf(high));
        }else if(text_state == 2){
            low = low/10;
            textLow.setText(String.valueOf(low));
        }
        update();
    }


    private void addDigit(int digit){
        if(text_state == 1){
            if(last_state == 2){
                high = 0;
                last_state = 1;
            }
            high = high * 10 + digit;
            textHigh.setText(String.valueOf(high));
            low = high * current_rate;
            textLow.setText(String.valueOf(low));
        }else if(text_state == 2){
            if(last_state == 1){
                low = 0;
                last_state = 2;
            }
            low = low * 10 + digit;
            textLow.setText(String.valueOf(low));
            high = low * current_rate;
            textHigh.setText(String.valueOf(high));
        }
    }






}
