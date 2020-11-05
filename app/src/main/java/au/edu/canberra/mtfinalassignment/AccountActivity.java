package au.edu.canberra.mtfinalassignment;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    private EditText user_name,et_gender,et_email;
    private String userName,gender,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        init();
    }

    private void init() {

        SharedPreferences sp=getSharedPreferences("gender", MODE_PRIVATE);
        gender=sp.getString(userName, "");

        SharedPreferences spp=getSharedPreferences("email", MODE_PRIVATE);
        email=spp.getString(userName, "");
        System.out.println(email);

        user_name = (EditText) findViewById(R.id.user_name);
        et_gender = (EditText) findViewById(R.id.et_gender);
        et_email = (EditText) findViewById(R.id.et_email);
        Button btn_return = (Button) findViewById(R.id.btn_register);

        user_name.setText(userName);
        et_gender.setText(gender);
        et_email.setText(email);
        btn_return.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AccountActivity.this.finish();
                Intent intent=new Intent(AccountActivity.this,IBMMain.class);
                startActivity(intent);
            }
        });
    }





}
