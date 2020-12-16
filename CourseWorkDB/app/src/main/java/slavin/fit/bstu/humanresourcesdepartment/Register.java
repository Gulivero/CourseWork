package slavin.fit.bstu.humanresourcesdepartment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import slavin.fit.bstu.humanresourcesdepartment.Model.Worker;
import slavin.fit.bstu.humanresourcesdepartment.Remote.IMyAPI;
import slavin.fit.bstu.humanresourcesdepartment.Remote.RetrofitClient;

public class Register extends AppCompatActivity {

    Button register, back;
    EditText login, password, name, surname, position;
    Spinner spinnerDepartments, spinnerSex;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    ArrayList<String> departments = new ArrayList<>();
    String nameOfDep, nameOfSex;
    String[] sexs = {"М", "Ж"};

    slavin.fit.bstu.humanresourcesdepartment.Remote.IMyAPI IMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        IMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);

        init();

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.open();

        departments.clear();

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


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

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

    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    public void addUser(){



        ArrayList<EditText> reginfoList = new ArrayList<>();
        reginfoList.add(login);
        reginfoList.add(password);
        reginfoList.add(name);
        reginfoList.add(surname);
        reginfoList.add(position);


        for (EditText edit : reginfoList) {
            if (TextUtils.isEmpty(edit.getText())) {
                edit.setError("Внимание! Заполните поле!");
                return;
            }
            if (password.getText().toString().length() < 6) {
                password.setError("Внимание! Длина пароля должна быть не менее 6 символов!");
                return;
            }
            else {
                sqlHelper = new DatabaseHelper(this);
                db = sqlHelper.open();

            }
        }

            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_WORKERS_LOGIN, login.getText().toString());
            cv.put(DatabaseHelper.COLUMN_WORKERS_PASSWORD, password.getText().toString());
            cv.put(DatabaseHelper.COLUMN_WORKERS_NAME, name.getText().toString());
            cv.put(DatabaseHelper.COLUMN_WORKERS_SURNAME, surname.getText().toString());
            cv.put(DatabaseHelper.COLUMN_WORKERS_SEX, nameOfSex);
            cv.put(DatabaseHelper.COLUMN_WORKERS_DEPARTMENT, nameOfDep);
            cv.put(DatabaseHelper.COLUMN_WORKERS_POSITION, position.getText().toString());

            long count = db.insert(DatabaseHelper.TABLE_WORKERS, null, cv);
            if (count > 0) {
                Worker user = new Worker();
                user.setLogin(login.getText().toString());
                user.setPassword(password.getText().toString());
                user.setName(name.getText().toString());
                user.setSurname(surname.getText().toString());
                user.setSex(nameOfSex);
                user.setDepartment(nameOfDep);
                user.setPosition(position.getText().toString());

                compositeDisposable.add(IMyAPI.registerUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(Register.this, s, Toast.LENGTH_SHORT).show();

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(Register.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));
            }


            Toast toast = Toast.makeText(this, "Новый пользователь зарегистрирован", Toast.LENGTH_SHORT);
            toast.show();

    }

    public void back(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void init (){
        register = (Button) findViewById(R.id.register);
        back = (Button) findViewById(R.id.back);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        spinnerSex = (Spinner) findViewById(R.id.spinnerSex);
        spinnerDepartments = (Spinner) findViewById(R.id.spinnerDepartments);
        position = (EditText) findViewById(R.id.position);
    }

}
