package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;

public class ImgResult extends AppCompatActivity {

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


    private TextView mianName;

    private TextView sub_name;

    private TextView content_score;

    private ImageView edit_image1;
    private String kas = "ok";
    String a;
    String b;
    String c;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_result);

        Intent intent = getIntent();
        mianName = findViewById(R.id.textView1);

        edit_image1 = findViewById(R.id.imageView);

        setTitle(getIntent().getStringExtra("Title"));
        mianName.setText(intent.getStringExtra("classifiedResult"));
        uri = intent.getStringExtra("uri");
        outputUri = Uri.parse(uri);
        Bitmap bitmap = getCapturedImage();
        edit_image1.setImageBitmap(bitmap);

        a = intent.getStringExtra("itemName");
        b = intent.getStringExtra("classifiedResult");
        c = intent.getStringExtra("detectedScore");
        imageFileName = intent.getStringExtra("imageFileName");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
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
        Intent intent = new Intent(getApplicationContext(), EditResult.class);
        intent.putExtra("itemName",a);
        intent.putExtra("Result", b);
        intent.putExtra("uri",uri);
        intent.putExtra("source",kas);
        intent.putExtra("imageFileName",imageFileName);
        startActivity(intent);
    }

    public void finish(View view) {
        finish();
    }
}
