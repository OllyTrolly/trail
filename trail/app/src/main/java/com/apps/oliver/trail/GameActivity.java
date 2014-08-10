package com.apps.oliver.trail;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends Activity {

    private static final String TAG = GameActivity.class.getSimpleName();

    private int gameMode;
    private int stageNo;
    private Score gameScore;
    private GamePanel panel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GamePanel panel = new GamePanel(this);
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
        //outState.putDouble("VALUE", liter);
        //Toast.makeText(this, "Activity state saved", Toast.LENGTH_LONG).show();
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
