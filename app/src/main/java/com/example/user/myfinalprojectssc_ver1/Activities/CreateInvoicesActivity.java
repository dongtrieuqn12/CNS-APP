package com.example.user.myfinalprojectssc_ver1.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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

import com.example.user.myfinalprojectssc_ver1.Adapter.InvoicesAdapter;
import com.example.user.myfinalprojectssc_ver1.Model.DeleteInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.RequestInvoices.DeliveryDetail;
import com.example.user.myfinalprojectssc_ver1.Model.RequestInvoices.InvoiceDetail;
import com.example.user.myfinalprojectssc_ver1.Model.RequestInvoices.RequestCreateInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.ReturnInvoices.ReturnInvoices;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.KiotVietModel;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.TransactionModel;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.APIUtils;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.SOService;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperKiotViet;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperTransaction;
import com.example.user.myfinalprojectssc_ver1.Table.InvoicesModel;
import com.example.user.myfinalprojectssc_ver1.Table.MenuModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateInvoicesActivity extends AppCompatActivity implements InvoicesAdapter.InvoicesAdapterOnlick {

    String branchIds,categoryId;
    List<MenuModel> menuModels;

    RecyclerView recyclerView;
    List<InvoicesModel> invoicesModelList;
    InvoicesAdapter invoicesAdapter;

    TextView tv_allprice,tv_sum;

    AlertDialog alert11;
    AlertDialog.Builder builder1;

    String SoldById;

    private DatabaseHelperKiotViet databaseHelperKiotViet;
    private KiotVietModel kiotVietModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoices);
        databaseHelperKiotViet = new DatabaseHelperKiotViet(this);
        //Get data from Purchase Activity
        GetDataFromPurchaseActivity();
        //init param kiot viet
        GetParamForKiotViet();
        //init View
        InitView();
    }

    private void InitView() {
        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_create_invoices);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Hóa đơn");
        //other view
        tv_allprice = findViewById(R.id.tv_allprice);
        tv_sum = findViewById(R.id.tv_sum);
        recyclerView = findViewById(R.id.recyclerView_hd);
        invoicesModelList = new ArrayList<>();
        invoicesAdapter = new InvoicesAdapter(this,invoicesModelList,this);
        initRecyclerView();
        findViewById(R.id.btn_create_invoices).setOnClickListener(view -> CreateInvoices());
        //findViewById(R.id.btn_sell_direct).setOnClickListener(view -> SellDirect());
    }

    private void SellDirect() {
        Toast.makeText(this,"Sell direct",Toast.LENGTH_SHORT).show();
    }

    private void CreateInvoices() {
        CallWaiting();
        SendRequestCreateInvoices();
    }

    private void CallWaiting(){
        builder1 = new AlertDialog.Builder(CreateInvoicesActivity.this);
        alert11 = builder1.create();
        builder1.setMessage("waiting...");
        builder1.setCancelable(false);

        alert11 = builder1.create();
        alert11.show();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        gridLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(invoicesAdapter);
        FletchDataRecyclerView();
    }

    private void FletchDataRecyclerView() {
        int sum = 0;
        invoicesModelList.clear();
        for(int i = 0 ; i < menuModels.size() ; i++){
            InvoicesModel invoicesModel = new InvoicesModel();
            invoicesModel.setName(menuModels.get(i).getName());
            invoicesModel.setPrice(menuModels.get(i).getPrice());
            sum += Integer.parseInt(menuModels.get(i).getPrice());
            invoicesModel.setSelected(1);
            invoicesModelList.add(invoicesModel);
        }
        StringBuilder s = new StringBuilder();
        int numLength = String.valueOf(sum).length();
        for (int j =0; j < numLength; j++) {
            if ((numLength-j)%3 == 0 && j != 0) {
                s.append(".");
            }
            s.append(String.valueOf(sum).charAt(j));
        }
        tv_allprice.setText(s.toString());
        tv_sum.setText(String.valueOf(menuModels.size()));
        invoicesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void GetDataFromPurchaseActivity(){
        menuModels = (List<MenuModel>) getIntent().getSerializableExtra("DataInvoices");
    }

    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(CreateInvoicesActivity.this,R.style.AlertDialogTheme);
        builder1.setMessage("Hủy bỏ đặt hàng?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Đồng ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(CreateInvoicesActivity.this,PurchaseActivity.class));
                        CreateInvoicesActivity.this.finish();
                    }
                });
        builder1.setNegativeButton(
                "Không",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void GetParamForKiotViet(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        branchIds = preferences.getString("idChinhanh", getString(R.string.idChinhanh));
        categoryId = "";
        SoldById = preferences.getString("idsold",getString(R.string.idsold));
    }

    @Override
    public void OnclickIncDec(InvoicesModel invoicesModel,int i) {
        if(i == 1){
            invoicesModel.setSelected(invoicesModel.getSelected() + 1);
            String temp = String.valueOf(Integer.parseInt(tv_allprice.getText().toString().replace(".","")) + Integer.parseInt(invoicesModel.getPrice()));
            StringBuilder s = new StringBuilder();
            int numLength = temp.length();
            for (int j =0; j < numLength; j++) {
                if ((numLength-j)%3 == 0 && j != 0) {
                    s.append(".");
                }
                s.append(temp.charAt(j));
            }
            tv_allprice.setText(s.toString());
            tv_sum.setText( String.valueOf(Integer.parseInt(tv_sum.getText().toString()) + 1) );
            invoicesAdapter.notifyDataSetChanged();
        }else if(i == -1){
            invoicesModel.setSelected(invoicesModel.getSelected() - 1);
            String temp = String.valueOf(Integer.parseInt(tv_allprice.getText().toString().replace(".","")) - Integer.parseInt(invoicesModel.getPrice()));
            StringBuilder s = new StringBuilder();
            int numLength = temp.length();
            for (int j =0; j < numLength; j++) {
                if ((numLength-j)%3 == 0 && j != 0) {
                    s.append(".");
                }
                s.append(temp.charAt(j));
            }
            tv_allprice.setText(s.toString());
            tv_sum.setText( String.valueOf(Integer.parseInt(tv_sum.getText().toString()) - 1) );
            invoicesAdapter.notifyDataSetChanged();
        }
    }

    private RequestCreateInvoices CreateRequestInvoices(){
        RequestCreateInvoices requestCreateInvoices = new RequestCreateInvoices();
        requestCreateInvoices.setBranchId(branchIds);
        requestCreateInvoices.setBranchName("Chi nhánh trung tâm");
        requestCreateInvoices.setTotalPayment(tv_allprice.getText().toString().replace(".",""));
        requestCreateInvoices.setMethod("Card");
        requestCreateInvoices.setSoldById(SoldById);
        requestCreateInvoices.setUsingCod(true);
        requestCreateInvoices.setStatus(3);
        List<InvoiceDetail> invoiceDetailList = new ArrayList<>();
        for(int i = 0 ; i < menuModels.size();i++){
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setProductId(menuModels.get(i).getId());
            Log.d(Constants.TAG,menuModels.get(i).getId() + "");
            invoiceDetail.setPrice(menuModels.get(i).getPrice());
            Log.d(Constants.TAG,menuModels.get(i).getPrice());
            invoiceDetail.setQuantity(String.valueOf(invoicesModelList.get(i).getSelected()));
            Log.d(Constants.TAG,String.valueOf(invoicesModelList.get(i).getSelected()));
            invoiceDetail.setDiscount(0);
            invoiceDetail.setDiscountRatio(0);
            invoiceDetailList.add(invoiceDetail);
        }
        requestCreateInvoices.setInvoiceDetails(invoiceDetailList);
        DeliveryDetail deliveryDetail = new DeliveryDetail();
        deliveryDetail.setLocationId("700000");
        deliveryDetail.setStatus(0);
        deliveryDetail.setPrice(tv_allprice.getText().toString().replace(".",""));
        requestCreateInvoices.setDeliveryDetail(deliveryDetail);
        return requestCreateInvoices;
    }

    private void SendRequestCreateInvoices(){
        APIUtils apiUtils = new APIUtils("https://public.kiotapi.com/");
        SOService mService = apiUtils.getSOService();
        mService.getReturnInvoicesAfterCreate("Bearer " + MainActivity.accessToken,CreateRequestInvoices()).enqueue(new Callback<ReturnInvoices>() {
            @Override
            public void onResponse(Call<ReturnInvoices> call, final Response<ReturnInvoices> response) {
                Log.d(Constants.TAG, response.message());
                if (response.message().equals("OK")) {
                    //do something...
                    Log.d(Constants.TAG, String.valueOf(response.body().getId()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert11.dismiss();
                            //Toast.makeText(CreateInvoicesActivity.this,"Id = " + response.body().getId() ,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateInvoicesActivity.this,TapActivityForBooking.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("InvoicesId",String.valueOf(response.body().getId()));
                            bundle.putString("TotalPayment",tv_allprice.getText().toString().replace(".",""));
                            bundle.putString("InvoicesCode",response.body().getCode());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            CreateInvoicesActivity.this.finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert11.dismiss();
                            PurchaseActivity.call_synchronized = 0;
                            Toast.makeText(CreateInvoicesActivity.this,"Không đủ hàng trong kho, mời bạn chọn món khác",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ReturnInvoices> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert11.dismiss();
                        Toast.makeText(CreateInvoicesActivity.this,"fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
