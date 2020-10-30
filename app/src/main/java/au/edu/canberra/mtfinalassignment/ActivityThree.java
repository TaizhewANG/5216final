package au.edu.canberra.mtfinalassignment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ActivityThree extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    private static final int REQUEST_PERMISSION = 3000;
    private Activity activity;
    private Uri outputFileUri;
    private final String TAG = "";
    private String currentPhotoPath;
    private ImageView imageView;
    private TextView textView;
    private File photoFile;
    private Bitmap photoBitmap;
    String detectedItem = "";
    String classifiedResult="";
    String Score="";
    String imageFileName;
    private String temp;
    private String item;
    private String all;
    private String a;


  //  private ImageView imageView;
  //  private TextView textView;

 //   private File photoFile;
 //   private Bitmap photoBitmap;
    private Uri contentUri;
    private VisualRecognition visualRecognition;
    private CameraHelper cameraHelper;
    private GalleryHelper galleryHelper;
 //   private Uri contentUri;
 private final String api_key = "E_OniHRo8v93K9svjW1Eznx5-LS2RihdGiUUJAlExkH6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        Intent intent = getIntent();
        setTitle(getIntent().getStringExtra("Title"));
         a = intent.getStringExtra("id");
        imageView = findViewById(R.id.imageView2);
        textView = (TextView) findViewById(R.id.textView4);

        activity = this;


        if(a.equals("'google")){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.google));
        }
        else if(a.equals("ibm")){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ibm));
        }
        cameraHelper = new CameraHelper(this);
        galleryHelper = new GalleryHelper(this);
        IamOptions options = new IamOptions.Builder()
                .apiKey(api_key)
                .build();
        visualRecognition = new VisualRecognition("2020-01-30", options);

    }
    public void captureImage(View view){
        if (a.equals("google")) {
            if (checkPermissions() == false)
                return;
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhotoIntent.resolveActivity(activity.getPackageManager()) != null) {
                outputFileUri = getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else if (a.equals("ibm")) {
            cameraHelper.dispatchTakePictureIntent();
        }
    }

    public void load(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);}
        else if (a.equals("ibm")){
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
            Toast.makeText(getApplicationContext(), "Nothing to upload",
                    Toast.LENGTH_LONG).show();
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
      if(a.equals("google")){
        super.onActivityResult(requestCode, resultCode, data);

        textView.setText("");
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            photoBitmap = getCapturedImage();
            imageView.setImageBitmap(photoBitmap);
        }
        if (requestCode == PICK_IMAGE_REQUEST){
            photoBitmap = getBitmap(resultCode, data);
            outputFileUri=data.getData();
            imageView.setImageBitmap(photoBitmap);
        }

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photoBitmap);

        FirebaseVisionObjectDetectorOptions options =
                new FirebaseVisionObjectDetectorOptions.Builder()
                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .build();

        FirebaseVisionObjectDetector objectDetector = FirebaseVision
                .getInstance()
                .getOnDeviceObjectDetector(options);

        objectDetector.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<FirebaseVisionObject>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionObject> firebaseVisionObjects) {
                                for (FirebaseVisionObject object : firebaseVisionObjects) {
                                    int category = object.getClassificationCategory();
                                    float score=0;
                                    if (object.getClassificationConfidence() != null)
                                        score = object.getClassificationConfidence();
                                    if (category == 1) {
                                        item="Home Good";

                                        textView.append("Firebase ML:Detected Item: Home Good\n Detected Score: " + String.valueOf(score));
                                        item="Home Good";
                                        Score ="Detected Score: " + String.valueOf(score);
                                    }
                                    else if (category == 2) {
                                        item="Fashion Good";

                                        textView.append("Firebase ML:Detected Item: Fashion Good\n Detected Score: " + String.valueOf(score));
                                        item="Fashion Good";
                                        Score ="Detected Score: " + String.valueOf(score);
                                    }
                                    else if (category == 3) {
                                        item="Food";

                                        textView.append("Firebase ML:Detected Item: Food\n Detected Score: " + String.valueOf(score));
                                        item="Food";
                                        Score ="Detected Score: " + String.valueOf(score);
                                    }
                                    else if (category == 4) {
                                        item="Place";

                                        textView.append("Firebase ML:Detected Item: Place\nDetected  Score: " + String.valueOf(score));
                                        item="Place";
                                        Score ="Detected Score: " + String.valueOf(score);
                                    }
                                    else if (category == 5) {
                                        item="Plant";

                                        textView.append("Firebase ML :Detected item Plant\nDetected  Score: " + String.valueOf(score));
                                        item="Plant";
                                        Score ="Detected Score: " + String.valueOf(score);
                                    }
                                    else{

                                        textView.append("Firebase ML:Detected Item: Place\n Score: " + String.valueOf(score));

                                        item="Unknown";
                                        Score ="Detected Score: " + String.valueOf(score);
                                    }
                                    textView.append("\n");
                                }
                                String imageFileName=(item+String.valueOf(System.currentTimeMillis()).toLowerCase().replace(" ","_")+".jpg");
                                uploadFilesToFirebaseStorage(imageFileName,outputFileUri);
                                Intent  intent = new Intent(getApplicationContext(), ActivitySix.class);
                                String data2=textView.getText().toString()+Score;
                                intent.putExtra("itemName",item);
                                intent.putExtra("classifiedResult", data2);
                                intent.putExtra("Title","Google Firebase ML Clould Service");
                                intent.putExtra("detectedScore",Score);
                                intent.putExtra("id",a);
                                intent.putExtra("uri",outputFileUri.toString());
                                intent.putExtra("imageFileName",imageFileName);

                                //intent.putExtra("imageFileNmae", iamgeFileName);
                                startActivity(intent);
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textView.setText("Failed");
                    }
                });}else if(a.equals("ibm")){
          super.onActivityResult(requestCode, resultCode, data);

          if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
              photoBitmap = cameraHelper.getBitmap(resultCode);
              photoFile = cameraHelper.getFile(resultCode);
//                imageView.setImageBitmap(photoBitmap);
          }

          if (requestCode == GalleryHelper.PICK_IMAGE_REQUEST){
              photoBitmap = galleryHelper.getBitmap(resultCode, data);
              photoFile = galleryHelper.getFile(resultCode, data);
//                imageView.setImageBitmap(photoBitmap);
          }

          runBackgroundThread();
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
           //     final double finalScore =score;


              final  String data2 = "IBM Watson: Detected Item: " + item + ". Detected Score: " + String.valueOf(score);
                imageFileName = (item + String.valueOf(new Date().getTime()))
                        .toLowerCase()
                        .replace(' ', '_') + ".jpg";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(),ActivitySix.class);
                        intent.putExtra("itemName",item);
                        intent.putExtra("classifiedResult",data2);
                        intent.putExtra("ibm",a);
                        intent.putExtra("Title","IBM Watson ML Clould Service");
                        intent.putExtra("uri",Uri.fromFile(photoFile).toString());
                        intent.putExtra("imageFileName",imageFileName);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}