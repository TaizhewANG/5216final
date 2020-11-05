package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class RegisterActivity extends AppCompatActivity {

    private EditText et_user_name,et_psw,et_psw_again,et_email;
    private String userName,psw,pswAgain,email;
    private RadioGroup Sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        setTitle("IBM Watson ML Cloud Service");
        //activity_register.xml
        Button btn_register = (Button) findViewById(R.id.btn_register);
        et_user_name= (EditText) findViewById(R.id.et_user_name);
        et_psw= (EditText) findViewById(R.id.et_psw);
        et_psw_again= (EditText) findViewById(R.id.et_psw_again);
        Sex= (RadioGroup) findViewById(R.id.SexRadio);
        et_email= (EditText) findViewById(R.id.et_email);
        //register
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getEditString();
                int sex;
                int sexChoseId = Sex.getCheckedRadioButtonId();
                switch (sexChoseId) {
                    case R.id.mainRegisterRdBtnFemale:
                        sex = 0;
                        break;
                    case R.id.mainRegisterRdBtnMale:
                        sex = 1;
                        break;
                    default:
                        sex = -1;
                        break;
                }

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your account", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password again", Toast.LENGTH_SHORT).show();
                } else if (sex<0){
                    Toast.makeText(RegisterActivity.this, "Please select your sex", Toast.LENGTH_SHORT).show();
                }else if(!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();


                }else if(isExistUserName(userName)){
                    Toast.makeText(RegisterActivity.this, "This account already exists", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();

                    saveRegisterInfo(userName, psw);
                    if(sex==0){
                        saveGender(userName, "Female");
                    }
                    else if(sex==-1){
                        saveGender(userName, "Male");
                    }
                    saveEmail(userName, email);
                    Intent data = new Intent();
                    data.putExtra("userName", userName);
                    setResult(RESULT_OK, data);

                    RegisterActivity.this.finish();
                }
            }
        });
    }

    private void getEditString(){
        userName=et_user_name.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        pswAgain=et_psw_again.getText().toString().trim();
        email=et_email.getText().toString().trim();
    }
    /**
     * Read form SharedPreferences
     */
    private boolean isExistUserName(String userName){
        boolean has_userName=false;
        //mode_private SharedPreferences sp = getSharedPreferences( );
        // "loginInfo", MODE_PRIVATE
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //get password
        String spPsw=sp.getString(userName, "");
        if(!TextUtils.isEmpty(spPsw)) {
            has_userName=true;
        }
        return has_userName;
    }
    /**
     * save to SharedPreferences中SharedPreferences
     */
    private void saveRegisterInfo(String userName,String psw){
        String md5Psw = MD5Utils.toMD5(psw);//MD5
        //loginInfo, mode_private SharedPreferences sp = getSharedPreferences( );
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);

        //SharedPreferences.Editor  editor -> sp.edit();
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(userName, md5Psw);
        //editor.commit();
        editor.apply();
    }

    private void saveGender(String userName, String gender){
        SharedPreferences sp=getSharedPreferences("gender", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(userName, gender);
        editor.apply();
    }

    private void saveEmail(String userName, String email){
        SharedPreferences sp=getSharedPreferences("email", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(userName, email);
        editor.apply();
    }

}