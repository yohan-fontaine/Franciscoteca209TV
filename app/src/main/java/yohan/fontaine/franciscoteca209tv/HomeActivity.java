package yohan.fontaine.franciscoteca209tv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private ProgressDialog progressDialog;
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private List<UploadPDF> uploadPDFList;
    private BottomNavigationView bottomNavigationView;
    private int currentVueMode = 0;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        stubGrid = findViewById(R.id.stub_grid);
        stubList = findViewById(R.id.stub_list);

        stubGrid.inflate();
        stubList.inflate();

        gridView = findViewById(R.id.gridView);
        listView = findViewById(R.id.listView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_library);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        getBooksList();


        //Get current view mode in share references
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentVueMode = sharedPreferences.getInt("currentVueMode", VIEW_MODE_GRIDVIEW); //default is view listview

        //Register item click
        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);

        //Bottom navigation item click
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelected);
    }

    private void switchView() {

        if (VIEW_MODE_LISTVIEW == currentVueMode) {
            //display listview
            stubList.setVisibility(View.VISIBLE);
            //hide gridview
            stubGrid.setVisibility(View.GONE);
        } else {
            //hide listview
            stubList.setVisibility(View.GONE);
            //display gridview
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentVueMode) {
            listViewAdapter = new ListViewAdapter(this,R.layout.list_item, uploadPDFList);
            listView.setAdapter(listViewAdapter);
        } else {
            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, uploadPDFList);
            gridView.setAdapter(gridViewAdapter);
        }
    }

    private List<UploadPDF> getBooksList() {
        uploadPDFList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    UploadPDF uploadPDF = postSnapshot.getValue(UploadPDF.class);
                    uploadPDFList.add(uploadPDF);
                }
                progressDialog.dismiss();
                switchView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return uploadPDFList;
    }

    private List<UploadPDF> getTextbooksList() {
        uploadPDFList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("textbooks");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    UploadPDF textbook = postSnapshot.getValue(UploadPDF.class);
                    uploadPDFList.add(textbook);
                }

                switchView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return uploadPDFList;
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = uploadPDFList.get(position).getPdfUrl(); // missing 'http://' will cause crashed
            Intent intent = new Intent(getApplicationContext(), pdfview.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    };

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int id = item.getItemId();

            switch (id) {
                case R.id.action_text:
                    getTextbooksList();
                    return true;
                case R.id.action_library:
                    getBooksList();
                    return true;
                case R.id.action_audio:
                    Toast.makeText(getApplicationContext(),"No disponible por el momento", Toast.LENGTH_SHORT).show();
                    return false;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_menu_1) {
            if (VIEW_MODE_LISTVIEW == currentVueMode) {
                currentVueMode = VIEW_MODE_GRIDVIEW;
                item.setIcon(R.drawable.ic_view_module);
            } else {
                currentVueMode = VIEW_MODE_LISTVIEW;
                item.setIcon(R.drawable.ic_view_list);
            }
            //switch view
            switchView();
            //save view in share reference
            SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("currentViewMode", currentVueMode);
            editor.apply();
        }
        return true;
    }
}