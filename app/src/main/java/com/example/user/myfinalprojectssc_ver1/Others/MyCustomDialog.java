package com.example.user.myfinalprojectssc_ver1.Others;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.user.myfinalprojectssc_ver1.R;

/**
 * Created by Ho Dong Trieu on 09/17/2018
 */
public class MyCustomDialog extends AlertDialog.Builder {

    public MyCustomDialog(Context context,String title,String message) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View viewDialog = inflater.inflate(R.layout.layout_recyclerview_settlement, null, false);

        TextView titleTextView = (TextView)viewDialog.findViewById(R.id.title);
        titleTextView.setText(title);
        TextView messageTextView = (TextView)viewDialog.findViewById(R.id.message);
        messageTextView.setText(message);

        this.setCancelable(false);
        this.setView(viewDialog);

    }
}
