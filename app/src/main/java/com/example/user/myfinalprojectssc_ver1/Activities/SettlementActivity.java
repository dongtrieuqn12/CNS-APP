package com.example.user.myfinalprojectssc_ver1.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.API.SettlementTransactionAPI;
import com.example.user.myfinalprojectssc_ver1.Adapter.SettlementAdapter;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.TransactionModel;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SettlementActivity extends AppCompatActivity implements SettlementAdapter.SettleMentOnClick {

    public DatabaseHelperTransaction databaseHelperTransaction;
    public List<TransactionModel> transactionModelList;
    RecyclerView recyclerView;
    public SettlementAdapter settlementAdapter;
    public TextView settlement_total_transaction,total_settlment_amount;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        InitObjectAndView();
        InitRecyclerView();
    }

    private void InitObjectAndView() {
        transactionModelList = new ArrayList<>();
        databaseHelperTransaction = new DatabaseHelperTransaction(this);
        transactionModelList = databaseHelperTransaction.GetTransactionTYPESALE(Constants.SALE_TYPE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng đợi trong giây lát");
        progressDialog.setCancelable(false);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_settlement);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Danh sách giao dịch");
        //view
        settlement_total_transaction = findViewById(R.id.settlement_total_transaction);
        total_settlment_amount = findViewById(R.id.total_settlment_amount);
        findViewById(R.id.settlement_action).setOnClickListener(view -> SettlementAction());
    }

    private void SettlementAction() {
        if(transactionModelList.size() == 0){
            Toast.makeText(this,"Không có giao dịch nào để kết toán",Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            SettlementTransactionAPI settlementTransactionAPI = new SettlementTransactionAPI();
            try {
                long amount = Long.valueOf(total_settlment_amount.getText().toString().replace(".",""));
                settlementTransactionAPI.execute(new SettlementTransactionAPI.SettlementParam(this,CreateRequestSettlement(amount,transactionModelList.size()),""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String CreateRequestSettlement(long totalAmount,int totalTransaction) throws JSONException {
        String temp = "00000000000000";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mti",Constants.MTI_SETTLEMENT);
        jsonObject.put("processCode","920000");
        Random rand = new Random();
        int n = rand.nextInt(899) + 100;
        int n1 = rand.nextInt(899) + 100;
        String traceNumber = String.valueOf(n) + String .valueOf(n1);
        jsonObject.put("traceNumber",traceNumber);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
        String currentDateandTime = sdf.format(new Date());
        jsonObject.put("time",currentDateandTime.substring(6,12));
        jsonObject.put("date",currentDateandTime.substring(2,6));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String terminalId = preferences.getString("terminalId", getString(R.string.terminalId));
        String merchantId = preferences.getString("merchantId",getString(R.string.merchantId));
        jsonObject.put("terminalId",terminalId);
        jsonObject.put("merchantId",merchantId);

        jsonObject.put("batchNumber",System.currentTimeMillis()/1000);
        String totals = "";
        if(totalTransaction < 10) {
            totals = String.valueOf(totalAmount) + "0" + String.valueOf(totalTransaction);
        } else {
            totals = String.valueOf(totalAmount) + String.valueOf(totalTransaction);
        }
        if(totals.length() == 14){
            jsonObject.put("totals",totals);
        } else {
            jsonObject.put("totals",temp.substring(0,14-totals.length()) + totals);
        }
        Log.d(Constants.TAG,jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,StoreActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void InitRecyclerView(){
        recyclerView = findViewById(R.id.recyclerview_settlement);
        recyclerView.setHasFixedSize(true);
        settlementAdapter = new SettlementAdapter(transactionModelList,this,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        gridLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(settlementAdapter);
        settlementAdapter.notifyDataSetChanged();
        FletchDataFromSQLite();
    }

    @SuppressLint("SetTextI18n")
    private void FletchDataFromSQLite() {
        if(transactionModelList.size() == 0){
            Toast.makeText(this,"Chưa có giao dịch nào được thực hiện",Toast.LENGTH_SHORT).show();
            settlement_total_transaction.setText("Tổng số 0 giao dịch");
        } else {
            long sum = 0;
            for(int i = 0 ; i < transactionModelList.size() ; i++){
                sum += Long.valueOf(transactionModelList.get(i).getAmount());
            }
            settlement_total_transaction.setText("Tổng số " + transactionModelList.size() + " giao dịch");
            StringBuilder s = new StringBuilder();
            int numLength = String.valueOf(sum).length();
            for (int j =0; j < numLength; j++) {
                if ((numLength-j)%3 == 0 && j != 0) {
                    s.append(".");
                }
                s.append(String.valueOf(sum).charAt(j));
            }
            total_settlment_amount.setText(s.toString());
        }
    }


    @Override
    public void ClickPosition(TransactionModel transactionModel) {
        Intent intent = new Intent(SettlementActivity.this,InvoicesSelectedActivity.class);
        Bundle bundle = new Bundle();
        List<String> temp = new ArrayList<>();
        temp.add(transactionModel.getId_invoices());
        bundle.putStringArrayList("idInvoices", (ArrayList<String>) temp);
        bundle.putString("Activity","SettlementActivity");
        intent.putExtras(bundle);
        startActivity(intent);
        SettlementActivity.this.finish();
    }
}
