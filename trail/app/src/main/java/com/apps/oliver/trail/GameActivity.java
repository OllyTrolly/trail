package com.apps.oliver.trail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class GameActivity extends Activity {

    private static final String TAG = GameActivity.class.getSimpleName();
    private int gameMode;
    private GamePanel panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            gameMode = extras.getInt("GAME_MODE");
            Log.d(TAG, "gameMode is " + gameMode);
        }
        else {
            Log.d(TAG, "Can't find gameMode");
            gameMode = 1;
        }
        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        final GamePanel panel = new GamePanel(this, robotoLight, gameMode);
        int id = 0;
        panel.setId(id);
        setContentView(panel);

        Log.d(TAG, "View added");
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "Restarting...");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "Pausing...");
        super.onPause();
    }

    protected void onResume() {
        Log.d(TAG, "Resuming...");
        super.onResume();
    }

    @Override //Just adds Log dialogue to overridden method
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override //Just adds Log dialogue to overridden method
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "Saving instance state...");
        super.onSaveInstanceState(savedInstanceState); // the UI component values are saved here.
        //savedInstanceState.putParcelable("gamePanel", panel.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "Restoring instance state...");
        super.onRestoreInstanceState(savedInstanceState); // the UI component values are saved here.
        //panel.onRestoreInstanceState(savedInstanceState.getParcelable("gamePanel"));
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
