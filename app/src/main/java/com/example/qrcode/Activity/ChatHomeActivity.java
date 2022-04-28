package com.example.qrcode.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.qrcode.R;
import com.example.qrcode.Adapter.UserAdapter;
import com.example.qrcode.Class.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatHomeActivity extends AppCompatActivity {
    RecyclerView mainUserRecyclerView ; // initialized recycle view
    UserAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database ;

    ArrayList<Users> usersArrayList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        usersArrayList = new ArrayList<>();

        DatabaseReference reference = database.getReference().child("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve all the users from the database (iterator)
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged(); // refresh himself
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });



        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView) ; // get recycle view by id
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // to send the layout
        adapter = new UserAdapter(ChatHomeActivity.this,usersArrayList); // pass activity and arraylist that contain users
        mainUserRecyclerView.setAdapter(adapter);
    }
}