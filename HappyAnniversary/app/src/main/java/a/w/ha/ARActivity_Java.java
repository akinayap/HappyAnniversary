package a.w.ha;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.ar.sceneform.ux.ArFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class ARActivity_Java extends AppCompatActivity {
	List<String> messages = new ArrayList<>();
	boolean choosing = false;
	int currMessage = 0;

	AppCompatImageView otterImg;
	AppCompatImageView tigerImg;
	Typewriter tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoadMessages();
		setContentView(R.layout.activity_ar);

		otterImg = findViewById(R.id.otter_img);
		tigerImg = findViewById(R.id.tiger_img);
		tv = findViewById(R.id.typeText);
		tv.setCharacterDelay(150);

		otterImg.setVisibility(View.VISIBLE);
		tigerImg.setVisibility(View.GONE);

		String msg = messages.get(currMessage).substring(2);
		tv.animateText(msg);

		tv.setOnClickListener(v->{
			if(!choosing)
			NextLine();
		});

		Button skipbtn = findViewById(R.id.skip);
		skipbtn.setOnClickListener( v->{
			currMessage = 24;
			NextLine();
		});


	}
	void LoadMessages(){
		try {
			BufferedReader reader = null;
			reader = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open("present_messages.txt")));
			String line = reader.readLine();
			Log.e("String", line);
			while(line!=null) {
				messages.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void NextLine(){
		GifImageView heartImg = findViewById(R.id.bigheart);
		GifImageView gifImg = findViewById(R.id.gif_msg);
		gifImg.setVisibility(View.GONE);
		ConstraintLayout choiceBox = findViewById(R.id.choice_box);
		choiceBox.setVisibility(View.GONE);

		ArFragment frag = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

		//val fragContainer = findViewById<FrameLayout>(R.id.fragmentContainer)
		if(tv.nextText()){
			++currMessage;
			if(currMessage < messages.size()){
				char sender = messages.get(currMessage).charAt(0);
				String msg = messages.get(currMessage).substring(2);

				if(sender == 'A'){
					otterImg.setVisibility(View.GONE);
					tigerImg.setVisibility(View.GONE);
					Objects.requireNonNull(frag).getArSceneView().setVisibility(View.VISIBLE);
					NextLine();
				}
				else if (sender == 'B'){
					otterImg.setVisibility(View.VISIBLE);
					tigerImg.setVisibility(View.GONE);
					heartImg.setVisibility(View.VISIBLE);
					heartImg.animate();
					tv.animateText(msg);
				}
				else if (sender == 'C') {
					choosing = true;
					otterImg.setVisibility(View.GONE);
					tigerImg.setVisibility(View.VISIBLE);
					tv.setText("");
					choiceBox.setVisibility(View.VISIBLE);

					int yesVal = Integer.parseInt(msg.substring(0, 2)) - 1;
					int noVal = Integer.parseInt(msg.substring(3, 5)) - 1;

					Button yesbtn = findViewById(R.id.yesBtn);
					Button nobtn = findViewById(R.id.noBtn);
					yesbtn.setOnClickListener(v->{
						currMessage = yesVal;
						choosing = false;
						NextLine();
					});
					nobtn.setOnClickListener(v -> {
						currMessage = noVal;
						choosing = false;
						NextLine();
					});

				}
				else if (sender == 'G') {
					otterImg.setVisibility(View.VISIBLE);
					tigerImg.setVisibility(View.GONE);
					tv.setText("");

					gifImg.setVisibility(View.VISIBLE);
					int imgId = getResources().getIdentifier(msg, "drawable", getPackageName());
					gifImg.setImageResource(imgId);

				}
				else if (sender == 'J') {
					otterImg.setVisibility(View.GONE);
					tigerImg.setVisibility(View.GONE);
					tv.setText("");
					currMessage = Integer.parseInt(msg.substring(0, 2)) - 1;/*((msg[0].toInt()-48) * 10) + (msg[1].toInt()-48)) - 1;*/
					NextLine();
				}
				else if(sender == '0'){
					otterImg.setVisibility(View.VISIBLE);
					tigerImg.setVisibility(View.GONE);
					tv.animateText(msg);
				}
				else{
					otterImg.setVisibility(View.GONE);
					tigerImg.setVisibility(View.VISIBLE);
					tv.animateText(msg);
				}
			}
		}
	}
}