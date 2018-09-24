package com.example.user.myfinalprojectssc_ver1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Table.InvoicesModel;

import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/10/2018
 */
public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.MyViewHolder> {

    private Context mContext;
    private List<InvoicesModel> invoicesModelList;
    private InvoicesAdapterOnlick listener;

    public InvoicesAdapter(Context mContext, List<InvoicesModel> invoicesModelList,InvoicesAdapterOnlick listener){
        this.mContext = mContext;
        this.invoicesModelList = invoicesModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_recyclerview_invoices, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        InvoicesModel invoicesModel = invoicesModelList.get(i);
        myViewHolder.tv_namehd.setText(invoicesModel.getName());
        String s = "";
        int numLength = invoicesModel.getPrice().length();
        for (int j =0; j < numLength; j++) {
            if ((numLength-j)%3 == 0 && j != 0) {
                s += ".";
            }
            s += invoicesModel.getPrice().charAt(j);
        }
        myViewHolder.tv_pricehd.setText(s);
        myViewHolder.tv_selecthd.setText(invoicesModel.getSelected() + "");
        myViewHolder.Click(myViewHolder);
    }

    @Override
    public int getItemCount() {
        return invoicesModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_namehd,tv_pricehd,tv_selecthd;
        public ImageButton decrease;
        public ImageButton increase;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_namehd = itemView.findViewById(R.id.tv_namehd);
            tv_pricehd = itemView.findViewById(R.id.tv_pricehd);
            tv_selecthd = itemView.findViewById(R.id.tv_selecthd);
            decrease = itemView.findViewById(R.id.decrease);
            increase = itemView.findViewById(R.id.increase);
        }

        private void Click(MyViewHolder view) {
            decrease.setOnClickListener(this);
            increase.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.increase:
                    Log.d(Constants.TAG,"+");
                    //tv_selecthd.setText( (Integer.parseInt(tv_selecthd.getText().toString()) + 1) + "" );
                    //notifyDataSetChanged();
                    listener.OnclickIncDec(invoicesModelList.get(getAdapterPosition()),1);
                    break;
                case R.id.decrease:
                    Log.d(Constants.TAG,"-");
                    if (Integer.parseInt(tv_selecthd.getText().toString()) > 1){
                        //tv_selecthd.setText( (Integer.parseInt(tv_selecthd.getText().toString()) - 1) + "" );
                        listener.OnclickIncDec(invoicesModelList.get(getAdapterPosition()),-1);
                    }
                    //notifyDataSetChanged();
                    break;
            }
        }
    }

    public interface InvoicesAdapterOnlick {
        void OnclickIncDec(InvoicesModel invoicesModel,int i);
    }
}
