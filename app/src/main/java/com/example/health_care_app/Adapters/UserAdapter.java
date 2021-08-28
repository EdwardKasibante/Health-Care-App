package com.example.health_care_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.health_care_app.R;
import com.example.health_care_app.Screens.PatientDetailsActivity;
import com.example.health_care_app.UserList;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.DataViewHolder> implements Filterable {

    private Context mCtx;
    String status;
    private int doll;
    List<UserList> mData;
    List<UserList> mDataFiltered;

    public UserAdapter(Context mCtx, List<UserList> mData) {
        this.mCtx = mCtx;
        this.mData = mData;
        this.mDataFiltered = mData;
    }
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_patients, parent,false);
//        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(layoutParams);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, final int position) {
        final UserList info = mDataFiltered.get(position);
        holder.lname.setText(info.getLname());
        holder.fname.setText(info.getFname());
        holder.dob.setText(info.getDob());
        holder.added_by.setText("Registrar: "+info.getAdded_by());
        holder.gender.setText(info.getGender());
        holder.phone.setText(info.getPhone());
        Glide.with(mCtx).load(info.getImage()).into(holder.image);
        holder.car_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(mCtx, PatientDetailsActivity.class);
              intent.putExtra("fullname",info.getFname()+" "+info.getLname());
              intent.putExtra("dob",info.getDob());
              intent.putExtra("gender",info.getGender());
                intent.putExtra("time",info.getTime());
              intent.putExtra("image",info.getImage());
              intent.putExtra("date", info.getDate());
                intent.putExtra("phone", info.getPhone());
              intent.putExtra("key",info.getFirebaseKeys());
              mCtx.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return myFilterData;
    }


    private Filter myFilterData = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String key=constraint.toString();
            if (key.isEmpty()){
                mDataFiltered=mData;
            }
            else{
                List<UserList> FilteredList=new ArrayList<>();
                for (UserList row: mData){
                    if (row.getFname().toString().contains(key)){
                        FilteredList.add(row);
                    }
                }

                mDataFiltered=FilteredList;
            }
            FilterResults  filterResults=new FilterResults();
            filterResults.values=mDataFiltered;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mDataFiltered=(List<UserList>)results.values;
//            mData.clear();
//            mData.addAll((Collection<? extends Home_Objects>) results.values);
            notifyDataSetChanged();
        }
    };
    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView fname,lname,address,gender,dob,date,time,phone,added_by;
        private CircleImageView image;
        private CardView car_here;
        public DataViewHolder(View itemView) {
            super(itemView);
            fname=itemView.findViewById(R.id.fname);
            added_by=itemView.findViewById(R.id.added_by);
            lname=itemView.findViewById(R.id.lname);
            gender=itemView.findViewById(R.id.gender);
            dob=itemView.findViewById(R.id.dob);
            phone=itemView.findViewById(R.id.phone);
            image=itemView.findViewById(R.id.image);
            car_here=itemView.findViewById(R.id.car_here);
        }
    }

}