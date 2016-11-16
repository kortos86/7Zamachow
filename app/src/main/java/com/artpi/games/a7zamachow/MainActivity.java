package com.artpi.games.a7zamachow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void puzzleOnClick (View V) {
        Intent myIntent = new Intent(MainActivity.this, PuzzleGame.class);
        //myIntent.putExtra("key", value);
        MainActivity.this.startActivity(myIntent);
    }

    public void cardsOnClick (View V) {
        Intent myIntent = new Intent(MainActivity.this, A3CardsGame.class);
        //myIntent.putExtra("key", value);
        MainActivity.this.startActivity(myIntent);
    }

    public void memoryOnClick (View V) {
        Intent myIntent = new Intent(MainActivity.this, MemoryGame.class);
        //myIntent.putExtra("key", value);
        MainActivity.this.startActivity(myIntent);
    }

    public void shootingOnClick (View V) {
        Intent myIntent = new Intent(MainActivity.this, ShootingGame.class);
        //myIntent.putExtra("key", value);
        MainActivity.this.startActivity(myIntent);
    }

}
