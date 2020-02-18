package com.gova.openmap.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gova.openmap.DetailActivity;
import com.gova.openmap.R;
import com.gova.openmap.models.Mechanic;
import com.squareup.picasso.Picasso;

import java.util.List;



public class MechanicAdapter extends RecyclerView.Adapter<MechanicAdapter.MyViewHolder> {

    private List<Mechanic> mechanics;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, mechanicType, address;
        public ImageView image;
        CardView parentLayout;
        Intent intent;

        public MyViewHolder(View view) {
            super(view);


            mContext = view.getContext();

            name = (TextView) view.findViewById(R.id.textViewName);
            address = (TextView) view.findViewById(R.id.textViewAddress);
            mechanicType = (TextView) view.findViewById(R.id.textViewMechinicType);
            image =  view.findViewById(R.id.mechanicImageView);
             parentLayout = itemView.findViewById(R.id.parent_layout);



        }


    }




    public MechanicAdapter(List<Mechanic> mechanics) {

        this.mechanics = mechanics;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mechanic_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Mechanic mechanic = mechanics.get(position);

       /* String mechType;
        for (String mechanicType : mechanic.getMechanicTypes())
        {
            mechType  = mechanicType+ ", ";
        }*/

        holder.name.setText(mechanic.getName());
        holder.address.setText(mechanic.getAddress());
        holder.mechanicType.setText(mechanic.getMechanicTypes().toString());
    Picasso.get().load(mechanic.getImageUri()).fit().centerCrop().into(holder.image);




        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            Intent intent;
            @Override
            public void onClick(View v) {
              //  Log.d("Detial Activity", "onClick: clicked on: " + mechanics.get(position));

               // Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, DetailActivity.class);

                intent.putExtra("Mechanic", mechanic);

// To retrieve object in second Activity


                /*intent.putExtra("image_url", mImages.get(position));
                intent.putExtra("image_name", mImageNames.get(position));*/
                mContext.startActivity(intent);

            }
        });







    }

    @Override
    public int getItemCount() {
        return mechanics.size();
    }
}