package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("IBM Image Classification");

    }

    public void openView(View v) {
        Intent intent = new Intent(getApplicationContext() , SignIn.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 這個是第一頁右上方的選項,看看要怎麼改
        int id = item.getItemId();
        switch (id) {
            case R.id.map_normal:

                break;
            case R.id.map_satellite:

                break;
            case R.id.ActivityEight:
                Intent intent = new Intent(getApplicationContext() , HistoryImg.class);
                startActivity(intent);
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
