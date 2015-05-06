package com.linkin.mtv.digi.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sonnet on 2/24/15.
 */
public class SolaimanTextView extends TextView {

	public SolaimanTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SolaimanTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SolaimanTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"SolaimanLipi.ttf");
		setTypeface(tf);
	}

}
