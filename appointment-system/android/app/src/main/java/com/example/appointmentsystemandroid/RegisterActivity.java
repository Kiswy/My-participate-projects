package com.example.appointmentsystemandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText phone = findViewById(R.id.phone);
        TextView register = findViewById(R.id.register);
        TextView login = findViewById(R.id.login);

        // 注册
        register.setOnClickListener(v -> {
            String usernameText =
                    username.getText()
                            .toString()
                            .trim();

            String passwordText =
                    password.getText()
                            .toString()
                            .trim();

            String phoneText =
                    phone.getText()
                            .toString()
                            .trim();

            if (usernameText.isEmpty()
                    || passwordText.isEmpty()
                    || phoneText.isEmpty()) {
                Toast.makeText(
                        RegisterActivity.this,
                        "请填写完整信息",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            new Thread(() -> {
                try {
                    URL url = new URL("http://10.0.2.2:8081/api/register");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty(
                            "Content-Type",
                            "application/x-www-form-urlencoded"
                    );

                    String data =
                            "username="
                                    + usernameText
                                    + "&password="
                                    + passwordText
                                    + "&phone="
                                    + phoneText;

                    conn.getOutputStream()
                            .write(
                                    data.getBytes()
                            );

                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            conn.getInputStream()
                                    )
                            );

                    String result = reader.readLine();

                    reader.close();

                    JSONObject json = new JSONObject(result);

                    boolean success = json.getBoolean("success");

                    String message = json.getString("message");

                    runOnUiThread(() -> {
                        Toast.makeText(
                                RegisterActivity.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();

                        if(success){
                            finish();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(
                                RegisterActivity.this,
                                "注册失败",
                                Toast.LENGTH_SHORT
                        ).show();
                    });
                }
            }).start();
        });

        // 返回登录
        login.setOnClickListener(v -> {
            finish();
        });
    }
}