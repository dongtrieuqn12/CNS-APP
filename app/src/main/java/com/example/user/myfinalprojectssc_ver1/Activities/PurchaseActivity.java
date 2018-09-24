package com.example.user.myfinalprojectssc_ver1.Activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.Adapter.MenuMainAdapter;
import com.example.user.myfinalprojectssc_ver1.Model.Products.Product;
import com.example.user.myfinalprojectssc_ver1.ModelSQLite.KiotVietModel;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.Others.GridSpacingItemDecoration;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.APIUtils;
import com.example.user.myfinalprojectssc_ver1.Retrofit2.SOService;
import com.example.user.myfinalprojectssc_ver1.SQLiteDB.DatabaseHelperKiotViet;
import com.example.user.myfinalprojectssc_ver1.Table.MenuModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class PurchaseActivity extends AppCompatActivity implements MenuMainAdapter.PurchaseAdapterOnclick,SwipeRefreshLayout.OnRefreshListener {

    LinearLayout linear_all;

    private RecyclerView recycler_purchase;
    private MenuMainAdapter menuMainAdapter;
    private List<MenuModel> menuModelList;
    private SOService mService;

    //kiot viet
    private String branchIds,categoryId;
    private SearchView searchView;

    List<Integer> index;

    SwipeRefreshLayout swipe_container;

    private DatabaseHelperKiotViet databaseHelperKiotViet;
    private List<KiotVietModel> kiotVietModelList;

    public  static int call_synchronized = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        //initial toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Căn tin CNS");

        //init View
        SetInitView();
        InitDataKiot();
        SelectCategories("Tất cả","",false);
        InitRecyclerView();
        swipe_container.post(() -> {
            swipe_container.setRefreshing(true);
            // Fetching data from server
            switch (call_synchronized){
                case 0:
                    menuModelList.clear();
                    call_synchronized++;
                    databaseHelperKiotViet.clearDatabase();
                    PrepareData();
                    break;
                case 1:
                    menuModelList.clear();
                    ShowDataKiotViet();
                    break;
            }
        });
    }

    //init view
    private void SetInitView() {
        linear_all = findViewById(R.id.linear_all);
        recycler_purchase = findViewById(R.id.recycler_purchase);
        index = new ArrayList<>();
        findViewById(R.id.btn_reload).setOnClickListener(view -> ClearAll());
        findViewById(R.id.btn_done).setOnClickListener(view -> ChangeToMakeInvoices());

        databaseHelperKiotViet = new DatabaseHelperKiotViet(this);
        kiotVietModelList = new ArrayList<>();

        swipe_container = findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    //init data to connect to Kiot Viet
    private void InitDataKiot() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        branchIds = preferences.getString("idChinhanh", getString(R.string.idChinhanh));
        categoryId = "";
    }

    //init Recycler View
    private void InitRecyclerView() {
        menuModelList = new ArrayList<>();
        menuMainAdapter = new MenuMainAdapter(this,menuModelList,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recycler_purchase.setLayoutManager(mLayoutManager);
        recycler_purchase.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recycler_purchase.setItemAnimator(new DefaultItemAnimator());
        recycler_purchase.setAdapter(menuMainAdapter);
    }

    //set dp, using for config Recycler View item.
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //fetch data from Kiot Viet to show to Recycler View using retrofit2.
    private void PrepareData() {
        // Showing refresh animation before making http call
        swipe_container.setRefreshing(true);
        APIUtils apiUtils = new APIUtils("https://public.kiotapi.com/");
        mService = apiUtils.getSOService();
        mService.getAnswers("Bearer " + MainActivity.accessToken,categoryId,branchIds).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, retrofit2.Response<Product> response) {
                Log.d(Constants.TAG,response.message());
                if(response.message().equals("OK")) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        if(response.body().getData().get(i).getCode().startsWith("SP")) {
                            KiotVietModel kiotVietModel = new KiotVietModel();
                            kiotVietModel.setId_kiot(response.body().getData().get(i).getId());
                            kiotVietModel.setCode(response.body().getData().get(i).getCode());
                            kiotVietModel.setFullName(response.body().getData().get(i).getFullName());
                            if (response.body().getData().get(i).getAllowsSale()) {
                                kiotVietModel.setAllowsSale(1);
                            } else {
                                kiotVietModel.setAllowsSale(0);
                            }
                            kiotVietModel.setBasePrice(response.body().getData().get(i).getBasePrice().toString());
                            if (response.body().getData().get(i).getIsActive()) {
                                kiotVietModel.setIsActive(1);
                            } else {
                                kiotVietModel.setIsActive(0);
                            }
                            kiotVietModel.setProductId(response.body().getData().get(i).getInventories().get(0).getProductId());
                            kiotVietModel.setProductName(response.body().getData().get(i).getInventories().get(0).getProductName());
                            Log.d(Constants.TAG,response.body().getData().get(i).getInventories().get(0).getProductName());
                            kiotVietModel.setBranchId(response.body().getData().get(i).getInventories().get(0).getBranchId());
                            kiotVietModel.setOnHand(response.body().getData().get(i).getInventories().get(0).getOnHand());
                            Log.d(Constants.TAG,response.body().getData().get(i).getInventories().get(0).getOnHand().toString());
                            try {
                                kiotVietModel.setImages(response.body().getData().get(i).getImages().get(0));
                            } catch (NullPointerException e){
                                Log.d(Constants.TAG,"null pointer");
                            }
                            databaseHelperKiotViet.AddCatelogies(kiotVietModel);
                        }
                    }
                    Log.d(Constants.TAG,"finish get data...");
                    runOnUiThread(() -> {
                        ShowDataKiotViet();
                    });
                }else {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),"Đã xảy ra lỗi",Toast.LENGTH_SHORT).show();
                        ShowDataKiotViet();
                    });
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d(Constants.TAG,"hello");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> {
                            swipe_container.setRefreshing(false);
                            timer.cancel();
                        });
                    }
                },600);
                ShowDataKiotViet();
            }
        });
    }

    private void ShowDataKiotViet(){
        Log.d(Constants.TAG,"loading...");
        kiotVietModelList = databaseHelperKiotViet.GetAllCatelogies_Is_Active(1,1);
        for(int i = 0 ; i < kiotVietModelList.size() ; i++){
            if(kiotVietModelList.get(i).getOnHand() > 0) {
                MenuModel menuModel = new MenuModel();
                menuModel.setName(kiotVietModelList.get(i).getFullName());
                menuModel.setPrice(kiotVietModelList.get(i).getBasePrice() + "");
                menuModel.setImage(kiotVietModelList.get(i).getImages());
                menuModel.setId(kiotVietModelList.get(i).getId_kiot());
                menuModel.setVisible(0);
                menuModelList.add(menuModel);
            }
        }
        menuMainAdapter.notifyDataSetChanged(); //update view after fetch data.
        Log.d(Constants.TAG,"load ok");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    swipe_container.setRefreshing(false);
                    timer.cancel();
                });
            }
        }, 600);
    }

    //change to Create Invoices Activity.
    private void ChangeToMakeInvoices() {
        List<MenuModel> menuModels_temp = new ArrayList<>();
        for(int i = 0 ; i < menuModelList.size() ; i ++){
            if(menuModelList.get(i).getVisible() == 1){
                menuModels_temp.add(menuModelList.get(i));
            }
        }
        if(menuModels_temp.size() > 0) {
            Intent intent = new Intent(this, CreateInvoicesActivity.class);
            intent.putExtra("DataInvoices", (Serializable) menuModels_temp);
            startActivity(intent);
            this.finish();
        }else {
            Toast.makeText(this,"Mời bạn chọn món",Toast.LENGTH_SHORT).show();
        }
    }

    //clear data selected
    private void ClearAll() {
        for(int i = 0 ; i < menuModelList.size() ; i ++){
            menuModelList.get(i).setVisible(0);
        }
        menuMainAdapter.notifyDataSetChanged();
    }

    //chose type of categories
    private void SelectCategories(String name,String categoryId,boolean numbers){
        TextView valueTV = new TextView(this);
        valueTV.setText(name);
        valueTV.setTextSize(14);
        valueTV.setPadding(8,3,0,0);
        valueTV.setGravity(View.TEXT_ALIGNMENT_CENTER);
        valueTV.setTextColor(Color.BLACK);
        linear_all.addView(valueTV);
        //addData(categoryId,numbers);
    }
    //***************************query by text**********************************//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //filter recycler view when query submitted
                menuMainAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //filter when text change
                menuMainAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
    //***************************query by text**********************************//

    //refresh data.
    @Override
    public void onRefresh() {
        menuModelList.clear();
        //PrepareData();
        ShowDataKiotViet();
    }

    // On click item.
    @Override
    public void ClickPosition(MenuModel menuModel) {
        if(menuModel.getVisible() == 0) {
            menuModel.setVisible(1);
        }else {
            menuModel.setVisible(0);
        }
    }
}
