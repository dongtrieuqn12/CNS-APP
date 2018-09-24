package com.example.user.myfinalprojectssc_ver1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Table.MenuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ho Dong Trieu on 09/06/2018
 */
public class MenuMainAdapter extends RecyclerView.Adapter<MenuMainAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

    private List<MenuModel> menuModelList;
    private List<MenuModel> menuModelListFilter;
//    private List<Datum> datumList;
//    private List<Datum> datumListFilter;

    PurchaseAdapterOnclick listener;

    public MenuMainAdapter(Context mContext,List<MenuModel> menuModelList,PurchaseAdapterOnclick listener){
        this.mContext = mContext;
        this.menuModelList = menuModelList;
        this.listener = listener;
        this.menuModelListFilter = menuModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_recycerview_menu_card, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final MenuModel menuModel = menuModelListFilter.get(i);
        myViewHolder.title.setText(menuModel.getName());
        String s = "";
        int numLength = menuModel.getPrice().length();
        for (int j =0; j < numLength; j++) {
            if ((numLength-j)%3 == 0 && j != 0) {
                s += ".";
            }
            s += menuModel.getPrice().charAt(j);
        }
        myViewHolder.count.setText(s);

        Glide.with(mContext).load(menuModel.getImage()).into(myViewHolder.thumbnail);

        myViewHolder.bind(menuModel);
    }

    @Override
    public int getItemCount() {
        return menuModelListFilter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView count;
        public CheckedTextView title;
        public ImageView thumbnail;
        public CardView card_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            card_view = itemView.findViewById(R.id.card_view);

            card_view.setOnClickListener(this);
            thumbnail.setOnClickListener(this);
        }

        void bind(MenuModel menuModel) {
            // use the sparse boolean array to check
            if (menuModel.getVisible() == 0) {
                title.setCheckMarkDrawable(R.drawable.uncheck);
                title.setChecked(false);
                card_view.setBackgroundResource(R.color.white);
            }
            else {
                title.setCheckMarkDrawable(R.drawable.check);
                title.setChecked(true);
                card_view.setBackgroundResource(R.color.colorPrimary);
            }
        }

        @Override
        public void onClick(View view) {
            listener.ClickPosition(menuModelListFilter.get(getAdapterPosition()));
            if (menuModelListFilter.get(getAdapterPosition()).getVisible() == 0) {
                title.setCheckMarkDrawable(R.drawable.uncheck);
                title.setChecked(false);
                card_view.setBackgroundResource(R.color.white);
            }
            else {
                title.setCheckMarkDrawable(R.drawable.check);
                title.setChecked(true);
                card_view.setBackgroundResource(R.color.colorPrimary);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    menuModelListFilter = menuModelList;
                } else {
                    List<MenuModel> filteredList = new ArrayList<>();
                    for (MenuModel row : menuModelList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    menuModelListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = menuModelListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menuModelListFilter = (ArrayList<MenuModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface PurchaseAdapterOnclick {
        void ClickPosition(MenuModel menuModel);
    }
}
