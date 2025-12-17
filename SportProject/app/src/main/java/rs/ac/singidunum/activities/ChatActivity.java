package rs.ac.singidunum.activities;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rs.ac.singidunum.R;
import rs.ac.singidunum.adapters.ChatAdapter;
import rs.ac.singidunum.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String           gameId;
    private TextView         tvExpiryWarning;
    private ImageButton      btnSend;
    private Date             gameEnd;
    private RecyclerView     rv;
    private ChatAdapter      adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvExpiryWarning = findViewById(R.id.tvChatExpiryWarning);
        btnSend         = findViewById(R.id.btnSend);
        EditText etMessage = findViewById(R.id.etMessage);

        // pull in the game ID
        gameId = getIntent().getStringExtra("GAME_ID");
        if (gameId == null) finish();

        db = FirebaseFirestore.getInstance();

        // set up your RecyclerView once
        rv = findViewById(R.id.rvChat);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        rv.setAdapter(adapter);

        // --- FETCH GAME DOC TO READ ITS endTimestamp ---
        db.collection("games")
                .document(gameId)
                .get()
                .addOnSuccessListener(doc -> {
                    gameEnd = doc.getDate("endTimestamp");
                    if (gameEnd == null) {
                        // no expiry info → just start listening immediately
                        listenForMessages();
                        return;
                    }
                    long delay = gameEnd.getTime() - System.currentTimeMillis();

                    // show the "expires at" warning
                    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    tvExpiryWarning.setText(
                            "This chat will be deleted at " + fmt.format(gameEnd)
                    );
                    tvExpiryWarning.setVisibility(View.VISIBLE);

                    if (delay <= 0) {
                        // Already expired: disable input & clear messages immediately
                        tvExpiryWarning.setText("Chat has expired");
                        btnSend.setEnabled(false);
                        adapter.setMessages(new ArrayList<>());     // wipe out old messages
                    } else {
                        // Still live: start listening now...
                        listenForMessages();

                        // ...and schedule the expiry event
                        new Handler(Looper.getMainLooper())
                                .postDelayed(() -> {
                                    tvExpiryWarning.setText("Chat has expired");
                                    btnSend.setEnabled(false);
                                    adapter.setMessages(new ArrayList<>());   // clear all messages
                                }, delay);
                    }
                });

        // wire up send button (it will get disabled at expiry)
        btnSend.setOnClickListener(v -> {
            String txt = etMessage.getText().toString().trim();
            if (txt.isEmpty()) return;

            Map<String,Object> msg = new HashMap<>();
            msg.put("text",       txt);
            msg.put("senderName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            msg.put("senderUid",  FirebaseAuth.getInstance().getUid());
            msg.put("timestamp",  FieldValue.serverTimestamp());

            db.collection("games")
                    .document(gameId)
                    .collection("messages")
                    .add(msg);

            etMessage.setText("");
        });
    }

    private void listenForMessages() {
        // real‐time log of all messages
        db.collection("games")
                .document(gameId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snap, err) -> {
                    if (err != null) return;
                    List<ChatMessage> msgs = new ArrayList<>();
                    for (DocumentSnapshot d : snap.getDocuments()) {
                        msgs.add(d.toObject(ChatMessage.class));
                    }
                    adapter.setMessages(msgs);
                    rv.scrollToPosition(msgs.size() - 1);
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

