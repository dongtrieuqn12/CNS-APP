package com.example.user.myfinalprojectssc_ver1.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.myfinalprojectssc_ver1.Adapter.StoreOneInvoicesAdapter;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.Booking;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices.getAllDataInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.ReturnInvoices.ReturnInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.UpdateInvoices.DeliveryDetail;
import com.example.user.myfinalprojectssc_ver1.Model.UpdateInvoices.PartnerDelivery;
import com.example.user.myfinalprojectssc_ver1.Model.UpdateInvoices.UpdateInvoices;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.KiotVietModel;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.APIUtils;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.SOService;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperKiotViet;
import com.example.user.myfinalprojectssc_ver1.Table.StoreOneInvoicesModel;
import com.example.user.myfinalprojectssc_ver1.WorkerActivity.WorkerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends WorkerActivity {

    private ImageView wave,image1;
    public FrameLayout frameLayout;
    public LinearLayout linearLayout;
    TextView number_invoices_store,totalPayment_store,date_time_store_1;
    RecyclerView recyclerView_store;

    public List<StoreOneInvoicesModel> storeOneInvoicesModelList;
    StoreOneInvoicesAdapter storeOneInvoicesAdapter;

    DesfireDep desfireDep;
    public List<Booking> bookings;

    public boolean done = false;

    boolean check = true;

    private NFCReader nfcReader1;
    int count = 0;
    ProgressDialog progressDialog;

    private DatabaseHelperKiotViet databaseHelperKiotViet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        databaseHelperKiotViet = new DatabaseHelperKiotViet(this);

        InitView();
        InitRecyclerView();
    }

    private void InitView() {
        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_store);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Căn tin CNS");
        //init View
        frameLayout = findViewById(R.id.framelayout);
        linearLayout = findViewById(R.id.store_linear_layout);
        linearLayout.setVisibility(View.GONE);
        wave = findViewById(R.id.image);
        recyclerView_store = findViewById(R.id.recyclerView_store);
        number_invoices_store = findViewById(R.id.number_invoices_store);
        totalPayment_store = findViewById(R.id.totalPayment_store);
        date_time_store_1 = findViewById(R.id.date_time_store_1);
        SetAnimation();

        bookings = new ArrayList<>();

        findViewById(R.id.btn_done).setOnClickListener(view -> SetDone());
        findViewById(R.id.btn_reload).setOnClickListener(view -> Reload());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý");
    }

    private void Reload() {
        check = true;
        Default();
    }

    private void SetDone() {
        if(done){
            done = false;
            bookings.get(0).setOrderStatus("00");
            try {
                if (nfcReader1.card.isConnected()) {
                    desfireDep.updateOrderLog(bookings.get(0), 0);
                    if (desfireDep.commitTransaction()) {
                        UpdateInvoices(bookings.get(0).getOrderNumber());
                        Intent intent = new Intent(StoreActivity.this, InvoicesSelectedActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("idInvoices", bookings.get(0).getOrderNumber());
                        bundle.putString("Activity", "StoreActivity");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        StoreActivity.this.finish();
                    }
                } else {
                    Toast.makeText(StoreActivity.this,"Thẻ bị disconnect",Toast.LENGTH_SHORT).show();
                    Default();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Default(){
        check = true;
        linearLayout.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        SetAnimation();
        bookings.clear();
        storeOneInvoicesModelList.clear();
    }

    private void InTap() {
        frameLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        check = false;
        done = true;
    }

    //init Recycler View
    private void InitRecyclerView() {
        storeOneInvoicesModelList = new ArrayList<>();
        storeOneInvoicesAdapter = new StoreOneInvoicesAdapter(storeOneInvoicesModelList,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        recyclerView_store.setLayoutManager(mLayoutManager);
        recyclerView_store.setItemAnimator(new DefaultItemAnimator());
        recyclerView_store.setAdapter(storeOneInvoicesAdapter);
    }

    public void SetAnimation(){
        Animation sizingAnimation = AnimationUtils.loadAnimation(StoreActivity.this, R.anim.anim_nfc_tap);
        sizingAnimation.setDuration(1500);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1500);
        fadeOut.setRepeatCount(Animation.INFINITE);
        fadeOut.setRepeatMode(Animation.RESTART);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(sizingAnimation);
        animation.addAnimation(fadeOut);
        wave.startAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settlement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settlement) {
            Intent intent = new Intent(this,SettlementActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void functionProcess(NFCReader nfcReader) {
        if(check) {
            runOnUiThread(() -> progressDialog.show());
            check = false;
            this.nfcReader1 = nfcReader;
            bookings.clear();
            if (nfcReader1.card.isConnected()) {
                desfireDep = new DesfireDep(nfcReader1.card, NFCReader.smartcardReader);
                try {
                    desfireDep.selectApp("0400DF");
                    desfireDep.authenDivInp("2B", "01", Constants.DIVINP);
                    bookings = desfireDep.orderListInit();
                    if(bookings.get(0).getOrderStatus().equals("01")){
                        GetDataInvoicesSelected(bookings.get(0).getOrderNumber());
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(StoreActivity.this,"Không có hóa đơn nào được đặt",Toast.LENGTH_SHORT).show();
                            Default();
                            progressDialog.dismiss();
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(StoreActivity.this,"Thẻ bị disconnect",Toast.LENGTH_SHORT).show();
                    Default();
                    progressDialog.dismiss();
                });
            }
        }
    }

    private void GetDataInvoicesSelected(String id){
        SOService mService;
        APIUtils apiUtils = new APIUtils("https://public.kiotapi.com/");
        mService = apiUtils.getSOService();
        mService.ReturnDataInvoicesSelected("Bearer " + MainActivity.accessToken,id).enqueue(new Callback<getAllDataInvoices>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<getAllDataInvoices> call, final Response<getAllDataInvoices> response) {
                Log.d("hodongtrieuTest1",response.message());
                if(response.message().equals("OK")){
                    for(int i = 0 ; i < response.body().getInvoiceDetails().size() ; i ++){
                        StoreOneInvoicesModel storeOneInvoicesModel = new StoreOneInvoicesModel();
                        storeOneInvoicesModel.setCode(response.body().getInvoiceDetails().get(i).getProductCode());
                        //storeOneInvoicesModel.setImages();
                        storeOneInvoicesModel.setName(response.body().getInvoiceDetails().get(i).getProductName());
                        storeOneInvoicesModel.setPrice(response.body().getInvoiceDetails().get(i).getPrice().toString());
                        storeOneInvoicesModel.setQuantity(response.body().getInvoiceDetails().get(i).getQuantity());
                        KiotVietModel kiotVietModel = databaseHelperKiotViet.QueryCategory(response.body().getInvoiceDetails().get(i).getProductId());
                        storeOneInvoicesModel.setImages(kiotVietModel.getImages());
                        storeOneInvoicesModelList.add(storeOneInvoicesModel);
                    }
                    runOnUiThread(() -> {
                        number_invoices_store.setText(response.body().getCode());
                        StringBuilder s = new StringBuilder();
                        int numLength = String.valueOf(response.body().getTotalPayment()).length();
                        for (int j =0; j < numLength; j++) {
                            if ((numLength-j)%3 == 0 && j != 0) {
                                s.append(".");
                            }
                            s.append(String.valueOf(response.body().getTotalPayment()).charAt(j));
                        }
                        totalPayment_store.setText(s.toString() + " VNĐ");
                        date_time_store_1.setText(bookings.get(0).getOrderDatetime().substring(6,8) + "/" +
                                bookings.get(0).getOrderDatetime().substring(4,6) + "/" +
                                bookings.get(0).getOrderDatetime().substring(0,4) + " " +
                                bookings.get(0).getOrderDatetime().substring(8,10) + ":" +
                                bookings.get(0).getOrderDatetime().substring(10,12) + ":" +
                                bookings.get(0).getOrderDatetime().substring(12,14));
                        InTap();
                        progressDialog.dismiss();
                        storeOneInvoicesAdapter.notifyDataSetChanged();
                    });
                } else {
                    runOnUiThread(() -> {
                        Default();
                        progressDialog.dismiss();
                        Toast.makeText(StoreActivity.this,"Mời bạn chạm lại thẻ để nhận món sau ít phút",Toast.LENGTH_SHORT).show();
                    });
                }
            }
            @Override
            public void onFailure(Call<getAllDataInvoices> call, Throwable t) {
                runOnUiThread(() -> {
                    Default();
                    progressDialog.dismiss();
                    Toast.makeText(StoreActivity.this,"Mời bạn chạm lại thẻ để nhận món sau ít phút",Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void UpdateInvoices(String id){
        SOService mService;
        APIUtils apiUtils = new APIUtils("https://public.kiotapi.com/");
        mService = apiUtils.getSOService();
        mService.RETURN_INVOICES_CALL("Bearer " + MainActivity.accessToken,id,CreateBodyUpdate()).enqueue(new Callback<ReturnInvoices>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ReturnInvoices> call, final Response<ReturnInvoices> response) {
                Log.d("hodongtrieuTest1",response.message());
                if(response.message().equals("OK")) {
                    runOnUiThread(() -> Toast.makeText(StoreActivity.this,"Hoàn thành",Toast.LENGTH_SHORT).show());
                } else {
                    UpdateInvoices(id);
                }
            }
            @Override
            public void onFailure(Call<ReturnInvoices> call, Throwable t) {
                UpdateInvoices(id);
            }
        });
    }

    private UpdateInvoices CreateBodyUpdate(){
        UpdateInvoices updateInvoices = new UpdateInvoices();
        updateInvoices.setUsingCod(true);
        DeliveryDetail deliveryDetail = new DeliveryDetail();
        deliveryDetail.setStatus(3);
        PartnerDelivery partnerDelivery = new PartnerDelivery();
        partnerDelivery.setName("CNS tổng công ty");
        deliveryDetail.setPartnerDelivery(partnerDelivery);
        updateInvoices.setDeliveryDetail(deliveryDetail);
        return updateInvoices;
    }
}
