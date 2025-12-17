package rs.ac.singidunum.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rs.ac.singidunum.R;
import rs.ac.singidunum.adapters.ChatsListAdapter;
import rs.ac.singidunum.models.ChatRoom;

public class ChatsListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String            myUid;
    private ChatsListAdapter  adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db    = FirebaseFirestore.getInstance();
        myUid = FirebaseAuth.getInstance().getUid();

        RecyclerView rv = findViewById(R.id.recyclerViewChats);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatsListAdapter(room -> {
            startActivity(new Intent(this, ChatActivity.class)
                    .putExtra("GAME_ID", room.id));
        });
        rv.setAdapter(adapter);

        // 1) Listen for the list of games this user joined:
        db.collection("users")
                .document(myUid)
                .collection("joinedGames")
                .orderBy("joinedAt", Query.Direction.DESCENDING)
                .addSnapshotListener((snap, err) -> {
                    if (err != null || snap == null) return;

                    List<ChatRoom> liveRooms = new ArrayList<>();
                    Date now = new Date();

                    // 2) For each joinedGames entry, fetch the parent /games doc:
                    for (DocumentSnapshot joinedDoc : snap.getDocuments()) {
                        String gameId = joinedDoc.getId();
                        String title  = joinedDoc.getString("title");

                        db.collection("games")
                                .document(gameId)
                                .get()
                                .addOnSuccessListener(gameDoc -> {
                                    if (!gameDoc.exists()) return;

                                    Date endTs = gameDoc.getDate("endTimestamp");
                                    // only include if endTimestamp is after now
                                    if (endTs != null && endTs.after(now)) {
                                        ChatRoom r = new ChatRoom();
                                        r.id    = gameId;
                                        r.title = title;
                                        liveRooms.add(r);
                                    }

                                    // update UI (you may batch this to avoid too-many callbacks)
                                    adapter.setRooms(liveRooms);
                                });
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}


