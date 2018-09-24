package com.example.user.myfinalprojectssc_ver1.WorkerActivity;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.myfinalprojectssc_ver1.NFCReader.NFCReader;
import com.famoco.secommunication.SmartcardReader;

/**
 * Created by Ho Dong Trieu on 08/28/2018
 */
public abstract class WorkerActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    @Override
    protected void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stopWorker();
        disableReaderMode();
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        NFCReader nfcReader = NFCReader.get(tag, this);
        Log.d("HDT2","in tag discorvery");
        if (nfcReader != null) {
            //StartNFCConfig(nfcReader);
            Log.d("HDT2","in tag discorvery");
            functionProcess(nfcReader);
        }
    }

    protected abstract void functionProcess(NFCReader nfcReader);

    private void StartNFCConfig(NFCReader nfcReader){
        nfcReader.powerOn();
    }

    private void enableReaderMode() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        int timeout = 500;
        Bundle bundle = new Bundle();
        bundle.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, timeout * 10);
        adapter.enableReaderMode(this, this,
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                bundle);
    }

    private void disableReaderMode() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter != null){
            adapter.disableReaderMode(this);
        }
    }
}
