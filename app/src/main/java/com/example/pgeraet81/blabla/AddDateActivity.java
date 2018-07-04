package com.example.pgeraet81.blabla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class AddDateActivity extends AppCompatActivity implements View.OnClickListener {
    private Employee employee;
    private TextView txtName;
    private ImageView imageViewTemp;
    private ImageView imageViewDate;
    private Bitmap bitmap;
    private Canvas canvas;
    private Uri ResultFileUri;
    private Month month;
    private ImageButton btnShare;
    private Button btnDate;
    private static final int WIDTH = 864;
    private static final int HEIGHT = 1536;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
        Intent intent = getIntent();
        Uri picture = Uri.parse(intent.getStringExtra("temp"));

        txtName = findViewById(R.id.editText);
        imageViewTemp = findViewById(R.id.imageViewTemp);
        imageViewDate = findViewById(R.id.imageViewDate);
        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);
        btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);

        canvas = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picture);
            bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, false);
            Bitmap mutuableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvas = new Canvas(mutuableBitmap);
            imageViewTemp.setImageBitmap(mutuableBitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Error getting a picture", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShare:
                Uri ResultFileUri = saveImage(view);
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("image/png");
                intentShare.putExtra(Intent.EXTRA_STREAM, ResultFileUri);
                intentShare.putExtra(Intent.EXTRA_SUBJECT,"" );
                intentShare.putExtra(Intent.EXTRA_TEXT, "Werknemer van de Maand is:" + txtName.getText().toString());
                startActivity(Intent.createChooser(intentShare, "Delen met: "));

                break;
            case R.id.btnDate:
                month = new Month();
                imageViewDate.setVisibility(View.VISIBLE);
                imageViewDate.setImageResource(month.getCurrentMonth());
                break;

        }
    }

    public Uri saveImage(View view) {
        employee = new Employee();
        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/gallery");
        boolean success = false;
        final Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        final String fname = "temp" + n + ".png";
        myDir.mkdirs();
        File image = new File(myDir, fname);
        imageViewTemp.setDrawingCacheEnabled(true);
        ResultFileUri = Uri.fromFile(image);
        employee.setFinalPicture(ResultFileUri);
        myDir.setReadable(true);
        myDir.setWritable(true);


        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(image);

            Bitmap mainBitmap = ((BitmapDrawable)imageViewTemp.getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            Bitmap dateBitmap = ((BitmapDrawable)imageViewDate.getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            dateBitmap = Bitmap.createScaledBitmap(dateBitmap, 400, 200, false);
            Canvas canvas = new Canvas(mainBitmap);
            canvas.drawBitmap(mainBitmap.copy(Bitmap.Config.ARGB_8888, true), 1, 1, null);
            canvas.drawBitmap(dateBitmap.copy(Bitmap.Config.ARGB_8888, true), 358, 25, null);



            mainBitmap.compress(Bitmap.CompressFormat.PNG, 75, outStream);

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
        return ResultFileUri;
    }
}

