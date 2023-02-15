package com.example.blutspende;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Connector extends RecyclerView.Adapter<Connector.ViewHolder> {
    List<ModelRecyclerView> list;
    Context context;

    public Connector(List<ModelRecyclerView> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(list.get(position).getPictureLink()).into(holder.profilePic);
        holder.name.setText(list.get(position).getUserName());
        holder.bloodGroup.setText(list.get(position).getBloodGroup());
        holder.contact.setText(list.get(position).getPhoneNumber());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dial = "tel:" + list.get(position).getPhoneNumber();
                context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic,call;
        TextView name,bloodGroup,contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.modelRecyclerViewImageView);
            call=itemView.findViewById(R.id.modelRecyclerViewCallButton);
            name=itemView.findViewById(R.id.modelRecyclerViewUserName);
            contact=itemView.findViewById(R.id.modelRecyclerViewPhone);
            bloodGroup=itemView.findViewById(R.id.modelRecyclerViewBloodGroup);
        }
    }
}
