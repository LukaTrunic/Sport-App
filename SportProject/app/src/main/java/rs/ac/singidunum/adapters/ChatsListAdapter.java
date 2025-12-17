package rs.ac.singidunum.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import rs.ac.singidunum.R;
import rs.ac.singidunum.models.ChatRoom;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ViewHolder> {
    public interface OnRoomClickListener {
        void onRoomClick(ChatRoom room);
    }

    private final List<ChatRoom> rooms = new ArrayList<>();
    private final OnRoomClickListener listener;

    public ChatsListAdapter(OnRoomClickListener listener) {
        this.listener = listener;
    }

    public void setRooms(List<ChatRoom> newRooms) {
        rooms.clear();
        rooms.addAll(newRooms);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom room = rooms.get(position);
        holder.tvTitle.setText(room.title);
        holder.itemView.setOnClickListener(v -> listener.onRoomClick(room));
    }

    @Override public int getItemCount() {
        return rooms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvRoomTitle);
        }
    }
}
