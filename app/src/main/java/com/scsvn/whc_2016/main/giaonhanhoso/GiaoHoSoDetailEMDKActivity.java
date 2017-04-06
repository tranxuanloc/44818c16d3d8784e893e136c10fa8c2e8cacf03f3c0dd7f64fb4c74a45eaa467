package com.scsvn.whc_2016.main.giaonhanhoso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.scsvn.whc_2016.utilities.Const;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;
import java.util.List;

public class GiaoHoSoDetailEMDKActivity extends GiaoHoSoDetailActivity implements Scanner.DataListener, Scanner.StatusListener {
    public static final String TAG = GiaoHoSoDetailEMDKActivity.class.getSimpleName();

    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AsyncEMDK().execute();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (emdkManager != null) {
            barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
            initScanner();
            setTrigger();
            setDecoders();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deInitScanner();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        deInitScanner();
        if (emdkManager != null) {
            emdkManager.release(EMDKManager.FEATURE_TYPE.BARCODE);
        }
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
            for (ScanDataCollection.ScanData data : scanData) {

                String barcode = data.getData();
                new AsyncUiControlUpdate().execute(barcode);
            }
        }
    }


    @Override
    public void onStatus(StatusData statusData) {
        StatusData.ScannerStates state = statusData.getState();
        switch (state) {
            case IDLE:
                try {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    scanner.read();
                } catch (ScannerException e) {

                }
                break;
            case WAITING:
                break;
            case SCANNING:
                break;
            case DISABLED:
                break;
            case ERROR:
                break;
            default:
                break;
        }
    }


    private void initScanner() {

        if (scanner == null) {
            List<ScannerInfo> deviceList = barcodeManager.getSupportedDevicesInfo();
            int numberDevices = deviceList.size();
            if (numberDevices != 0) {
                scanner = barcodeManager.getDevice(deviceList.get(numberDevices > 0 ? 1 : 0));
            } else {
                Snackbar.make(snackBarView, "Failed to get the specified scanner device! Please close and restart the application.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    Snackbar.make(snackBarView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(snackBarView, "Failed to initialize the scanner device.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void setTrigger() {
        if (scanner == null) {
            initScanner();
        }
        if (scanner != null) {
            scanner.triggerType = Scanner.TriggerType.HARD;
        }
    }

    private void setDecoders() {
        if (scanner == null)
            initScanner();
        if (scanner != null)
            try {
                ScannerConfig config = scanner.getConfig();
                config.decoderParams.ean8.enabled = true;
                config.decoderParams.ean13.enabled = true;
                config.decoderParams.code39.enabled = true;
                config.decoderParams.code128.enabled = true;
                scanner.setConfig(config);
            } catch (ScannerException e) {
                Snackbar.make(snackBarView, "setDecoders: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                scanner.disable();

            } catch (ScannerException e) {
                Snackbar.make(snackBarView, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner.removeDataListener(this);
            scanner.removeStatusListener(this);
            try {
                scanner.release();
            } catch (ScannerException e) {
                Snackbar.make(snackBarView, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner = null;
        }
    }


    private class AsyncEMDK extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EMDKResults results = EMDKManager.getEMDKManager(GiaoHoSoDetailEMDKActivity.this, new EMDKManager.EMDKListener() {
                @Override
                public void onOpened(EMDKManager emdkManager) {
                    GiaoHoSoDetailEMDKActivity.this.emdkManager = emdkManager;
                    barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
                    initScanner();
                    setTrigger();
                    setDecoders();
                }

                @Override
                public void onClosed() {
                    if (emdkManager != null) {
                        emdkManager.release();
                        emdkManager = null;
                    }
                    Snackbar.make(snackBarView, "onClosed: " + "EMDK closed unexpectedly! Please close and restart the application.", Snackbar.LENGTH_LONG).show();
                }
            });
            if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS)
                Snackbar.make(snackBarView, "Có lỗi xảy ra. Không thể thực hiện Scan", Snackbar.LENGTH_SHORT).show();
        }
    }
}
