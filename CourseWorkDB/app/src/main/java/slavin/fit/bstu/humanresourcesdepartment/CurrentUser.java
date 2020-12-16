package slavin.fit.bstu.humanresourcesdepartment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import slavin.fit.bstu.humanresourcesdepartment.Model.Worker;

public class CurrentUser extends AppCompatActivity {

    String[] user_values = { "Имя: ", "Фамилия: ", "Пол: ", "Отдел: ", "Должность: "};
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    ListView countriesList, listView;
    Button backButton;
    int userId;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentuser);

        init();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        ArrayAdapter<String> adapter_const = new ArrayAdapter(this, android.R.layout.simple_list_item_1, user_values);
        countriesList.setAdapter(adapter_const);

        Bundle intent = getIntent().getExtras();
        userId = intent.getInt("id");
        login = intent.getString("login");

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.open();

        Cursor query = db.rawQuery("SELECT * FROM "+ DatabaseHelper.TABLE_WORKERS +
                " WHERE " + DatabaseHelper.COLUMN_WORKERS_ID +
                " = " + userId +
                " OR " + DatabaseHelper.COLUMN_WORKERS_LOGIN +
                " = '" + login + "' ;", null);

        final ArrayList<String> userNames = new ArrayList<>();
        if(query.moveToFirst()){
                Worker worker = new Worker();
                worker.setName(query.getString(3));
                worker.setSurname(query.getString(4));
                worker.setSex(query.getString(5));
                worker.setDepartment(query.getString(6));
                worker.setPosition(query.getString(7));

                userNames.add(worker.getName());
                userNames.add(worker.getSurname());
                userNames.add(worker.getSex());
                userNames.add(worker.getDepartment());
                userNames.add(worker.getPosition());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userNames);
        listView.setAdapter(adapter);


        query.close();
        db.close();
    }

    private void init() {
        countriesList = (ListView) findViewById(R.id.constant_values);
        listView = (ListView) findViewById(R.id.list_of_data);
        backButton = (Button) findViewById(R.id.backButton);
    }

    public void back(){
//        Intent intent = new Intent(this, List.class);
//        intent.putExtra("id", userId);
//        startActivity(intent);
        onBackPressed();
    }
}
