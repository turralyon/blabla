package com.example.pgeraet81.blabla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DisplayAndEditPictureActivity extends AppCompatActivity implements View.OnClickListener {


    private int width;
    private int height;
    private ConstraintLayout layout;
    private Uri tempFileUri;
    private ArrayList<OverlayImage> overlayItems = new ArrayList<>();
    private Bitmap bitmap;
    private Bitmap mutuableBitmap;
    private Button btnConfetti;
    private Button btnHat;
    private Button btnMoustache;
    private Button btnStar;
    private Button btnNextScreen;
    private ImageView imageViewConfetti;
    private ImageView imageViewHat;
    private ImageView imageViewStar;
    private ImageView imageViewMoustache;
    private ImageView imageViewPicture;

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
        imageViewStar.setVisibility(View.INVISIBLE);
        imageViewMoustache.setVisibility(View.INVISIBLE);
        imageViewHat.setVisibility(View.INVISIBLE);
        imageViewConfetti.setVisibility(View.INVISIBLE);
        layout = findViewById(R.id.layoutView);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picture);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            mutuableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutuableBitmap);

            imageViewPicture.setImageBitmap(mutuableBitmap);

        } catch (Exception e) {
            Toast.makeText(this, "Error getting a picture", Toast.LENGTH_LONG).show();
        }

        int minX = (int) imageViewPicture.getX() - width;
        int minY = (int) imageViewPicture.getY() - height;
        int maxX = (int) imageViewPicture.getX() + width;
        int maxY = (int) imageViewPicture.getY() + height;


        overlayItems.add(new OverlayImage(this, imageViewConfetti, maxX, maxY, minX, minY));
        overlayItems.add(new OverlayImage(this, imageViewHat, maxX, maxY, minX, minY));
        overlayItems.add(new OverlayImage(this, imageViewStar, maxX, maxY, minX, minY));
        overlayItems.add(new OverlayImage(this, imageViewMoustache, maxX, maxY, minX, minY));


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnHat:
                imageViewHat.setVisibility(View.VISIBLE);
                imageViewHat.setImageResource(R.drawable.hat);

                break;
            case R.id.btnConfetti:
                imageViewConfetti.setVisibility(View.VISIBLE);
                int[] cards = {R.drawable.confetti1, R.drawable.confetti2, R.drawable.confetti3, R.drawable.confetti4};
                Random r = new Random();
                int n = r.nextInt(4);
                imageViewConfetti.setImageResource(cards[n]);
                break;
            case R.id.btnStar:
                imageViewStar.setVisibility(View.VISIBLE);
                imageViewStar.setImageResource(R.drawable.star);


                break;
            case R.id.btnMoustache:
                imageViewMoustache.setVisibility(View.VISIBLE);
                imageViewMoustache.setImageResource(R.drawable.mustache);

                break;
            case R.id.btnNext:
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri tempFileUri = saveImage(v);

                startAddDateActivity(tempFileUri);

        }
    }


    public Uri saveImage(View view) {
        final File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp");
        boolean success = false;
        myDir.deleteOnExit();
        final Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        final String fname = "temp" + n + ".png";
        myDir.mkdirs();
        File image = new File(myDir, fname);

        tempFileUri = Uri.fromFile(image);
        myDir.setReadable(true);
        myDir.setWritable(true);
        image.deleteOnExit();

        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(image);
            getScreenViewBitmap(view).compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            success = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "not saved", Toast.LENGTH_LONG).show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(image);
            scanIntent.setData(contentUri);
            sendBroadcast(scanIntent);
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://mnt/sdcard/" + Environment.getExternalStorageDirectory())));
        }
        return tempFileUri;
    }


    private void startAddDateActivity(Uri data) {
        Intent intent = new Intent(getApplicationContext(), AddDateActivity.class);
        intent.putExtra("temp", data.toString());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }




    private Bitmap getScreenViewBitmap(View v) {

        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        Bitmap bm1 = layout.getDrawingCache();
        return bm1;
    }
}

