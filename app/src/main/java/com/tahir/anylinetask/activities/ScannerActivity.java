package com.tahir.anylinetask.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.tahir.anylinetask.R;

import at.nineyards.anyline.core.RunFailure;
import io.anyline.AnylineDebugListener;
import io.anyline.plugin.ScanResultListener;
import io.anyline.plugin.ocr.OcrScanResult;
import io.anyline.plugin.ocr.OcrScanViewPlugin;
import io.anyline.view.ScanView;


public class ScannerActivity extends AppCompatActivity implements AnylineDebugListener {
    private static final String TAG = ScannerActivity.class.getSimpleName();
    private ScanView documentScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the flag to keep the screen on (otherwise the screen may go dark during scanning)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.ocr_layout);

        documentScanView = (ScanView) findViewById(R.id.scan_view);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start the scanning
        documentScanView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();


        documentScanView.stop();
        documentScanView.releaseCameraInBackground();
    }

    void init() {

        try {
            documentScanView.init("document_view_config.json");
        } catch (Exception e) {
            e.printStackTrace();
        }


        OcrScanViewPlugin scanViewPlugin = (OcrScanViewPlugin) documentScanView.getScanViewPlugin();

        scanViewPlugin.addScanResultListener(new ScanResultListener<OcrScanResult>() {
            @Override
            public void onResult(OcrScanResult anylineOcrResult) {

                String result = anylineOcrResult.getResult();

                if (!result.isEmpty()) {

                    Log.d("##", result.toString());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",result);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }

        });


    }

    @Override
    public void onDebug(String s, Object o) {
        Log.d("###", "debug called");

    }

    @Override
    public void onRunSkipped(RunFailure runFailure) {
        Log.d("###", "failure called");

    }
}