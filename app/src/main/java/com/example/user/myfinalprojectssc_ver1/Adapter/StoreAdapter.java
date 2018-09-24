package com.example.user.myfinalprojectssc_ver1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myfinalprojectssc_ver1.Activities.StoreActivity;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.CardData;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Table.StoreModel;

import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/12/2018
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

    private Context mContext;
    private List<StoreModel> storeModelList;
    private StoreAdapterOnlick listener;

    public StoreAdapter(Context mContext,List<StoreModel> storeModelList,StoreAdapterOnlick listener){
        this.mContext = mContext;
        this.storeModelList = storeModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_recyclerview_store_card, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        StoreModel storeModel = storeModelList.get(i);
        myViewHolder.SetCheck();
        myViewHolder.store_food_name.setText(storeModel.getFoodName());
        myViewHolder.store_id_invoices.setText("Hóa đơn số: " + storeModel.getIdInvoices());
        myViewHolder.store_date_time.setText(storeModel.getDatetime().substring(6,8) + "/" +
                storeModel.getDatetime().substring(4,6) + "/" +
                storeModel.getDatetime().substring(0,4) + " " +
                storeModel.getDatetime().substring(8,10) + ":" +
                storeModel.getDatetime().substring(10,12) + ":" +
                storeModel.getDatetime().substring(12,14));
    }

    @Override
    public int getItemCount() {
        return storeModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView store_id_invoices,store_food_name,store_date_time;
        ImageView store_checkbox;
        CardView card_view_store;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            store_id_invoices = itemView.findViewById(R.id.store_id_invoices);
            store_food_name = itemView.findViewById(R.id.store_food_name);
            store_checkbox = itemView.findViewById(R.id.store_checkbox);
            store_date_time = itemView.findViewById(R.id.store_date_time);
            card_view_store = itemView.findViewById(R.id.card_view_store);
            card_view_store.setOnClickListener(this);
        }


        public void SetCheck(){
            if(storeModelList.get(getAdapterPosition()).getCheckbox() == 0){
                store_checkbox.setImageResource(R.drawable.uncheck);
                //listener.OnclickInvoices(storeModelList.get(getAdapterPosition()));
            }else {
                Log.d(Constants.TAG,"set check in here");
                store_checkbox.setImageResource(R.drawable.check);
                //listener.OnclickInvoices(storeModelList.get(getAdapterPosition()));
            }
        }

        @Override
        public void onClick(View view) {
            listener.OnclickInvoices(storeModelList.get(getAdapterPosition()),getAdapterPosition());
            if(storeModelList.get(getAdapterPosition()).getCheckbox() == 0){
                store_checkbox.setImageResource(R.drawable.uncheck);
            }else {
                Log.d(Constants.TAG,"set check in here");
                store_checkbox.setImageResource(R.drawable.check);
            }
        }
    }

    public interface StoreAdapterOnlick {
        void OnclickInvoices(StoreModel storeModel,int index);
    }
}
