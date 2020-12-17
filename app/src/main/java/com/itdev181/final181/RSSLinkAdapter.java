package com.itdev181.final181;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RSSLinkAdapter extends RecyclerView.Adapter<RSSLinkAdapter.ViewHolder>{
    private ArrayList<RSSLink> links;
    RSSLinkAdapter.ItemClicked activity;

    public interface ItemClicked{
        void onItemClicked(int position);
    }

    public RSSLinkAdapter(Context context, ArrayList<RSSLink> list){
        links = list;
        activity = (RSSLinkAdapter.ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivLink;
        TextView tvLink;
        Button btnInApp;
        Button btnInBrowser;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            ivLink = itemView.findViewById(R.id.ivLink);
            tvLink = itemView.findViewById(R.id.tvLink);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(links.indexOf((RSSLink) itemView.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public RSSLinkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_list_items, parent, false);

        return new RSSLinkAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RSSLinkAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(links.get(position));

        holder.tvLink.setText(links.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
}
