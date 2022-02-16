package a.w.ha;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HistoryActivity extends AppCompatActivity {
	private ViewPager2 viewPager;
	private ViewPagerAdapter adapter;
	private TabLayout tabLayout;
	private int[] tabIcons = {
					R.drawable.telegram,
					R.drawable.fb,
					R.drawable.wa
	};

	private String[] tabStrings = {
					"How it started",
					"End of sem",
					"Both of us"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Typewriter start = findViewById(R.id.typeText);
		start.setOnClickListener(v->{
			// Start NewActivity.class
			Intent myIntent = new Intent(HistoryActivity.this,
							ARActivity.class);
			startActivity(myIntent);
		});

		viewPager = findViewById(R.id.viewPager);
		tabLayout = findViewById(R.id.tabLayout);
		adapter = new ViewPagerAdapter(getApplicationContext());

		viewPager.setAdapter(adapter);
		viewPager.setUserInputEnabled(false);
		TabLayoutMediator tlm = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
			tab.setIcon(tabIcons[position]);
			tab.setText(tabStrings[position]);
		});
		tlm.attach();
	}
}