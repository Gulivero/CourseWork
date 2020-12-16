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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import slavin.fit.bstu.humanresourcesdepartment.Model.Worker;

public class Search extends AppCompatActivity {


    Button showButton, backButton;
    ListView list_users;
    DatabaseHelper SQLiteHelper;
    SQLiteDatabase db;
    Button back;
    EditText name, surname, position;
    Spinner spinnerDepartments, spinnerSex;
    ArrayList<String> departments = new ArrayList<>();
    String nameOfDep, nameOfSex;
    String[] sexs = {"", "М", "Ж"};
    int userId;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        SQLiteHelper = new DatabaseHelper(this);
        db = SQLiteHelper.open();

        departments.clear();
        departments.add("");
        Cursor query = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_DEPARTMENTS + " ;", null);
        if(query.moveToFirst()){
            do{
                String dep = query.getString(1);
                departments.add(dep);
            }
            while(query.moveToNext());
        }
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartments.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexs);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapter2);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                nameOfDep = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        AdapterView.OnItemSelectedListener item2SelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                nameOfSex = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerDepartments.setOnItemSelectedListener(itemSelectedListener);
        spinnerSex.setOnItemSelectedListener(item2SelectedListener);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display();
            }
        });

    }

    ArrayList<String> workers = new ArrayList<>();
    ArrayList<Worker> WorkersList = new ArrayList<>();

    public void display() {
        SQLiteHelper = new DatabaseHelper(this);
        db = SQLiteHelper.open();

        workers.clear();
        Cursor query = db.rawQuery("SELECT * FROM Workers WHERE name like '%" + name.getText().toString() +
                "%' and surname like '%" + surname.getText().toString() +
                "%' and sex like '%" + nameOfSex +
                "%' and department like '%" + nameOfDep +
                "%' and position like '%" + position.getText().toString() +
                "%';", null);

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
                login = worker.getLogin();
                current_user(view);
            }
        });
    }

    public void current_user(View view) {
        Intent intent = new Intent(this, CurrentUser.class);
        intent.putExtra("id", userId);
        intent.putExtra("login", login);
        startActivity(intent);
    }

    public void back(){
        Intent intent = new Intent(this, Department.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("id", userId);
        intent.putExtra("login", login);
        startActivity(intent);
    }

    private void init() {
        showButton = (Button) findViewById(R.id.showButton);
        backButton = (Button) findViewById(R.id.backButton);
        list_users = (ListView) findViewById(R.id.list_users);
        back = (Button) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        spinnerSex = (Spinner) findViewById(R.id.spinnerSex);
        spinnerDepartments = (Spinner) findViewById(R.id.spinnerDepartments);
        position = (EditText) findViewById(R.id.position);
    }

}
