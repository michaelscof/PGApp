package com.example.prem.pgapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prem on 03-Mar-18.
 **/

/*
1.RecyclerView.Adapter
2.RecyclerView.ViewHolder
 */
public class PGAdapter extends RecyclerView.Adapter<PGAdapter.PGViewHolder> {
    private Context mCtx;
    private List<PGs> pgList;

    public PGAdapter(Context mCtx, List<PGs> pgList) {
        this.mCtx = mCtx;
        this.pgList = pgList;
    }

    @Override
    public PGViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater pgInflater=LayoutInflater.from(mCtx);
        View pgView=pgInflater.inflate(R.layout.pg_list_layout,null);
        PGViewHolder pgViewHolder=new PGViewHolder(pgView);
        return pgViewHolder;
    }

    @Override
    public void onBindViewHolder(PGViewHolder holder, int position) {
        PGs pGs=pgList.get(position);
        holder.textViewTitle.setText(pGs.getTitle());
        holder.textViewDesc.setText(pGs.get_shortdesc());
        holder.textViewRating.setText(String.valueOf(pGs.getRating()));
        holder.textViewPrice.setText(String.valueOf(pGs.getPrice()));
        holder.pgImage.setImageDrawable(mCtx.getResources().getDrawable(pGs.getImage()));

    }

    @Override
    public int getItemCount() {
        return pgList.size();
    }

    class PGViewHolder extends RecyclerView.ViewHolder
    {
        ImageView pgImage;
        TextView textViewTitle,textViewDesc,textViewRating,textViewPrice;
        public PGViewHolder(View itemView) {
            super(itemView);
            pgImage=itemView.findViewById(R.id.imageView);
            textViewDesc=itemView.findViewById(R.id.textViewShortDesc);
            textViewTitle=itemView.findViewById(R.id.textViewTitle);
            textViewPrice=itemView.findViewById(R.id.textViewPrice);
            textViewRating=itemView.findViewById(R.id.textViewRating);
        }
    }
}
