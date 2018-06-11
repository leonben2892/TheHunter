package com.finalproject.hananavr.flying_birds;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class Settings extends AppCompatActivity implements CheckBox.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    CheckBox cbSfx;
    SeekBar skVolume;
    private int volume;
    private boolean IsSFX;

    public Settings(){
        volume = 24;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        skVolume = findViewById(R.id.skVolume);
        skVolume.setMax(100);
        skVolume.setProgress(volume);
        skVolume.setOnSeekBarChangeListener(this);
        cbSfx = findViewById(R.id.cbSfx);
        cbSfx.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (cbSfx.isChecked()){
            cbSfx.setText("SFX sounds on");
            IsSFX = true;
        }else{
            cbSfx.setText("SFX sounds off");
            IsSFX = false;
        }
    }

    public int getVolume(){
        return volume;
    }

    public boolean IsSFX(){
        return IsSFX;
    }

    public void mainMenuClick(View view){
        MainActivity.appBgMusic.start();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        volume = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
