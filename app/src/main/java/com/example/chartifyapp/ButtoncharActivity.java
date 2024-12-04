package com.example.chartifyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ButtoncharActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttonchar);

        ImageView tsubasa = findViewById(R.id.characterTsubasa);
        ImageView bjHabibie = findViewById(R.id.characterBJHabibie);
        ImageView albertEinstein = findViewById(R.id.characterAlbertEinstein);

        tsubasa.setOnClickListener(v -> openChatbot("Osamu Dazai"));
        bjHabibie.setOnClickListener(v -> openChatbot("BJ Habibie"));
        albertEinstein.setOnClickListener(v -> openChatbot("Albert Einstein"));
    }

    private void openChatbot(String characterName) {
        Intent intent = new Intent(ButtoncharActivity.this, MainActivity.class);
        intent.putExtra("characterName", characterName);
        startActivity(intent);
    }
}

