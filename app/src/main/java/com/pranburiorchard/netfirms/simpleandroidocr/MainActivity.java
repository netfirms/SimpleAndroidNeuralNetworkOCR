package com.pranburiorchard.netfirms.simpleandroidocr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pranburiorchard.netfirms.simpleandroidocr.customview.WritingView;

import static android.content.ContentValues.TAG;

/**
 * Created by taweechai on 16/15/2560.
 */

public class MainActivity extends AppCompatActivity {

    private WritingView wv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button clearBtn = findViewById(R.id.game_clear_btn);
        wv = findViewById(R.id.writingView);
        tv = findViewById(R.id.result_txt);
        TextView verifyBtn = findViewById(R.id.game_ok_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(getResources().getString(R.string.questionMark));
                wv.clear();
                wv.invalidate();
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(wv.testNN());
                wv.clear();
                wv.invalidate();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1001);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1002);
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1001:
                // BEGIN_INCLUDE(permission_result)
                // Received permission result for camera permission.
                Log.i(TAG, "Received response for SDCARD permission request.");

                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted, preview can be displayed
                    Log.i(TAG, "SDCARD permission has now been granted. Showing preview.");

                } else {
                    Log.i(TAG, "SDCARD permission was NOT granted.");

                }
                // END_INCLUDE(permission_result)

                break;
            case 1002:
                // BEGIN_INCLUDE(permission_result)
                // Received permission result for camera permission.
                Log.i(TAG, "Received response for SDCARD permission request.");

                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted, preview can be displayed
                    Log.i(TAG, "SDCARD permission has now been granted. Showing preview.");

                } else {
                    Log.i(TAG, "SDCARD permission was NOT granted.");

                }
                // END_INCLUDE(permission_result)

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
