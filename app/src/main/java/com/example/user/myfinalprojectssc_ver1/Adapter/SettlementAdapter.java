package com.example.user.myfinalprojectssc_ver1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.myfinalprojectssc_ver1.ModelSQLite.TransactionModel;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Table.MenuModel;
import com.example.user.myfinalprojectssc_ver1.Table.SettlementModel;

import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/14/2018
 */
public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.MyViewHolder> {

    List<TransactionModel> transactionModelList;
    Context context;
    SettleMentOnClick listener;

    public SettlementAdapter(List<TransactionModel> transactionModelList,Context context,SettleMentOnClick listener){
        this.transactionModelList = transactionModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recyclerview_settlement,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        TransactionModel transactionModel = transactionModelList.get(i);
        myViewHolder.invoices_settlement_id.setText("Hóa đơn số: " + transactionModel.getId_invoices());
        myViewHolder.settlement_datetime.setText(transactionModel.getDate_time());
        String s = "";
        int numLength = transactionModel.getAmount().length();
        for (int j =0; j < numLength; j++) {
            if ((numLength-j)%3 == 0 && j != 0) {
                s += ".";
            }
            s += transactionModel.getAmount().charAt(j);
        }
        myViewHolder.settlement_price.setText(s);
    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView invoices_settlement_id,settlement_datetime,settlement_price;
        CardView card_view_settlment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            invoices_settlement_id = itemView.findViewById(R.id.invoices_settlement_id);
            settlement_datetime = itemView.findViewById(R.id.settlement_datetime);
            settlement_price = itemView.findViewById(R.id.settlement_price);
            card_view_settlment = itemView.findViewById(R.id.card_view_settlment);
            card_view_settlment.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.ClickPosition(transactionModelList.get(getAdapterPosition()));
        }
    }

    public interface SettleMentOnClick {
        void ClickPosition(TransactionModel transactionModel);
    }
}
