package com.iamadonar.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotCondensedTextView extends TextView {

public RobotCondensedTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
}

public RobotCondensedTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
}

public RobotCondensedTextView(Context context) {
    super(context);
    init();
}

private void init() {
    if (!isInEditMode()) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/robotocondensed_regular.ttf");
        setTypeface(tf);
    	}
	}
}
