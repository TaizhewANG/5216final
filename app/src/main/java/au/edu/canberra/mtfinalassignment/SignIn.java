package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SignIn extends AppCompatActivity {
    private String userName,psw,spPsw;
    private EditText et_user_name,et_psw;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        Intent intent = getIntent();

        init();
    }

    private void init() {
        //get id from main_title_bar
        //from activity_login.xml
        TextView tv_register = (TextView) findViewById(R.id.tv_register);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        et_user_name= (EditText) findViewById(R.id.et_user_name);
        et_psw= (EditText) findViewById(R.id.et_psw);
        //register
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this,RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get account and password getText().toString().trim();
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                //MD5
                String md5Psw = MD5Utils.toMD5(psw);

                spPsw = readPsw(userName);
                // TextUtils.isEmpty
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(SignIn.this, "Please enter your account", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(SignIn.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    // md5Psw.equals();
                } else if (md5Psw.equals(spPsw)) {
                    //success
                    Toast.makeText(SignIn.this, "Success", Toast.LENGTH_SHORT).show();
                    saveLoginStatus(true, userName);
                    Intent intent = new Intent(SignIn.this, IBMMain.class);
                    //datad.putExtra( ); name , value ;
                    intent.putExtra("isLogin", true);
                    intent.putExtra("userName", userName);
                    //RESULT_OK=-1
                    setResult(RESULT_OK, intent);
                    SignIn.this.finish();
                    startActivity(intent);
                } else if ((spPsw != null && !TextUtils.isEmpty(spPsw) && !md5Psw.equals(spPsw))) {
                    Toast.makeText(SignIn.this, "Wrong account or password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignIn.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     *get account and password from SharedPreferences
     */
    private String readPsw(String userName){
        //getSharedPreferences("loginInfo",MODE_PRIVATE);
        //"loginInfo",mode_private; MODE_PRIVATE
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //sp.getString() userName, "";
        return sp.getString(userName , "");
    }
    /**
     *Save to SharedPreferences
     */
    private void saveLoginStatus(boolean status,String userName){
        //loginInfo  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserName", userName);
        editor.apply();
    }
    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    //onActivityResult
    //startActivityForResult(intent, 1);
    //int requestCode , int resultCode , Intent data
    // LoginActivity -> startActivityForResult -> onActivityResult();
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            // getExtra().getString("***");
            String userName=data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                et_user_name.setText(userName);
                et_user_name.setSelection(userName.length());
            }
        }
    }
}
