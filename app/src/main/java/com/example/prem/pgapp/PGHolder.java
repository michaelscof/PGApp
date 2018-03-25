package com.example.prem.pgapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Prem on 25-Mar-18.
 */

public class PGHolder extends RecyclerView.ViewHolder {
    public TextView name,address,contact,landmark;
    public ImageView image;
    public PGHolder(View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.viewPGName);
        address=itemView.findViewById(R.id.viewPGArea);
        contact=itemView.findViewById(R.id.viewPGType);
        landmark=itemView.findViewById(R.id.textViewPrice);
        image=itemView.findViewById(R.id.imageView);
    }

   public void setName(String name1)
   {
       name.setText(name1);
   }
   public void setAddress(String address1)
   {
       address.setText(address1);
   }
   public void setContact(String contact1)
   {
       contact.setText(contact1);
   }
   public void setLandmark(String landmark1)
   {
       landmark.setText(landmark1);
   }
   public void setImage(Context ctx,String image1)
   {
       Picasso.with(ctx).load(image1).into(image);
   }
}
