package com.example.appointmentsystemandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookingHallFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookingHallAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view =
                inflater.inflate(
                        R.layout.fragment_booking_hall,
                        container,
                        false
                );

        recyclerView = view.findViewById(R.id.rvBookingHall);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingHallAdapter();
        adapter.setOnBookClickListener(
                item -> {
                    reserveProject(
                            item.getId()
                    );
                }
        );

        recyclerView.setAdapter(adapter);

        loadProjectData();

        return view;
    }

    private void loadProjectData() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8081/api/projects");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setInstanceFollowRedirects(false);

                int responseCode = conn.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("加载项目失败，HTTP状态码：" + responseCode);
                }

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

                List<BookingItem> list = new ArrayList<>();

                for (int i = 0;
                     i < jsonArray.length();
                     i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    BookingItem item =
                            new BookingItem(
                                    obj.getInt("id"),
                                    obj.getString("projectName"),
                                    obj.getString("location"),
                                    obj.getString("appointmentTime"),
                                    obj.getInt("remainingCount"),
                                    obj.getInt("capacity")
                            );

                    list.add(item);
                }

                requireActivity().runOnUiThread(() -> {
                            adapter.setDataList(
                                    list
                            );
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void reserveProject(int projectId) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8081/reservation");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                );

                String data = "projectId=" + projectId;

                conn.getOutputStream().write(data.getBytes());

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        conn.getInputStream()
                                )
                        );

                String result = reader.readLine();

                reader.close();

                requireActivity()
                        .runOnUiThread(() -> {
                            Toast.makeText(
                                    getContext(),
                                    result,
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (result.contains("成功")) {
                                loadProjectData();
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity()
                        .runOnUiThread(() -> {
                            Toast.makeText(
                                    getContext(),
                                    "预约失败",
                                    Toast.LENGTH_SHORT
                            ).show();
                        });
            }
        }).start();
    }
}