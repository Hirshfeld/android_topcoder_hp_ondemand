package com.topcoder.hp.idol.ondemand.BaseClasses;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.topcoder.hp.idol.ondemand.Helpers.SystemBarTintManager;
import com.topcoder.hp.idol.ondemand.R;

public class BaseActivity extends Activity {

    public Activity _activity = null;
    private SystemBarTintManager _tintManager;

    public static void setInsets(Activity context, View view, boolean includeTop, boolean includeBottom) {
        setInsets(context, view, includeTop, includeBottom, false);
    }

    public static void setInsets(Activity context, View view, boolean includeTop, boolean includeBottom, boolean noTranslucencyTop) {
        int topPadding = 0;
        int bottomPadding = 0;
        int rightPadding = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            if (!noTranslucencyTop) topPadding = config.getPixelInsetTop(true);
            bottomPadding = config.getPixelInsetBottom();
            rightPadding = config.getPixelInsetRight();
        }
        view.setPadding(0, includeTop ? topPadding : 0,
                rightPadding, includeBottom ? bottomPadding : 0);
    }

	/* BEGIN TRANSLUCENCY UTILITY METHODS */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        _activity = this;
        setupTransparentTints(this);
        if (findViewById(R.id.content_frame) != null) {
            findViewById(R.id.content_frame).setBackgroundColor(getResources().getColor(R.color.card_gray));
            int color;
            color = getResources().getColor(android.R.color.holo_purple);
            _tintManager.setTintColor(color);
            getActionBar().setBackgroundDrawable(new ColorDrawable(color));
            setInsets(this, findViewById(R.id.content_frame), true, false); // Should this point to the content frame? what is going on herE?
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        _activity = this;
    }

    public void setupTransparentTints(Activity context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        _tintManager = new SystemBarTintManager(context);
        _tintManager.setStatusBarTintEnabled(true);
    }

    /* END TRANSLUCENCY UTILITY METHODS */
}
