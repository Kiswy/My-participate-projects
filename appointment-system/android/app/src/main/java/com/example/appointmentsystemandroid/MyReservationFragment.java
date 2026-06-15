package com.example.appointmentsystemandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyReservationFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyReservationAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view =
                inflater.inflate(
                        R.layout.fragment_my_reservation,
                        container,
                        false
                );

        recyclerView = view.findViewById(R.id.rvMyReservations);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        getContext()
                )
        );

        adapter = new MyReservationAdapter();

        adapter.setOnDeleteClickListener(
                item -> {
                    cancelReservation(
                            item.getId()
                    );
                }
        );

        recyclerView.setAdapter(adapter);

        loadReservationData();

        return view;
    }

    private void loadReservationData() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8081/reservation");

                HttpURLConnection conn =
                        (HttpURLConnection)
                                url.openConnection();

                conn.setRequestMethod("GET");

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        conn.getInputStream()
                                )
                        );

                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();

                JSONArray jsonArray = new JSONArray(sb.toString());

                List<MyReservationItem> list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    MyReservationItem item = new MyReservationItem(
                            obj.getInt("id"),
                            obj.getString("projectName"),
                            obj.getString("reservationCode"),
                            obj.getString("username"),
                            obj.getString("reserveTime"),
                            obj.getString("status")
                            );

                    list.add(item);
                }

                requireActivity()
                        .runOnUiThread(() -> {
                            adapter.setDataList(
                                    list
                            );
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void cancelReservation(int reservationId) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8081/reservation");

                HttpURLConnection conn =
                        (HttpURLConnection)
                                url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                conn.setRequestProperty(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                );

                String data =
                        "action=cancel"
                                + "&reservationId="
                                + reservationId;

                conn.getOutputStream().write(data.getBytes());

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        conn.getInputStream()
                                )
                        );

                String result = reader.readLine();

                reader.close();

                requireActivity().runOnUiThread(() -> {
                    android.widget.Toast.makeText(
                            getContext(),
                            result,
                            android.widget.Toast.LENGTH_SHORT
                            ).show();

                            if(result.contains("成功")){
                                loadReservationData();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}