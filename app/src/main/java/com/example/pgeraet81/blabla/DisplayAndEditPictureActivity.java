package com.example.pgeraet81.blabla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DisplayAndEditPictureActivity extends AppCompatActivity implements View.OnClickListener {
    private Bitmap bitmap;
    private Canvas canvas;
    private OverlayImage hat;
    private static final int WIDTH = 864;
    private static final int HEIGHT = 1536;
    private ArrayList<OverlayImage> items = new ArrayList<>();
    private int currentItem = 0;
    private ImageView imageViewConfetti;
    private ImageView imageViewHat;
    private Button btnConfetti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_and_edit_picture);
        Intent intent = getIntent();
        Uri picture = Uri.parse(intent.getStringExtra("profile"));
        ImageView imageView = findViewById(R.id.imageView2);
        imageViewConfetti = findViewById(R.id.imageViewConfetti);
        btnConfetti = findViewById(R.id.btnConfetti);
        Button btnHat = findViewById(R.id.btnHat);
        btnConfetti.setOnClickListener(this);
        canvas = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picture);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);

            canvas = new Canvas(bitmap);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "error!", Toast.LENGTH_LONG).show();
        }
        ImageView item2 = findViewById(R.id.imageViewConfetti);
        ImageView item1 = findViewById(R.id.imageView3);
        int minX = (int) imageView.getX() - WIDTH;
        int minY = (int) imageView.getY() - HEIGHT;
        int maxX = (int) imageView.getX() + WIDTH;
        int maxY = (int) imageView.getY() + HEIGHT;

        items.add(new OverlayImage(this, item1, maxX, maxY, minX, minY));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHat:
                imageViewHat.setImageResource(R.drawable.hat);

                break;
            case R.id.btnConfetti:
                int[] cards={R.drawable.confetti1,R.drawable.confetti2,R.drawable.confetti3,R.drawable.confetti4};
                Random r = new Random();
                int n=r.nextInt(4);
                imageViewConfetti.setImageResource(cards[n]);
                break;
        }
    }
}