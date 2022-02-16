package a.w.ha;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

public class Typewriter extends AppCompatTextView {
	private boolean running = false;
	private CharSequence mText;
	private int mIndex;
	private long mDelay = 3000;
	public Typewriter(Context context) {
		super(context);
	}
	public Typewriter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	private Handler mHandler = new Handler();
	private Runnable characterAdder = new Runnable() {
		@Override
		public void run() {
			setText(mText.subSequence(0, mIndex++));
			if(mIndex <= mText.length()) {
				mHandler.postDelayed(characterAdder, mDelay);
			}
			else {
				Log.e("End", "Textend");
				running = false;
			}
		}
	};

	public boolean nextText(){
		if(running) {
			mIndex = mText.length();
			setText(mText);
			return false;
		}
		return true;
	}

	public void animateText(CharSequence text) {
		running = true;
		mText = text;
		mIndex = 0;
		setText("");
		mHandler.removeCallbacks(characterAdder);
		mHandler.postDelayed(characterAdder, mDelay);
	}

	public void setCharacterDelay(long millis) {
		mDelay = millis;
	}
}