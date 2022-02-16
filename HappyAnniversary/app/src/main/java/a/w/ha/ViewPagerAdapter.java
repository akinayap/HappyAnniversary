package a.w.ha;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.EventViewHolder>
{
	Context context;
	ViewPagerAdapter(Context c){
		context = c;
	}

	@NonNull
	@Override
	public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.web_frag, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
		if(position == 2)
		{
			holder.fbweb.setVisibility(View.GONE);
			holder.tweb.setVisibility(View.GONE);
			holder.wa.setVisibility(View.VISIBLE);
		}
		else if(position == 1){
			holder.tweb.setVisibility(View.GONE);
			holder.fbweb.setVisibility(View.VISIBLE);
			holder.wa.setVisibility(View.GONE);
		}
		else{
			holder.tweb.setVisibility(View.VISIBLE);
			holder.fbweb.setVisibility(View.GONE);
			holder.wa.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return 3;
	}

	class EventViewHolder extends RecyclerView.ViewHolder {
		WebView fbweb, tweb;
		ConstraintLayout wa;

		EventViewHolder(@NonNull View itemView) {
			super(itemView);
			fbweb = itemView.findViewById(R.id.fbwebview);
			tweb = itemView.findViewById(R.id.telewebview);
			wa = itemView.findViewById(R.id.waview);

			fbweb.loadUrl("file:///android_asset/OurChats/Facebook/fb.html");
			tweb.loadUrl("file:///android_asset/OurChats/Telegram/messages.html");
			List<ChatMessages> chatMessagesList = new ArrayList<>();
			// Populate chat list
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("OurChats/whatsapp.txt")));
				String line = reader.readLine();
				char name = 'P';
				String time = "";
				while(!line.equals("")){
					if((name != line.charAt(20)) || (!time.equals(line.substring(0, 17)))){
						name = line.charAt(20);
						time = line.substring(0, 17);
						ChatMessages newMsg = new ChatMessages(name, time, new ArrayList<>());
						if(name == 'A')
							newMsg.messages.add(line.substring(27));
						else
							newMsg.messages.add(line.substring(33));

						chatMessagesList.add(newMsg);
					}
					else {
						if(name == 'A')
							chatMessagesList.get(chatMessagesList.size()-1).messages.add(line.substring(27));
						else
							chatMessagesList.get(chatMessagesList.size()-1).messages.add(line.substring(33));
					}

					line = reader.readLine();
				}
				Log.e("Done", "Printing");
			} catch (IOException e) {
				e.printStackTrace();
			}

			// get the reference of RecyclerView
			RecyclerView recyclerView = wa.findViewById(R.id.rv);

			// set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
			LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
			recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

			ChatAdapter chatAdapter = new ChatAdapter(context, chatMessagesList);
			recyclerView.setAdapter(chatAdapter);

		}
	}
}