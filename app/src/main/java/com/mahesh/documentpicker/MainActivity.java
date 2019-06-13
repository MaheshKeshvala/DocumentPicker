package com.mahesh.documentpicker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mahesh.documentpicker2.DocumentPickerClass;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int RequestCode = 1;
    ArrayList<File> file = new ArrayList<>();
    Button openImagePicker;
    TextView tvFileCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFileCount = (TextView) findViewById(R.id.tvFileCount);
        openImagePicker = (Button) findViewById(R.id.btnOptnImagePicker);
        openImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }

    public void openImagePicker() {
        DocumentPickerClass.browseDocuments(this, RequestCode, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        file = DocumentPickerClass.onActivityResult(this, requestCode, resultCode, data);
        tvFileCount.setText("Selected file count " + file.size());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DocumentPickerClass.onRequestPermissionsResult(this, permissions, grantResults);
    }
}
