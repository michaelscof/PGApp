package com.example.prem.pgapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Prem on 25-Mar-18.
 */

public class PGHolder extends RecyclerView.ViewHolder {
    public TextView name,address,type,landmark;
    public ImageView image,imageViewProperty;
    public ImageButton buttonDelete,buttonModify;
    public PGHolder(View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.viewPGName);
        address=itemView.findViewById(R.id.viewPGArea);
        type=itemView.findViewById(R.id.viewPGType);
        landmark=itemView.findViewById(R.id.textViewPrice);
        image=itemView.findViewById(R.id.imageView);
        imageViewProperty=itemView.findViewById(R.id.imageViewProperty);
        image=itemView.findViewById(R.id.imageView);
        buttonDelete=itemView.findViewById(R.id.buttonDelete);
        buttonModify=itemView.findViewById(R.id.buttonModify);
    }

   public void setName(String name1)
   {
       name.setText(name1);
   }
   public void setAddress(String address1)
   {
       address.setText(address1);
   }
   public void setType(boolean type1)
   {
       type.setText(type1?"Boys":"Girls");
   }
   public void setLandmark(String landmark1)
   {
       landmark.setText(landmark1);
   }
   public void setImage(Context ctx,String image1)
   {
       Picasso.with(ctx).load(image1).into(image);
   }
   public  void setImageOwner(Context ctx,String image1)
   {
       Picasso.with(ctx).load(image1).into(imageViewProperty);
   }
}
