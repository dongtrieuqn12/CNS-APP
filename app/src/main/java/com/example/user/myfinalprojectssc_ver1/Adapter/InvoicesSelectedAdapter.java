package com.example.user.myfinalprojectssc_ver1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices.AllInvoiceDetail;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Table.MenuModel;

import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/13/2018
 */
public class InvoicesSelectedAdapter extends RecyclerView.Adapter<InvoicesSelectedAdapter.MyViewHolder> {

    List<AllInvoiceDetail> allInvoiceDetailList;
    Context context;

    public InvoicesSelectedAdapter(List<AllInvoiceDetail> allInvoiceDetailList,Context context){
        this.allInvoiceDetailList = allInvoiceDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_invoices_selected,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AllInvoiceDetail allInvoiceDetail = allInvoiceDetailList.get(position);
        holder.invoices_selected_name.setText(allInvoiceDetail.getProductName());
        holder.invoices_selected_code.setText(allInvoiceDetail.getProductCode());
        holder.invoices_selected_quantity.setText(String.valueOf(allInvoiceDetail.getQuantity()));
        holder.invoices_selected_price.setText(String.valueOf(allInvoiceDetail.getPrice()));
        holder.invoices_selected_total.setText(String.valueOf(allInvoiceDetail.getPrice()*allInvoiceDetail.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return allInvoiceDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView invoices_selected_name,invoices_selected_code,invoices_selected_price,invoices_selected_quantity,invoices_selected_total;
        public MyViewHolder(View itemView) {
            super(itemView);
            invoices_selected_name = itemView.findViewById(R.id.invoices_selected_name);
            invoices_selected_code = itemView.findViewById(R.id.invoices_selected_code);
            invoices_selected_price = itemView.findViewById(R.id.invoices_selected_price);
            invoices_selected_quantity = itemView.findViewById(R.id.invoices_selected_quantity);
            invoices_selected_total = itemView.findViewById(R.id.invoices_selected_total);
        }
    }
}
