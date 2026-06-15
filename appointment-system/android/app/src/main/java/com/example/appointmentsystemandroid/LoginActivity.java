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
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(
                    new CookieManager(
                            null,
                            CookiePolicy.ACCEPT_ALL
                    )
            );
        }

        // 登录
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        TextView login = findViewById(R.id.login);

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String usernameText =
                                username.getText()
                                        .toString()
                                        .trim();

                        String passwordText =
                                password.getText()
                                        .toString()
                                        .trim();

                        if (usernameText.isEmpty()
                                || passwordText.isEmpty()) {
                            Toast.makeText(
                                    LoginActivity.this,
                                    "请输入用户名和密码",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        new Thread(() -> {
                            try {
                                URL url = new URL("http://10.0.2.2:8081/api/login");

                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);
                                conn.setRequestProperty(
                                        "Content-Type",
                                        "application/x-www-form-urlencoded"
                                );

                                String data = "username=" + usernameText + "&password=" + passwordText;

                                conn.getOutputStream().write(data.getBytes());

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
                                            LoginActivity.this,
                                            message,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    if (success) {
                                        Intent intent =
                                                new Intent(
                                                        LoginActivity.this,
                                                        homepageActivity.class
                                                );

                                        startActivity(intent);

                                        finish();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(() -> {
                                    Toast.makeText(
                                            LoginActivity.this,
                                            "网络请求失败",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                });
                            }
                        }).start();
                    }
                }
        );

        // 注册
        TextView register = findViewById(R.id.register);

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =
                                new Intent(
                                        LoginActivity.this,
                                        RegisterActivity.class
                                );

                        startActivity(intent);
                    }
                }
        );
    }
}
