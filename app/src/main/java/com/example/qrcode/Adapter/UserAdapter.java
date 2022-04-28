package com.example.qrcode.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcode.Activity.ChatHomeActivity;
import com.example.qrcode.Activity.ChatActivity;
import com.example.qrcode.Class.Users;
import com.example.qrcode.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
// adapter is An intermediary through which the data is filled in list
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {


    Context chatHomeActivity ;  // Context that i will updating
    ArrayList<Users> usersArrayList; // array that contain all items

    public UserAdapter(ChatHomeActivity chatHomeActivity, ArrayList<Users> usersArrayList) {
        this.chatHomeActivity = chatHomeActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    // this function takes Context and the item layout
    // its call once only for each item shown on the screen
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(chatHomeActivity).inflate(R.layout.item_user_row,parent,false);
        return new Viewholder(view);
    }



    @Override
    // that update data
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Users users = usersArrayList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())){
            holder.itemView.setVisibility(View.GONE);
        }

        holder.user_name.setText(users.getName());
        holder.user_status.setText(users.getStatus());

        // when i click the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            // if i click on item froward to chat page
            public void onClick(View v) {
                Intent intent = new Intent(chatHomeActivity, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("uid",users.getUid());
                chatHomeActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    // here i identify all item attributes in layout
    // holder class for recycle view
    class Viewholder extends RecyclerView.ViewHolder{
       //  CircleImageView user_profile ;
        TextView user_name ;
        TextView user_status;
        // constructor of view holder
        public Viewholder(@NonNull View itemView) {
            super(itemView);
          //  user_profile = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
        }
    }
}
