package slavin.fit.bstu.humanresourcesdepartment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Department extends AppCompatActivity {

    Button profileButton, searchButton, listButton, controlButton;
    int userId;
    String login, passwordd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        Bundle id = getIntent().getExtras();
        userId = id.getInt("id");
        login = id.getString("login");
        passwordd = id.getString("password");
        init();

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile(v);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list(v);
            }
        });

        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control(v);
            }
        });

    }

    public void profile(View view) {
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("id", userId);
        intent.putExtra("login", login);
        intent.putExtra("password", passwordd);
        startActivity(intent);
    }

    public void search(View view) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void list(View view) {
        Intent intent = new Intent(this, List.class);
        startActivity(intent);
    }

    public void control(View view) {
        if (userId == 1) {
            controlButton.setEnabled(true);
            Intent intent = new Intent(this, Control.class);
            startActivity(intent);
        }
        else controlButton.setEnabled(false);
    }

    private void init() {
        profileButton = (Button) findViewById(R.id.profileButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        listButton = (Button) findViewById(R.id.listButton);
        controlButton = (Button) findViewById(R.id.controlButton);
    }

}
