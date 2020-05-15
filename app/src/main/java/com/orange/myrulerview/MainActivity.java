package com.orange.myrulerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ScaleView scaleView;
    private EditText edNum;
    private ImageView imgMinus;
    private ImageView imgPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scaleView = findViewById(R.id.scaleView);
        edNum = findViewById(R.id.ed_num);
        imgMinus = findViewById(R.id.img_minus);
        imgPlus = findViewById(R.id.img_plus);

        imgMinus.setOnClickListener(this);

        imgPlus.setOnClickListener(this);

        scaleView.setOnScaleChangeListener(new ScaleView.OnScaleChangeListener() {
            @Override
            public void OnChange(double index) {
                edNum.setText(String.valueOf(index));
            }
        });

        edNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)  {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    scaleView.setNowIndex(Double.valueOf(String.valueOf(edNum.getText())));
                    return false;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_minus:
                scaleView.setMinusScale();
                break;
            case R.id.img_plus:
                scaleView.setPlusScale();
                break;
            default:
        }
    }
}
