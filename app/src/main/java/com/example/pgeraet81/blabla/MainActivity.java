package com.example.pgeraet81.blabla;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ALL_PERMISSIONS_RESULT = 232;
    private static final int SELECT_IMAGE = 121;
    private static final int Create_IMAGE = 321;


    Uri picUri;
    Bitmap myBitmap;
    private ArrayList<String> permission;
    private ArrayList<String> missing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        Button openCamera = findViewById(R.id.btnOpenCamera);
        openCamera.setOnClickListener(this);

        Button openGallery = findViewById(R.id.btnOpenGallery);
        openGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenGallery:
                Intent selectImageFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(selectImageFromGallery, SELECT_IMAGE);
                break;

            case R.id.btnOpenCamera:
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent selectImageFromCamera = new Intent(getPickImageChooserIntent());

                startActivityForResult(selectImageFromCamera,Create_IMAGE);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = (ImageView) findViewById(R.id.imageViewMoustache);

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                startDisplayAndEditPictureActivity(picUri);
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    startDisplayAndEditPictureActivity(picUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {


                bitmap = (Bitmap) data.getExtras().get("data");

                myBitmap = bitmap;



                imageView.setImageBitmap(myBitmap);

            }

        }

    }


    private void startDisplayAndEditPictureActivity(Uri data) {
        Intent intent = new Intent(getApplicationContext(), DisplayAndEditPictureActivity.class);
        intent.putExtra("profile", data.toString());
        startActivity(intent);
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.jpg"));
        }
        return outputFileUri;
    }

    private void requestPermission() {
        //write arraylist for permission
        permission = new ArrayList<>();
        permission.add(CAMERA);
        permission.add(WRITE_EXTERNAL_STORAGE);
        permission.add(READ_EXTERNAL_STORAGE);
        //write arraylist for missing permissions
        missing = findUnAskedPermissions(permission);
        if (missing.size() > 0) {
            requestPermissions(missing.toArray(
                    new String[missing.size()]), ALL_PERMISSIONS_RESULT);
        }

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> permission) {
        ArrayList<String> result = new ArrayList<>();
        for (String permissionsRequest : permission) {
            if (!(checkSelfPermission(permissionsRequest) == PackageManager.PERMISSION_GRANTED)) {
                result.add(permissionsRequest);
            }
        }
        return result;
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
        return captureIntent;
    }
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null;
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    }



