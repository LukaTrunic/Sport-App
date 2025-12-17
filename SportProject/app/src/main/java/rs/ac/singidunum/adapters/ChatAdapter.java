package rs.ac.singidunum.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import rs.ac.singidunum.R;
import rs.ac.singidunum.models.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_USER  = 0;
    private static final int VIEW_OTHER = 1;

    private final List<ChatMessage> messages = new ArrayList<>();
    private final String myUid = FirebaseAuth.getInstance().getUid();

    // no-arg ctor, so ChatActivity can just `new ChatAdapter()`
    public ChatAdapter() {}

    @Override
    public int getItemViewType(int pos) {
        ChatMessage m = messages.get(pos);
        return m.getSenderUid().equals(myUid) ? VIEW_USER : VIEW_OTHER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_USER) {
            View v = inflater.inflate(R.layout.item_chat_user, parent, false);
            return new UserVH(v);
        } else {
            View v = inflater.inflate(R.layout.item_chat_other, parent, false);
            return new OtherVH(v);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int pos) {
        ChatMessage msg = messages.get(pos);

        if (holder instanceof UserVH) {
            ((UserVH) holder).tvSender.setText(msg.getSenderName());
            ((UserVH) holder).tvText.setText(msg.getText());
        } else {
            ((OtherVH) holder).tvSender.setText(msg.getSenderName());
            ((OtherVH) holder).tvText.setText(msg.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<ChatMessage> list) {
        messages.clear();
        messages.addAll(list);
        notifyDataSetChanged();
    }

    static class UserVH extends RecyclerView.ViewHolder {
        final TextView tvSender, tvText;
        UserVH(View itemView) {
            super(itemView);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvText   = itemView.findViewById(R.id.tvMessage);
        }
    }

    static class OtherVH extends RecyclerView.ViewHolder {
        final TextView tvSender, tvText;
        OtherVH(View itemView) {
            super(itemView);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvText   = itemView.findViewById(R.id.tvMessage);
        }
    }
}
