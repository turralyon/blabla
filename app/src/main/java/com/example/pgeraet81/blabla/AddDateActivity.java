package com.example.pgeraet81.blabla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddDateActivity extends AppCompatActivity {
    private TextView txtName;
    private ImageView imageViewTemp;
    private Bitmap bitmap;
    private Canvas canvas;
    private static final int WIDTH = 864;
    private static final int HEIGHT = 1536;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
        Intent intent = getIntent();
        Uri picture = Uri.parse(intent.getStringExtra("temp"));

        txtName = findViewById(R.id.txtName);
        imageViewTemp = findViewById(R.id.imageViewTemp);

        canvas = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picture);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, false);
            Bitmap mutuableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvasTemp = new Canvas(mutuableBitmap);
            imageViewTemp.setImageBitmap(mutuableBitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Error getting a picture", Toast.LENGTH_LONG).show();
        }
    }


}