package com.example.qrcode.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.Adapter.MessagesAdapter;
import com.example.qrcode.Class.Messages;
import com.example.qrcode.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String receiverName, receiverUID, senderUID;
    TextView receiverName_TV;

    FirebaseDatabase database;
    FirebaseAuth auth;

    CardView sendBtn;
    EditText edtMessage;


    String senderRoom, receiverRoom;

    RecyclerView messageAdapterRecyclerView;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        receiverName = getIntent().getStringExtra("name");
        receiverUID = getIntent().getStringExtra("uid");
        // receiverUID = getIntent().getStringExtra("uidToChat");

        messagesArrayList = new ArrayList<>();

        messageAdapterRecyclerView = findViewById(R.id.messageAdapterRecycleView); // get recycle view by id
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this); // to send the layout
        linearLayoutManager.setStackFromEnd(true);
        messageAdapterRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(ChatActivity.this, messagesArrayList); // pass activity and arraylist that contain users
        messageAdapterRecyclerView.setAdapter(adapter);
        receiverName_TV = findViewById(R.id.reciverName);

        receiverName_TV.setText(receiverName);

        senderUID = auth.getUid();

        // UID chat between sender and receiver
        senderRoom = senderUID + receiverUID;
        // UID chat between receiver and sender
        receiverRoom = receiverUID + senderUID;

        sendBtn = findViewById(R.id.sendMessageBtn);
        edtMessage = findViewById(R.id.edtMessage);

        // DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear(); // clear to not insert messages again
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);

                }
                adapter.notifyDataSetChanged(); // refresh himself
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Enter Your Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMessage.setText("");
                Date date = new Date();
                Messages messages = new Messages(message, senderUID, date.getTime());

                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats").child(receiverRoom).child("messages").push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}