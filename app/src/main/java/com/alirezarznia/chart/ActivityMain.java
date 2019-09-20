package com.alirezarznia.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox chkScale = findViewById(R.id.chkScale);
        CheckBox chkAxis = findViewById(R.id.chkAxis);
        CheckBox chkNumber = findViewById(R.id.chkNumber);
        CheckBox chkScroll = findViewById(R.id.chkScroll);
        final ChartView chartView = findViewById(R.id.chartView);
        chkScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                chartView.setVisScale(b);
                chartView.invalidate();
            }
        });
        chkAxis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                chartView.setVisAxis(b);
                chartView.invalidate();

            }
        }); chkNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                chartView.setVisNumber(b);
                chartView.invalidate();

            }
        });
        chkScroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                chartView.setVisScroll(b);
                chartView.invalidate();

            }
        });
    }
}
