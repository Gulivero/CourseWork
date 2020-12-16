package slavin.fit.bstu.humanresourcesdepartment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import slavin.fit.bstu.humanresourcesdepartment.Model.Worker;

public class List extends AppCompatActivity {


    Button showButton, backButton;
    ListView list_users;
    DatabaseHelper SQLiteHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        init();

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

    }

    ArrayList<String> workers = new ArrayList<>();
    ArrayList<Worker> WorkersList = new ArrayList<>();

    public void display() {
        SQLiteHelper = new DatabaseHelper(this);
        db = SQLiteHelper.open();

        workers.clear();
        Cursor query = db.rawQuery("SELECT * FROM Workers", null);

        if(query.moveToFirst()) {
            do {
                Worker worker = new Worker();
                worker.setId(query.getInt(0));
                worker.setName(query.getString(3));
                worker.setSurname(query.getString(4));
                workers.add(worker.getId() + " " + worker.getName() + " " + worker.getSurname());
                WorkersList.add(worker);
            }
            while (query.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, workers);
        list_users.setAdapter(adapter);

        query.close();
        db.close();

        list_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Worker worker = WorkersList.get(position);
                userId = worker.getId();
                current_user(view);
            }
        });
    }
    int userId;

    public void current_user(View view) {
        Intent intent = new Intent(this, CurrentUser.class);
        intent.putExtra("id", userId);
        startActivity(intent);
    }

    public void back(){
        Intent intent = new Intent(this, Department.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("id", userId);
        startActivity(intent);
    }

    private void init() {
        showButton = (Button) findViewById(R.id.showButton);
        backButton = (Button) findViewById(R.id.backButton);
        list_users = (ListView) findViewById(R.id.list_users);
    }

}
