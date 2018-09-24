package com.example.user.myfinalprojectssc_ver1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices.AllInvoiceDetail;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.KiotVietModel;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperKiotViet;
import com.example.user.myfinalprojectssc_ver1.Table.StoreOneInvoicesModel;

import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/20/2018
 */
public class StoreOneInvoicesAdapter extends RecyclerView.Adapter<StoreOneInvoicesAdapter.MyViewHolder> {

    private List<StoreOneInvoicesModel> storeOneInvoicesModelList;
    private Context context;


    public StoreOneInvoicesAdapter(List<StoreOneInvoicesModel> storeOneInvoicesModelList,Context context){
        this.storeOneInvoicesModelList = storeOneInvoicesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recyclerview_store_1_invoices,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        StoreOneInvoicesModel storeOneInvoicesModel = storeOneInvoicesModelList.get(i);
        myViewHolder.name_category_store.setText(storeOneInvoicesModel.getName());
        myViewHolder.code_category_store.setText(storeOneInvoicesModel.getCode());
        myViewHolder.quantity_category_store.setText("Số lượng: " + String.valueOf(storeOneInvoicesModel.getQuantity()));
        StringBuilder s = new StringBuilder();
        int numLength = storeOneInvoicesModel.getPrice().length();
        for (int j =0; j < numLength; j++) {
            if ((numLength-j)%3 == 0 && j != 0) {
                s.append(".");
            }
            s.append(storeOneInvoicesModel.getPrice().charAt(j));
        }
        myViewHolder.price_category_store.setText(s.toString());

        Glide.with(context).load(storeOneInvoicesModel.getImages()).into(myViewHolder.images_store);

    }

    @Override
    public int getItemCount() {
        return storeOneInvoicesModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_category_store,code_category_store,price_category_store,quantity_category_store;
        ImageView images_store;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            images_store = itemView.findViewById(R.id.images_store);
            name_category_store = itemView.findViewById(R.id.name_category_store);
            code_category_store = itemView.findViewById(R.id.code_category_store);
            price_category_store = itemView.findViewById(R.id.price_category_store);
            quantity_category_store = itemView.findViewById(R.id.quantity_category_store);
        }
    }
}
