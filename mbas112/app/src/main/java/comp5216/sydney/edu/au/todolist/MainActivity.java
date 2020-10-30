package comp5216.sydney.edu.au.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public int requestCode,resultCode;
    public final int EDIT_ITEM_REQUEST_CODE = 647;
    ArrayList<SubjectData> events;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            final ListView list = findViewById(R.id.list);
            ArrayList<SubjectData> arrayList = new ArrayList<SubjectData>();
            arrayList.add(new SubjectData("JAVA" ));
            arrayList.add(new SubjectData("Python" ));
            arrayList.add(new SubjectData("Javascript" ));
            arrayList.add(new SubjectData("Cprogramming"));
            arrayList.add(new SubjectData("Cplusplus" ));
            arrayList.add(new SubjectData("Android" ));
            CustAdapter customAdapter = new CustAdapter(this , arrayList);
            list.setAdapter(customAdapter);

            if (requestCode == EDIT_ITEM_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Intent data = getIntent();
                    // Extract name value from result extras

                    String editedItem = data.getExtras().getString("item");
                    int position = data.getIntExtra("position" , -1);

                    events.set(position, editedItem );
                    Log.i("Updated Item in list:" , editedItem + ",position:"
                            + position);
                    Toast.makeText(this , "updated:" + editedItem , Toast.LENGTH_SHORT).show();
                    customAdapter.notifyDataSetChanged();
                }
            }
            list.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            User cbrevent = events.get(position);
                            Intent intent = new Intent(view.getContext(), EditView.class);
                            intent.putExtra("itemName", cbrevent.getItemName());
                            intent.putExtra("position", position);
                            startActivity(intent);
                        }
                    });
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                        position, long rowId)
                {
                    Log.i("MainActivity", "Long Clicked item " + position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.dialog_delete_title)
                            .setMessage(R.string.dialog_delete_msg)
                            .setPositiveButton(R.string.delete, new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            events.remove(position); // Remove item from the ArrayList
                                            adapter1.notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton(R.string.cancel, new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // User cancelled the dialog
// Nothing happens
                                        }
                                    });
                    builder.create().show();
                    return true;
                }

            });
        }

}
