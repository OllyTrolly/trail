package com.apps.oliver.trail;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends Activity {

    private int gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets intent from bundle (if it exists) that denotes the game mode that should be started
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            gameMode = extras.getInt("GAME_MODE");
        }
        else {
            gameMode = 1;
        }

        // Created new typeface from font file (Roboto Light) in assets folder
        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

        // Instantiates a custom SurfaceView object called GamePanel, gives it an id so it can
        // save and restore state, then sets it as the view that can be interacted with
        final GamePanel panel = new GamePanel(this, robotoLight, gameMode);
        int id = 0;
        panel.setId(id);
        setContentView(panel);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // The UI component values are saved here.
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // The UI component values are restored here.
        super.onRestoreInstanceState(savedInstanceState);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
