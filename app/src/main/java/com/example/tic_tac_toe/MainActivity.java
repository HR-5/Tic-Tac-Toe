package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void single(View view){
        Intent in = new Intent(MainActivity.this, SinglePlayer.class);
        startActivity(in);
    }

    public void multi(View view){
        Intent in = new Intent(MainActivity.this, MultiPlayer.class);
        startActivity(in);
    }
}
