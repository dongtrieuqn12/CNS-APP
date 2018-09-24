package com.example.user.myfinalprojectssc_ver1.Activities;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myfinalprojectssc_ver1.API.getAccessToken;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.ByteUtils;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.CardData;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.DesfireDep;
import com.example.user.myfinalprojectssc_ver1.MifareDesfire.PocketData;
import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.example.user.myfinalprojectssc_ver1.Others.Constants;
import com.example.user.myfinalprojectssc_ver1.Others.NFCReader_test;
import com.example.user.myfinalprojectssc_ver1.R;
import com.example.user.myfinalprojectssc_ver1.WorkerActivity.WorkerActivity;
import com.famoco.secommunication.ALPARProtocol;
import com.famoco.secommunication.SmartcardReader;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private int oldOrientation;
    private NfcAdapter nfcAdapter;

    private ImageView wave,image1,card;
    AlertDialog.Builder builder1;
    AlertDialog alert11;

    private boolean check = false;

    private ProgressDialog myDialog;

    //token
    public static String TokenOfTopUp;
    public static String accessToken;

    public static boolean hello = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        InitView();
        SetAnimation();

        //get Token
        TakeTokenTopUp();
        try {
            TakeAccessToken();
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void SetAnimation(){
        Animation sizingAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_nfc_tap);
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

    private void InitView(){
        builder1 = new AlertDialog.Builder(MainActivity.this);
        alert11 = builder1.create();
        wave = findViewById(R.id.image);
        image1 = findViewById(R.id.image1);
        card = findViewById(R.id.card);
        card.setVisibility(View.GONE);

        findViewById(R.id.btn_topup).setOnClickListener((v) -> topup());
        findViewById(R.id.btn_store).setOnClickListener((v) -> store());
        findViewById(R.id.btn_customer).setOnClickListener((v) -> purchase());
        TokenOfTopUp = "";
    }

    private void topup() {
        if(!check) {
            check = true;
//            AnimTranslate(image1, (int) image1.getPivotY(), -80);
//            AnimTranslate(wave, (int) wave.getPivotY(), -80);
            card.setVisibility(View.VISIBLE);
            SetAnimationForCard1();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    check = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            AnimTranslate(image1, (int) image1.getPivotY() - 80, (int) image1.getPivotY());
//                            AnimTranslate(wave, (int) wave.getPivotY() - 80, (int) wave.getPivotY());
                            card.setVisibility(View.GONE);
                            Log.d(Constants.TAG, "check anim");
                        }
                    });
                }
            }, 10000);
        }
    }

    private void SetAnimationForCard1(){
        Animation sizingAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_for_card_main_activity);
        AnimationSet animationSet = new AnimationSet(true);
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setDuration(1000);
        animationSet.addAnimation(sizingAnimation);
        animationSet.addAnimation(fadeOut);
        card.startAnimation(animationSet);
    }

    private void store() {
        startActivity(new Intent(MainActivity.this,StoreActivity.class));
    }

    private void purchase() {
        startActivity(new Intent(MainActivity.this,PurchaseActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            Toast.makeText(MainActivity.this,"go to setting",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onResume() {
        super.onResume();
        enableNfc();

        // avoid re-starting the App and loosing the tag by rotating screen
        oldOrientation = getRequestedOrientation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentfilter = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentfilter, null);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Restore screen mode
        setRequestedOrientation(oldOrientation);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        for (String tech : tag.getTechList()){
            if(tech.equals(IsoDep.class.getName())){
                Log.d(Constants.TAG,"isodep discovery");
                NFCReader nfcReader = NFCReader.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG), MainActivity.this);
                if(!check) {
                    builder1.setTitle("Xin Chào");
                    TextView tv = new TextView(this);
                    String hexString = "48e1bb9320c490c3b46e6720547269e1bb8175";
                    byte[] bytes = new byte[0];
                    try {
                        bytes = Hex.decodeHex(hexString.toCharArray());
                        Log.d(Constants.TAG,new String(bytes, "UTF-8"));
                        tv.setText(new String(bytes, "UTF-8"));
                    } catch (DecoderException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    tv.setTextColor(getResources().getColor(R.color.black));
                    tv.setTextSize(26);
                    tv.setGravity(Gravity.CENTER);
                    builder1.setView(tv);
                    builder1.setCancelable(false);
                    alert11 = builder1.create();
                    alert11.show();
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    alert11.dismiss();
                                }
                            });
                            timer.cancel();
                        }
                    }, 1000);
                }else {
                    try {
                        card.setVisibility(View.GONE);
                        GetInformation(nfcReader);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void GetInformation(NFCReader nfcReader) throws IOException {
        CardData cardData = new CardData();
        PocketData pocketData = new PocketData();
        DesfireDep desfireDep = null;
        desfireDep = new DesfireDep(nfcReader.card, NFCReader.smartcardReader);
        desfireDep.cardDataInit(cardData);
        if(cardData.isActive() && cardData.isTopupEnable() && cardData.isPurseEnable()){
            desfireDep.pocketDataInit(pocketData);
            String PocketID = pocketData.getPocketID();
            Long PocketBalance = pocketData.getPocketBalance();
            Intent intent1 = new Intent(MainActivity.this,TopUpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("PocketID",PocketID);
            bundle.putLong("PocketBalance",PocketBalance);
            bundle.putString("Activity","MainActivity");
            intent1.putExtras(bundle);
            startActivity(intent1);
            check = false;
        }
    }

    private void enableNfc() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (!adapter.isEnabled()) {
            if (dialog == null) {
                dialog = new AlertDialog.Builder(this)
                        .setMessage("NFC is required to communicate with a contactless smart card. Do you want to enable NFC now?")
                        .setTitle("Enable NFC")
                        .setPositiveButton(android.R.string.yes, (dialog, id) -> startActivity(new Intent(Settings.ACTION_NFC_SETTINGS)))
                        .setNegativeButton(android.R.string.no, (dialog, id) -> {})
                        .create();
            }
            dialog.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void TakeTokenTopUp(){
        new AsyncTask<Void, Void, Response>() {
            Response response = null;
            StringBuilder stringBuilder = new StringBuilder();
            @Override
            protected Response doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n\t\"username\": \"admin\",\n\t\"password\": \"admin\"\n}");
                Request request = new Request.Builder()
                        .url(Constants.URL_MCP_ACCESS_TOKEN)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    return null;
                }
                return response;
            }

            @Override
            protected void onPostExecute(Response response) {
                super.onPostExecute(response);
                try {
                    TokenOfTopUp = response.header("Authorization");
                } catch (NullPointerException e){
                    Log.d(Constants.TAG,e.toString());
                }

            }
        }.execute();
    }

    private void TakeAccessToken() throws ExecutionException, InterruptedException, JSONException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String client_id = preferences.getString("client_id", getString(R.string.client_id));
        String client_secret = preferences.getString("client_secret",getString(R.string.client_secret));
        getAccessToken getAccessToken = new getAccessToken("scopes=PublicApi.Access&grant_type=client_credentials&client_id=" + client_id + "&client_secret=" + client_secret);
        getAccessToken.execute();
        String s = getAccessToken.get();
        JSONObject jsonObject = new JSONObject(s);
        accessToken = jsonObject.getString("access_token");
        Log.d(Constants.TAG,accessToken);
    }

}
