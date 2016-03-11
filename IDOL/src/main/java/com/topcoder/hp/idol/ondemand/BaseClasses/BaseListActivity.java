package com.topcoder.hp.idol.ondemand.BaseClasses;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import com.topcoder.hp.idol.ondemand.Helpers.SystemBarTintManager;
import com.topcoder.hp.idol.ondemand.Helpers.Utilities;
import com.topcoder.hp.idol.ondemand.R;

public class BaseListActivity extends ListActivity {

    public Activity _activity = null;
    private ViewAnimator _viewAnimator = null;
    private boolean _isProgressBarHidden = true;
    private SystemBarTintManager tintManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        _activity = this;
        setupTransparentTints(this);
        findViewById(R.id.content_frame).setBackgroundColor(getResources().getColor(R.color.card_gray));
        int color;
        color = getResources().getColor(android.R.color.holo_purple);
        tintManager.setTintColor(color);
        getActionBar().setBackgroundDrawable(new ColorDrawable(color));
        setInsets(this, findViewById(R.id.content_frame), true, false); // Should this point to the content frame? what is going on herE?

    }

    protected void ShowProgressBar() {
        if (_viewAnimator == null) {
            InitViewAnimator();
        }

        if (_viewAnimator != null) {
            Utilities.WriteLogcat("ShowProgressBar");
            if (_isProgressBarHidden) {
                _viewAnimator.showPrevious();
                _isProgressBarHidden = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        _activity = this;
    }

	/* BEGIN TRANSLUCENCY UTILITY METHODS */

    protected void HideProgressBar() {
        if (_viewAnimator == null) {
            InitViewAnimator();
        }

        if (_viewAnimator != null) {
            Utilities.WriteLogcat("HideProgressBar");
            if (!_isProgressBarHidden) {
                _viewAnimator.showPrevious();
                _isProgressBarHidden = true;
            }
        }
    }

    private void InitViewAnimator() {
        _viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
        if (_viewAnimator != null) {
            Animation slide_in_left, slide_out_right;
            slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.fade_in); //android.R.anim.slide__left); //android.R.anim.fade_in); // android.R.anim.slide_in_left);
            slide_out_right = AnimationUtils.loadAnimation(this, android.R.anim.fade_out); //android.R.anim.slide_out_right); // android.R.anim.fade_out); //android.R.anim.slide_out_right);

            _viewAnimator.setInAnimation(slide_in_left);
            _viewAnimator.setOutAnimation(slide_out_right);
        }

    }

    public void setupTransparentTints(Activity context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
    }

    /* END TRANSLUCENCY UTILITY METHODS */
}
