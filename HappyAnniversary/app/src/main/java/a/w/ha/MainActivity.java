package a.w.ha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);

		AppCompatButton start = findViewById(R.id.start_btn);
		start.setOnClickListener(v->{
			Intent myIntent = new Intent(MainActivity.this, HistoryActivity.class);
			startActivity(myIntent);
		});


	}

}
