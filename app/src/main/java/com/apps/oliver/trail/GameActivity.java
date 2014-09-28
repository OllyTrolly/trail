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
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;

public class GameActivity extends BaseGameActivity {

    private static final String TAG = GameActivity.class.getSimpleName();
    private int gameMode;
    private GameActivity activity;

    // request codes we use when invoking an external activity
    final int RC_RESOLVE = 5000, RC_UNUSED = 5001;

    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in, for instance)
    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            gameMode = extras.getInt("GAME_MODE");
            Log.d(TAG, "gameMode is " + gameMode);
        }
        else {
            Log.d(TAG, "Can't find gameMode");
            gameMode = 1;
        }
        onSignInButtonClicked();
        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        final GamePanel panel = new GamePanel(this, robotoLight, gameMode, activity);
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

    // Game related methods
    public boolean isSignIn() {
        if(isSignedIn()) {
            return true;
        }
        else return false;
    }

    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
    }

    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.leaderboards_not_available));
        }
    }

    @Override
    public void onSignInFailed() {
        // Sign-in failed, so show sign-in button on main menu
    }

    @Override
    public void onSignInSucceeded() {
        // if we have accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onSignInButtonClicked() {
        beginUserInitiatedSignIn();
    }

    /**
     * Check for achievements and unlock the appropriate ones.
     *
     */
    void checkForAchievements(int timeLeft, int stages, int edgesDrawn) {
        // Check if each condition is met; if so, unlock the corresponding
        // achievement.
        if (gameMode == 0 && stages == 10) {
            mOutbox.mTimedAchievement = true;
        }
        if (gameMode == 0 && stages == 20) {
            mOutbox.mTimeBombAchievement = true;
        }
        if (gameMode == 0 && timeLeft >= 15) {
            mOutbox.mTimeWarpAchievement = true;
        }
        if (gameMode == 1 && stages == 15) {
            mOutbox.mMistakesAchievement = true;
        }
        if (gameMode == 1 && stages == 30) {
            mOutbox.mPerfectionistAchievement = true;
        }
        if (gameMode == 2 && stages == 10) {
            mOutbox.mTutorialAchievement = true;
        }
        if (gameMode == 2 && stages > 10) {
            mOutbox.mExtraTutorialStages++;
        }

        mOutbox.mStages++;
        mOutbox.mEdgesDrawn += edgesDrawn;
    }

    void pushAccomplishments() {
        if (!isSignedIn()) {
            // can't push to the cloud, so save locally
            mOutbox.saveLocal(this);
            return;
        }
        if (mOutbox.mTimedAchievement) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_timed));
            mOutbox.mTimedAchievement = false;
        }
        if (mOutbox.mTimeBombAchievement) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_time_bomb));
            mOutbox.mTimeBombAchievement = false;
        }
        if (mOutbox.mTimeWarpAchievement) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_timewarp));
            mOutbox.mTimeWarpAchievement = false;
        }
        if (mOutbox.mMistakesAchievement) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_mistakes));
            mOutbox.mMistakesAchievement = false;
        }
        if (mOutbox.mPerfectionistAchievement) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_perfectionist));
            mOutbox.mPerfectionistAchievement = false;
        }
        if (mOutbox.mTutorialAchievement) {
            Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_euler));
            mOutbox.mTutorialAchievement = false;
        }
        if (mOutbox.mExtraTutorialStages > 0) {
            Games.Achievements.increment(getApiClient(), getString(R.string.achievement_untutorable),
                    mOutbox.mExtraTutorialStages);
            mOutbox.mExtraTutorialStages = 0;
        }
        if (mOutbox.mStages > 0) {
            Games.Achievements.increment(getApiClient(), getString(R.string.achievement_forever_ever),
                    mOutbox.mStages);
            Games.Achievements.increment(getApiClient(), getString(R.string.achievement_forever),
                    mOutbox.mStages);
            mOutbox.mStages = 0;
        }
        if (mOutbox.mEdgesDrawn > 0) {
            Games.Achievements.increment(getApiClient(), getString(R.string.achievement_500_lines),
                    mOutbox.mEdgesDrawn);
            Games.Achievements.increment(getApiClient(), getString(R.string.achievement_2000_lines),
                    mOutbox.mEdgesDrawn);
            Games.Achievements.increment(getApiClient(), getString(R.string.achievement_5000_lines),
                    mOutbox.mEdgesDrawn);
            mOutbox.mEdgesDrawn = 0;
        }
        if (mOutbox.mFlawlessModeScore > 0) {
            Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_flawless),
                    mOutbox.mFlawlessModeScore);
            mOutbox.mFlawlessModeScore = 0;
        }
        if (mOutbox.mTimedModeScore > 0) {
            Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_timed),
                    mOutbox.mTimedModeScore);
            mOutbox.mTimedModeScore = 0;
        }
        mOutbox.saveLocal(this);
    }

    /**
     * Update leaderboards with the user's score.
     *
     * @param finalScore The score the user got.
     */
    void updateLeaderboards(int finalScore) {
        if (gameMode == 0 && mOutbox.mTimedModeScore < finalScore) {
            mOutbox.mTimedModeScore = finalScore;
        } else if (gameMode == 1 && mOutbox.mFlawlessModeScore < finalScore) {
            mOutbox.mFlawlessModeScore = finalScore;
        }
    }

    class AccomplishmentsOutbox {
        boolean mTimedAchievement = false;
        boolean mTimeBombAchievement = false;
        boolean mTimeWarpAchievement = false;
        boolean mMistakesAchievement = false;
        boolean mPerfectionistAchievement = false;
        boolean mTutorialAchievement = false;
        int mStages = 0;
        int mExtraTutorialStages = 0;
        int mEdgesDrawn = 0;
        int mFlawlessModeScore = 0;
        int mTimedModeScore = 0;

        boolean isEmpty() {
            return !mTimedAchievement && !mTimeBombAchievement && !mTimeWarpAchievement && !mMistakesAchievement && !mPerfectionistAchievement && !mTutorialAchievement
                    && mStages == 0 && mExtraTutorialStages == 0&& mEdgesDrawn == 0 && mFlawlessModeScore == 0 && mTimedModeScore == 0;
        }

        public void saveLocal(Context ctx) {
            /* TODO: This is left as an exercise. To make it more difficult to cheat,
             * this data should be stored in an encrypted file! And remember not to
             * expose your encryption key (obfuscate it by building it from bits and
             * pieces and/or XORing with another string, for instance). */
        }

        public void loadLocal(Context ctx) {
            /* TODO: This is left as an exercise. Write code here that loads data
             * from the file you wrote in saveLocal(). */
        }
    }
}
