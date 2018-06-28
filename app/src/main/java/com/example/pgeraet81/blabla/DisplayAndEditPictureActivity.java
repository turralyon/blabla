package com.example.pgeraet81.blabla;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
public class DisplayAndEditPictureActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ".EditActivity";
    private static final int WIDTH = 864;
    private static final int HEIGHT = 1536;

    private ArrayList<OverlayImage> overlayItems = new ArrayList<>();
    private Bitmap bitmap;
    private Canvas canvas;
    private Button btnConfetti;
    private Button btnHat;
    private Button btnMoustache;
    private Button btnStar;
    private Button btnNextScreen;
    private ImageView imageViewConfetti;
    private ImageView imageViewHat;
    private ImageView imageViewStar;
    private ImageView imageViewMoustache;
    ImageView imageViewPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_and_edit_picture);

        Intent intent = getIntent();
        Uri picture = Uri.parse(intent.getStringExtra("profile"));

        //button declarations
        btnHat = findViewById(R.id.btnHat);
        btnHat.setOnClickListener(this);
        btnConfetti = findViewById(R.id.btnConfetti);
        btnConfetti.setOnClickListener(this);
        btnMoustache = findViewById(R.id.btnMoustache);
        btnMoustache.setOnClickListener(this);
        btnStar = findViewById(R.id.btnStar);
        btnStar.setOnClickListener(this);
        btnNextScreen = findViewById(R.id.btnNext);
        btnNextScreen.setOnClickListener(this);

        //imageview declarations
        imageViewConfetti = findViewById(R.id.imageViewConfetti);
        imageViewHat = findViewById(R.id.imageViewHat);
        imageViewStar = findViewById(R.id.imageViewStar);
        imageViewMoustache = findViewById(R.id.imageViewMoustache);
        imageViewPicture = findViewById(R.id.imageView2);

        canvas = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picture);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
            canvas = new Canvas(bitmap);
            imageViewPicture.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Error getting a picture", Toast.LENGTH_LONG).show();
        }

        int minX = (int) imageViewPicture.getX() - WIDTH;
        int minY = (int) imageViewPicture.getY() - HEIGHT;
        int maxX = (int) imageViewPicture.getX() + WIDTH;
        int maxY = (int) imageViewPicture.getY() + HEIGHT;


        overlayItems.add(new OverlayImage(this, imageViewConfetti, maxX, maxY, minX, minY));
        overlayItems.add(new OverlayImage(this, imageViewHat, maxX, maxY, minX, minY));
        overlayItems.add(new OverlayImage(this, imageViewStar, maxX, maxY, minX, minY));
        overlayItems.add(new OverlayImage(this, imageViewMoustache, maxX, maxY, minX, minY));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnHat:
                imageViewHat.setImageResource(R.drawable.hat);

                break;
            case R.id.btnConfetti:
                int[] cards = {R.drawable.confetti1, R.drawable.confetti2, R.drawable.confetti3, R.drawable.confetti4};
                Random r = new Random();
                int n = r.nextInt(4);
                imageViewConfetti.setImageResource(cards[n]);
                break;
            case R.id.btnStar:
                imageViewStar.setImageResource(R.drawable.star);

                break;
            case R.id.btnMoustache:
                imageViewMoustache.setImageResource(R.drawable.mustache);

                break;

        }
    }

    }
