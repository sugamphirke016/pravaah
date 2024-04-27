package com.example.pravaah.services;

import static androidx.core.content.ContextCompat.registerReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pravaah.services.retrofit.ApiService;
import com.example.pravaah.services.retrofit.RetrofitClient;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.pravaah.R;
import com.example.pravaah.services.adapter.TaskCardListAdapter;
import com.example.pravaah.services.dataModel.TaskCardModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksFragment extends Fragment {
    private TextView dateTime;
    private TextView tasksTextView;
    private final Handler handler = new Handler();
    ArrayList<TaskCardModel> tasks;
    private RecyclerView recyclerView;
    private TaskCardListAdapter adapter;
    public SwipeRefreshLayout swipeRefreshLayout;
    private Context mContext;
    private BroadcastReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateTime = view.findViewById(R.id.datentime);
        updateDateTime();
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        tasksTextView = view.findViewById(R.id.tasksTextView);
        fetchCountOfTasks();
        fetchTodaysTasks();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("REFRESH_LAYOUT")) {
                    refreshLayout();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("REFRESH_LAYOUT");
        mContext.registerReceiver(mReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
                Toast.makeText(requireContext(), "Task sheet refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout();
                handler.postDelayed(this, TimeUnit.MINUTES.toMillis(1));
            }
        }, TimeUnit.MINUTES.toMillis(1));
    }

    private void refreshLayout() {
        swipeRefreshLayout.setRefreshing(true);
        fetchCountOfTasks();
        fetchTodaysTasks();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    private void fetchCountOfTasks() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<JsonObject> call = apiService.getCountOfTasks();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();
                    int remainingTasks = jsonResponse.get("remaining_tasks").getAsInt();
                    int totalTasks = jsonResponse.get("total_tasks").getAsInt();
                    String message = jsonResponse.get("message").getAsString();

                    tasksTextView.setText(message);

                } else {
                    Toast.makeText(requireContext(), "Failed to fetch tasks count", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", "Error fetching tasks count: " + t.getMessage());
            }
        });
    }

    private void fetchTodaysTasks() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<TaskCardModel>> call = apiService.getTasks();
        call.enqueue(new Callback<List<TaskCardModel>>() {
            @Override
            public void onResponse(Call<List<TaskCardModel>> call, Response<List<TaskCardModel>> response) {
                if (response.isSuccessful()) {
                    List<TaskCardModel> taskList = response.body();
                    TodoService todoService = new TodoService();
                    adapter = new TaskCardListAdapter(getContext(), taskList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch tasks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskCardModel>> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDateTime() {
        Runnable updateDateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTimeInMillis());
                String currentTime = DateFormat.getTimeInstance().format(calendar.getTimeInMillis());
                String currentDateAndTime = currentDate + ", " + currentTime;
                dateTime.setText(currentDateAndTime);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateDateTimeRunnable);
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
        mContext.unregisterReceiver(mReceiver);
    }
}