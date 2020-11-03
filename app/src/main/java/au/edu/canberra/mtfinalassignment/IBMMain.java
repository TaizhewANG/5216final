package au.edu.canberra.mtfinalassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.android.library.camera.GalleryHelper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;


public class IBMMain extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 3000;
    private Activity activity;
    private Uri outputFileUri;
    private final String TAG = "";
    private ImageView imageView;
    private TextView textView;
    private File photoFile;
    private Bitmap photoBitmap;

    String imageFileName;
    private String temp;
    private String item;
    private String all;

    private VisualRecognition visualRecognition;
    private CameraHelper cameraHelper;
    private GalleryHelper galleryHelper;

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque = new ArrayDeque<>(3);

    private final String api_key = "E_OniHRo8v93K9svjW1Eznx5-LS2RihdGiUUJAlExkH6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibm_main);
        Intent intent = getIntent();
        setTitle("IBM Watson ML Cloud Service");
        imageView = findViewById(R.id.imageView2);
        textView = (TextView) findViewById(R.id.textView4);

        activity = this;

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ibm));
        cameraHelper = new CameraHelper(this);
        galleryHelper = new GalleryHelper(this);
        IamOptions options = new IamOptions.Builder()
                .apiKey(api_key)
                .build();
        visualRecognition = new VisualRecognition("2020-01-30", options);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bn_dashboard:
                        startActivity(new Intent(getApplicationContext(),
                                SignIn.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bn_account:
                        startActivity(new Intent(getApplicationContext(),
                                IBMMain.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bn_home:
                        startActivity(new Intent(getApplicationContext(),
                                IBMMain.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void captureImage(View view){

        if (checkPermissions() == false) {
            Toast.makeText(getApplicationContext(),"The application needs permission to do follow actions!",Toast.LENGTH_SHORT).show();
        }
        else {
            cameraHelper.dispatchTakePictureIntent();
        }
    }

    public void load(View view) {

        if (checkPermissions() == false) {
            Toast.makeText(getApplicationContext(),"The application needs permission to do follow actions!",Toast.LENGTH_SHORT).show();
        }
        else {
            galleryHelper.dispatchGalleryIntent();
        }
    }

    public void uploadFilesToFirebaseStorage( String filename,Uri photoUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef = storageRef.child("images");

        if (photoUri !=null) {
            UploadTask uploadTask = imagesRef.child(filename).putFile(photoUri);
            uploadTask.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Upload failed: " + e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Upload complete",
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                .addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Nothing to upload", Toast.LENGTH_LONG).show();
        }
    }


    private boolean checkPermissions() {
        String permissions[] = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean grantCamera =
                ContextCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_GRANTED;
        boolean grantExternal =
                ContextCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_GRANTED;

        if (!grantCamera && !grantExternal) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION);
        } else if (!grantCamera) {
            ActivityCompat.requestPermissions(activity, new String[]{permissions[0]}, REQUEST_PERMISSION);
        } else if (!grantExternal) {
            ActivityCompat.requestPermissions(activity, new String[]{permissions[1]}, REQUEST_PERMISSION);
        }

        return grantCamera && grantExternal;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
          super.onActivityResult(requestCode, resultCode, data);

          if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
              if (resultCode == 0) {
                  finish();
              }
              else {
                  photoBitmap = cameraHelper.getBitmap(resultCode);
                  photoFile = cameraHelper.getFile(resultCode);
                  runBackgroundThread();
              }
          }

          else if (requestCode == GalleryHelper.PICK_IMAGE_REQUEST){
              if (resultCode == 0) {
                  finish();
              }
              else {
                  photoBitmap = galleryHelper.getBitmap(resultCode, data);
                  photoFile = galleryHelper.getFile(resultCode, data);
                  runBackgroundThread();
              }

          }
    }

    private Bitmap getCapturedImage() {
        Bitmap srcImage = null;
        try {
            srcImage = FirebaseVisionImage
                    .fromFilePath(getBaseContext(), outputFileUri)
                    .getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return srcImage;
    }

    public Bitmap getBitmap(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            try {
                return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(targetUri));
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File Not Found", e);
                return null;
            }
        }
        Log.e(TAG, "Result Code was not OK");
        return null;
    }

    private void runBackgroundThread(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                InputStream imagesStream = null;
                try {
                    imagesStream = new FileInputStream(photoFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                        .imagesFile(imagesStream)
                        .imagesFilename(photoFile.getName())
                        .threshold((float) 0.6)
                        .classifierIds(Arrays.asList("default"))
                        .build();
                ClassifiedImages result = visualRecognition.classify(classifyOptions).execute();

                Gson gson = new Gson();
                String json = gson.toJson(result);
                Log.d("json", json);
                String name = null;
                double score = 0;
                String gender = null;
                int age = 0;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("classifiers");
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("classes");
                    JSONObject jsonObject3 = jsonArray2.getJSONObject(0);
                    name = jsonObject3.getString("class");
                    score = jsonObject3.getDouble("score");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String item = name;

                final  String data2 = "The item is: " + item + ".\nWith: " + String.valueOf(score*100) + "% likelihood!";
                imageFileName = (item + String.valueOf(new Date().getTime()))
                        .toLowerCase()
                        .replace(' ', '_') + ".jpg";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), ImgResult.class);
                        intent.putExtra("itemName",item);
                        intent.putExtra("classifiedResult",data2);
                        intent.putExtra("Title","IBM Watson ML Cloud Service");
                        intent.putExtra("uri",Uri.fromFile(photoFile).toString());
                        intent.putExtra("imageFileName",imageFileName);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}