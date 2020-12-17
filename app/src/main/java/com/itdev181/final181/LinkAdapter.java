package com.itdev181.final181;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {
    private ArrayList<Link> links;
    ItemClicked activity;

    public interface ItemClicked{
        void onItemClicked(int position);
    }

    public LinkAdapter(Context context, ArrayList<Link> list){
        links = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivLink;
        TextView tvLink;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            ivLink = itemView.findViewById(R.id.ivLink);
            tvLink = itemView.findViewById(R.id.tvLink);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(links.indexOf((Link) itemView.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public LinkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(links.get(position));

        holder.tvLink.setText(links.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
}
