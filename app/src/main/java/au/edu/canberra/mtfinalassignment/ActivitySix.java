package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;

public class ActivitySix extends AppCompatActivity {

    private EditText name_edit;

    private EditText score_edit;

    private int id;

    private ImageView ImageView_edit;

    private String name;

    private double score;

    private String path;

    private String from;

    private String uri;

    private Uri outputUri;

    private String imageFileName;


    private TextView mian_name;

    private TextView sub_name;

    private TextView content_score;

    private ImageView edit_image1;
    private String kas = "ok";
    String a;
    String b;
    String c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six);


        Intent intent = getIntent();
        mian_name = findViewById(R.id.textView1);

        edit_image1 = findViewById(R.id.imageView);

        setTitle(getIntent().getStringExtra("Title"));
        mian_name.setText(intent.getStringExtra("classifiedResult"));
       // sub_name.setText(intent.getStringExtra("classifiedResult"));
        uri = intent.getStringExtra("uri");
        outputUri = Uri.parse(uri);
        Bitmap bitmap = getCapturedImage();
        edit_image1.setImageBitmap(bitmap);

        a = intent.getStringExtra("itemName");
        b = intent.getStringExtra("classifiedResult");
        c = intent.getStringExtra("detectedScore");
        imageFileName = intent.getStringExtra("imageFileName");
    }

    private Bitmap getCapturedImage() {
        Bitmap srcImage = null;
        try {
            srcImage = FirebaseVisionImage
                    .fromFilePath(getBaseContext(), outputUri)
                    .getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return srcImage;
    }

    public void edit(View v) {
        Intent intent = new Intent(getApplicationContext(), ActivitySeven.class);

        intent.putExtra("itemName",a);
        intent.putExtra("Result", b);
        intent.putExtra("uri",uri);
       intent.putExtra("source",kas);
     //   intent.putExtra("score",c);
        intent.putExtra("imageFileName",imageFileName);
        startActivity(intent);
    }

}
