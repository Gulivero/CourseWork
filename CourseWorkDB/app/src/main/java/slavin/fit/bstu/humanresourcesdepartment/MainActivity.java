package slavin.fit.bstu.humanresourcesdepartment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import slavin.fit.bstu.humanresourcesdepartment.Model.Worker;
import slavin.fit.bstu.humanresourcesdepartment.Remote.IMyAPI;
import slavin.fit.bstu.humanresourcesdepartment.Remote.RetrofitClient;

public class MainActivity extends AppCompatActivity {

    Button loginButton, registerButton;
    EditText loginField, passwordField;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;

    IMyAPI IMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);

        sqlHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        sqlHelper.create_db();

        init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Department(v);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register(v);
            }
        });

    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    public void Register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void Department(View view) {
        Worker user = new Worker();
        user.setLogin(loginField.getText().toString());
        user.setPassword(passwordField.getText().toString());
        compositeDisposable.add(IMyAPI.loginUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        int id;
                        String login, passwordd;
                        db = sqlHelper.open();

                        Cursor query = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_WORKERS +
                                " WHERE " + DatabaseHelper.COLUMN_WORKERS_LOGIN + " = '" + loginField.getText().toString() +
                                "' AND " + DatabaseHelper.COLUMN_WORKERS_PASSWORD + " = '" + passwordField.getText().toString() + "' ;", null);

                        if (query.moveToFirst()) {
                            id = query.getInt(0);
                            login = query.getString(1);
                            passwordd = query.getString(2);
                            Intent intent = new Intent(MainActivity.this, Department.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("id", id);
                            intent.putExtra("login", login);
                            intent.putExtra("password", passwordd);
                            startActivity(intent);
                        }
                        else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        loginField.setError("Введено неверное имя пользователя или пароль");
                    }
                }));
    }

    private void init() {
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginField = (EditText) findViewById(R.id.loginField);
        passwordField = (EditText) findViewById(R.id.passwordField);
    }
}
