package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryImg extends AppCompatActivity {
    int selectedPosition = -1;

    ArrayList<ClassifiedItem> events =new ArrayList<ClassifiedItem>();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("DetectedItems");
    ArrayList<String> keys = new ArrayList<String>();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_img);
        final ClassifiedItemAdapter adapter = new ClassifiedItemAdapter(
            this, R.layout.my_listview_item, events);
        setTitle("IBM Image Classification");

        ListView listView = (ListView) findViewById(R.id.Listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String key =keys.get(position);

                    ClassifiedItem cbrevent = events.get(position);
                    Intent intent = new Intent(view.getContext(), EditHistoryImg.class);
                    intent.putExtra("itemName", cbrevent.getItemName());
                    intent.putExtra("detectedResult", cbrevent.getClassifiedResult());
                    intent.putExtra("imageFileName", cbrevent.getImageFileName());
                    intent.putExtra("key",key);
                    startActivity(intent);
                }
            });

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ClassifiedItem cbr = new ClassifiedItem(
                    (String) dataSnapshot.child("itemName").getValue(),
                    (String) dataSnapshot.child("ClassifiedResult").getValue(),
                    (String) dataSnapshot.child("imageFileName").getValue()
                );
                adapter.add(cbr);
                keys.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key =dataSnapshot.getKey();
                int index = keys.indexOf(key);
                if (index != -1){
                    events.remove(index);
                    keys.remove(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    public void AddImage(View v) {
        Intent intent = new Intent(getApplicationContext(), IBMMain.class);
        startActivity(intent);
    }

}