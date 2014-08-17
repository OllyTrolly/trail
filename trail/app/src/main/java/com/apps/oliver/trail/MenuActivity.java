package com.apps.oliver.trail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


public class MenuActivity extends Activity {

    private static final String TAG = MenuActivity.class.getSimpleName();
    private MenuPanel panel;
    private Point panelSize;
    private int panelWidth;
    private int panelHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        MenuPanel panel = new MenuPanel(this, robotoLight);
        setContentView(panel);
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

    public void launchGameActivity(int gameMode) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("GAME_MODE", gameMode);
        startActivity(i);
    }
}
