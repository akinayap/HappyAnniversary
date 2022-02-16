package a.w.ha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
	List<ChatMessages> chatMessagesList;
	Context context;

	public ChatAdapter(Context c, List<ChatMessages> msg){
		context = c;
		chatMessagesList = msg;
	}

	@NonNull
	@Override
	public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// infalte the item Layout
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
		// set the view's size, margins, paddings and layout parameters
		return new ChatViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
		ChatMessages data = chatMessagesList.get(position);
		if(data.name  == 'A'){
			holder.g_view.setVisibility(View.VISIBLE);
			holder.b_view.setVisibility(View.GONE);
			holder.g_time.setText(data.time);
			holder.g_list.removeAllViewsInLayout();
			for(int i = 0; i < data.messages.size(); ++i){
				View view = View.inflate(context, R.layout.girl_msg_row, null);
				TextView tv = view.findViewById(R.id.girl_message);
				tv.setText(data.messages.get(i));
				holder.g_list.addView(view);
			}

		} else {
			holder.g_view.setVisibility(View.GONE);
			holder.b_view.setVisibility(View.VISIBLE);
			holder.b_time.setText(data.time);

			holder.b_list.removeAllViewsInLayout();
			for(int i = 0; i < data.messages.size(); ++i){
				View view = View.inflate(context, R.layout.boy_msg_row, null);
				TextView tv = view.findViewById(R.id.boy_message);
				tv.setText(data.messages.get(i));
				holder.b_list.addView(view);
			}
		}
	}

	@Override
	public int getItemCount() {
		return chatMessagesList.size();
	}

	public class ChatViewHolder extends RecyclerView.ViewHolder {
		// init the item view's
		CardView b_view, g_view;
		TextView b_time, g_time;
		LinearLayout b_list, g_list;

		public ChatViewHolder(View itemView) {
			super(itemView);
			// get the reference of item view's
			b_view = itemView.findViewById(R.id.boy_view);
			g_view = itemView.findViewById(R.id.girl_view);

			b_time = itemView.findViewById(R.id.boy_time);
			g_time = itemView.findViewById(R.id.girl_time);

			b_list = itemView.findViewById(R.id.boy_list);
			g_list = itemView.findViewById(R.id.girl_list);

		}
	}
}