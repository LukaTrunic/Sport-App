package rs.ac.singidunum.models;

import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
    private String text;
    private String senderName;
    private String senderUid;
    private @ServerTimestamp Date timestamp;  // so Firestore will fill it

    // no-arg for Firestore
    public ChatMessage() { }

    // 4-arg constructor
    public ChatMessage(String text, String senderName, String senderUid, Date timestamp) {
        this.text       = text;
        this.senderName = senderName;
        this.senderUid  = senderUid;
        this.timestamp  = timestamp;
    }

    // getters & setters
    public String getText()         { return text; }
    public void   setText(String t) { this.text = t; }

    public String getSenderName()         { return senderName; }
    public void   setSenderName(String n) { this.senderName = n; }

    public String getSenderUid()         { return senderUid; }
    public void   setSenderUid(String u) { this.senderUid = u; }

    public Date   getTimestamp()               { return timestamp; }
    public void   setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
