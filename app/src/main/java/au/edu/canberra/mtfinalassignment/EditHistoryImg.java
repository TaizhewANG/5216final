
package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class EditHistoryImg extends AppCompatActivity {
    private EditText name_edit;

    private EditText score_edit;

    private int id;

    private ImageView ImageView_edit;

    private String name;

    private double score;

    private String path;

    private String from;

    private String uri;
    private String uri1;

    private Uri outputUri;

    private String kas = "ok1";
    private TextView mian_name;

    private TextView sub_name;

    private TextView content_score;

    private ImageView edit_image1;
    String a;
    String b;
    String c;
    String key;
    String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_history_img);
        setTitle("IBM Image Classification");
        Intent intent = getIntent();
        mian_name = findViewById(R.id.textViewLarge);
        sub_name = findViewById(R.id.textView3);
        key = intent.getStringExtra("key");
        a = intent.getStringExtra("itemName");
        b = intent.getStringExtra("detectedResult");
        imageFileName = intent.getStringExtra("imageFileName");
        sub_name.setText(b);
        mian_name.setText(a);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef = storageRef.child("images");
        try {
            final File localFile = File.createTempFile("temp" , ".jpg");
            downloadFile(imagesRef.child(imageFileName) , localFile);
        } catch (IOException ex) {
            notifyUser(ex.getMessage());
        }
    }

    public void downloadFile(StorageReference fileRef , final File file) {
        if (file != null) {
            fileRef.getFile(file)
                .addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            outputUri = Uri.fromFile(file);
                            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                            imageView.setImageURI(outputUri);
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
        Toast.makeText(this , message , Toast.LENGTH_SHORT).show();
    }

    public void deletebutton(View v) {
        deleteDateItemFromFirebaseDatabase(key);
    }

    public void editbutton(View v) {
        Intent intent = new Intent(getApplicationContext() , EditResult.class);
        intent.putExtra("itemName" , a);
        intent.putExtra("Result" , b);
        intent.putExtra("key" , key);
        intent.putExtra("source",kas);
        intent.putExtra("imageFileName" , imageFileName);
        startActivity(intent);
    }

    public void deleteDateItemFromFirebaseDatabase(String key) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference deRef = db.getReference("DetectedItems");
        deRef.child(key).removeValue();
        Intent intent = new Intent(this , HistoryImg.class);
        startActivity(intent);

    }
}