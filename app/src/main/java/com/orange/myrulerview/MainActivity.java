package com.orange.myrulerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ScaleView scaleView;
    private TextView tvNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scaleView = findViewById(R.id.scaleView);
        tvNum = findViewById(R.id.tv_num);

        scaleView.setOnScaleChangeListener(new ScaleView.OnScaleChangeListener() {
            @Override
            public void OnChange(double index) {
                tvNum.setText(String.valueOf(index));
            }
        });

    }
}
