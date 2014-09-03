package com.apps.oliver.trail;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ScoreActivity extends Activity {

    private static final String TAG = ScoreActivity.class.getSimpleName();
    private MenuPanel panel;
    private Point panelSize;
    private int panelWidth;
    private int panelHeight;
    private int highScoreNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            highScoreNumber = extras.getInt("HIGH_SCORE");
            Log.d(TAG, "High score is " + highScoreNumber);
        }
        else {
            highScoreNumber = 10;
        }

        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        ScorePanel panel = new ScorePanel(this, robotoLight, highScoreNumber);
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
}
