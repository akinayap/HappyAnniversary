package a.w.ha;

import java.util.ArrayList;
import java.util.List;

class ChatMessages {
	char name;
	String time;
	List<String> messages;

	public ChatMessages(char n, String t, ArrayList<String> msg) {
		name = n;
		time = t;
		messages = msg;

	}
}
