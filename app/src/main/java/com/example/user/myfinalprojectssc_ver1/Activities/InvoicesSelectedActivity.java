package com.example.user.myfinalprojectssc_ver1.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.user.myfinalprojectssc_ver1.Adapter.InvoicesSelectedAdapter;
import com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices.AllInvoiceDetail;
import com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices.getAllDataInvoices;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.APIUtils;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.SOService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoicesSelectedActivity extends AppCompatActivity {

    SOService mService;

    RecyclerView recyclerView;

    List<AllInvoiceDetail> allInvoiceDetailList;

    InvoicesSelectedAdapter invoicesSelectedAdapter;

    ProgressDialog progressDialog;
    int sum;
    TextView total_invoices;

    int count = 0;
    String idInvoices;
    String activity;
    TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices_selected);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_invoices_selected);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Căn tin CNS");

        textView = findViewById(R.id.information);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        textView.setText(currentDateandTime + "\nChi nhánh trung tâm");
        idInvoices = getIntent().getStringExtra("idInvoices");
        activity = getIntent().getStringExtra("Activity");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Hệ thống đang xử lý");
        progressDialog.show();
        total_invoices = findViewById(R.id.total_invoices);
        InitRecyclerView(idInvoices);
    }

    @Override
    public void onBackPressed() {
        switch (activity){
            case "SettlementActivity":
                Intent intent = new Intent(InvoicesSelectedActivity.this,SettlementActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case "StoreActivity":
                Intent intent1 = new Intent(InvoicesSelectedActivity.this,StoreActivity.class);
                startActivity(intent1);
                this.finish();
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void InitRecyclerView(String s){
        recyclerView = findViewById(R.id.recyclerView_invoices_selected);
        recyclerView.setHasFixedSize(true);
        allInvoiceDetailList = new ArrayList<>();
        invoicesSelectedAdapter = new InvoicesSelectedAdapter(allInvoiceDetailList,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        gridLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GetDataInvoicesSelected(s);
        recyclerView.setAdapter(invoicesSelectedAdapter);
    }

    private void GetDataInvoicesSelected(String id){
        APIUtils apiUtils = new APIUtils("https://public.kiotapi.com/");
        mService = apiUtils.getSOService();
        mService.ReturnDataInvoicesSelected("Bearer " + MainActivity.accessToken,id).enqueue(new Callback<getAllDataInvoices>() {
            @Override
            public void onResponse(Call<getAllDataInvoices> call, final Response<getAllDataInvoices> response) {
                Log.d("hodongtrieuTest1",response.message());
                if(response.message().equals("OK")){
                    for(int i = 0 ; i < response.body().getInvoiceDetails().size() ; i ++){
                        allInvoiceDetailList.add(response.body().getInvoiceDetails().get(i));
                    }
                    sum += response.body().getTotal();
                    invoicesSelectedAdapter.notifyDataSetChanged();
                    total_invoices.setText(sum +"");
                    count = 0;
                    progressDialog.dismiss();
//                    count++;
//                    FletchDataInvoicesFromKiot(idInvoices);
                }
            }
            @Override
            public void onFailure(Call<getAllDataInvoices> call, Throwable t) {

            }
        });
    }
}
