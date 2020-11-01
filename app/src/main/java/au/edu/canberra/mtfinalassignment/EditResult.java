package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class EditResult extends AppCompatActivity {
    private TextView food_result1_txt;
    private TextView food_result1_title;

    private ImageView food_result1_img;

    private String name;

    private String score;

    private String path;

    private String uri;
    private String ubk;
    private String imageFileName;
    private String key;
    private Uri outputFileUri;
    String result;
    private TextView food_result1_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_result);
        setTitle("Editing Classification Results");
        Intent intent = getIntent();

        name = intent.getStringExtra("itemName");
        result = intent.getStringExtra("Result");
        score =intent.getStringExtra("score");
        ubk =intent.getStringExtra("source");

        imageFileName = intent.getStringExtra("imageFileName");

        key = intent.getStringExtra("key");
        food_result1_img = findViewById(R.id.edit_img);
        food_result1_title =findViewById(R.id.name_edit);
        food_result1_score =findViewById(R.id.name_edit2);
        ubk = intent.getStringExtra("source");

        if (ubk.equals("ok")) {
            uri = intent.getStringExtra("uri");
            outputFileUri = Uri.parse((String) uri);
            food_result1_img .setImageURI(Uri.parse(uri));
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference imagesRef = storageRef.child("images");

            try {
                final File localFile = File.createTempFile("temp", ".jpg");

                downloadFile(imagesRef.child(imageFileName), localFile);

            } catch (IOException ex) {
                notifyUser(ex.getMessage());
            }
        }

        food_result1_score.setText(result);
        food_result1_title.setText(name);

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

    public void downloadFile(StorageReference fileRef, final File file) {
        if (file != null) {
            fileRef.getFile(file)
                    .addOnSuccessListener(
                        new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri uri = Uri.fromFile(file);
                                ImageView imageView = (ImageView) findViewById(R.id. edit_img);
                                imageView.setImageURI(uri);
                                notifyUser("Download complete");
                            }
                        })
                    .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                notifyUser("Unable to download");
                            }
                        });
        }
    }

    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public String addDetectedResultToFirebaseDatabase(ClassifiedItem item) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("DetectedItems");
        String key = dbRef.push().getKey();
        dbRef.child(key).child("itemName").setValue(item.getItemName());
        dbRef.child(key).child("ClassifiedResult").setValue(item.getClassifiedResult());
        dbRef.child(key).child("imageFileName").setValue(item.getImageFileName());
        return key;
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

    public void save(View v) {
        if(ubk.equals("ok")){
            Intent intent = new Intent(getApplicationContext(), HistoryImg.class);
            name = food_result1_title.getText().toString();
            score =food_result1_score.getText().toString();
            ClassifiedItem item = new ClassifiedItem(name,score,imageFileName);
            addDetectedResultToFirebaseDatabase(item);
            uploadFilesToFirebaseStorage(imageFileName,outputFileUri);
            startActivity(intent);
        }
        else if(ubk.equals("ok1")) {
            Intent intent = new Intent(getApplicationContext(), HistoryImg.class);
            name = food_result1_title.getText().toString();
            score =food_result1_score.getText().toString();

            ClassifiedItem item = new ClassifiedItem(name,score,imageFileName);
            updateDetectedResultToFirebaseDatabase(item,key);
            startActivity(intent);
        }
    }

    public void cancel(View v) {
        Intent intent = new Intent(getApplicationContext(), HistoryImg.class);
        startActivity(intent);
    }

    public void updateDetectedResultToFirebaseDatabase(ClassifiedItem item,String key){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("DetectedItems");
        dbRef.child(key).child("itemName").setValue(item.getItemName());
        dbRef.child(key).child("imageFileName").setValue(item.getImageFileName());
        dbRef.child(key).child("ClassifiedResult").setValue(item.getClassifiedResult());
    }
}
