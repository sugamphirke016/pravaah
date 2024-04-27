package com.example.pravaah.services.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pravaah.R;
import com.example.pravaah.services.PomodoroFragment;
import com.example.pravaah.services.TodoService;
import com.example.pravaah.services.UpdateTask;
import com.example.pravaah.services.UpdateTaskListener;
import com.example.pravaah.services.dataModel.TaskCardModel;
import com.example.pravaah.services.dataModel.UpdateNotifyStatusModel;
import com.example.pravaah.services.retrofit.ApiService;
import com.example.pravaah.services.retrofit.RetrofitClient;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskCardListAdapter extends RecyclerView.Adapter<TaskCardListAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskTitle;
        public TextView priority;
        public TextView time;
        public TextView description;
        public ImageButton alarmBtn;
        public ImageButton notifyBtn;
        public TextView category;
        public ImageButton hamMenu;
        public TextView taskId;
        public TextView taskStatus;
        public TextView timer;
        public LinearLayout multiDayExtension;
        public TextView multiDayStartnEndDateTV;
        public ProgressBar progress_barEXT;
        public ImageButton pomodoro_taskcard;

        public ViewHolder(View itemView) {
            super(itemView);
            taskTitle = (TextView) itemView.findViewById(R.id.task_title);
            priority = (TextView) itemView.findViewById(R.id.priority);
            time = (TextView) itemView.findViewById(R.id.time);
            description = (TextView) itemView.findViewById(R.id.description);
            alarmBtn = (ImageButton) itemView.findViewById(R.id.alarmBtn);
            notifyBtn = (ImageButton) itemView.findViewById(R.id.notifyBtn);
            category = (TextView) itemView.findViewById(R.id.category);
            hamMenu = (ImageButton) itemView.findViewById(R.id.hamMenu);
            taskId = (TextView) itemView.findViewById(R.id.taskId);
            taskStatus = (TextView) itemView.findViewById(R.id.taskStatus);
            timer = (TextView) itemView.findViewById(R.id.timer);
            multiDayExtension = itemView.findViewById(R.id.multiDayExtension);
            multiDayStartnEndDateTV = (TextView) itemView.findViewById(R.id.multiDayStartnEndDateTV);
            progress_barEXT = itemView.findViewById(R.id.progress_barEXT);
            pomodoro_taskcard = itemView.findViewById(R.id.pomodoro_taskcard);
        }
    }
    private Context context;
    private List<TaskCardModel> taskCards;
    public TaskCardListAdapter(Context context, List<TaskCardModel> taskCardModels) {
        this.context = context;
        taskCards = taskCardModels;
    }
    public TaskCardListAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.taskcard, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int color = 0xFFFABA35;
        TaskCardModel taskCardModel = taskCards.get(position);
        TextView tv1 = holder.taskTitle;
        SpannableString content = new SpannableString(taskCardModel.getTitle());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(color), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv1.setText(content);
        String timing = taskCardModel.getStart_time()+"-"+taskCardModel.getEnd_time();
        holder.time.setText(timing);
        holder.description.setText(taskCardModel.getDescription());
        if(Objects.equals(taskCardModel.getNotifications_enabled(), "1")){
            holder.notifyBtn.setImageResource(R.drawable.baseline_notifications_active_24);
        } else {
            holder.notifyBtn.setImageResource(R.drawable.baseline_notifications_off_24);
        }
        if(Objects.equals(taskCardModel.getAlarm_enabled(), "1")) {
            holder.alarmBtn.setImageResource(R.drawable.baseline_alarm_on_24);
        } else {
            holder.alarmBtn.setImageResource(R.drawable.baseline_alarm_off_24);
        }

        String alarmState = taskCardModel.getAlarm_enabled();
        String notifyState = taskCardModel.getNotifications_enabled();
        toggleAlarmImage(holder.alarmBtn, R.drawable.baseline_alarm_off_24, R.drawable.baseline_alarm_on_24, taskCardModel.getTask_id(), "Alarm", "alarm_enabled", alarmState);
        toggleAlarmImage(holder.notifyBtn, R.drawable.baseline_notifications_off_24, R.drawable.baseline_notifications_active_24, taskCardModel.getTask_id(), "Notification", "notifications_enabled", notifyState);
        holder.category.setText(taskCardModel.getLabel());
        holder.hamMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view,  holder.getAdapterPosition());
            }
        });
        holder.pomodoro_taskcard.setOnClickListener(new View.OnClickListener() {         // yet to resolve errors for this
            @Override
            public void onClick(View view) {
                TodoService todoService = new TodoService();
                todoService.replaceFragment(new PomodoroFragment());
            }
        });
        holder.taskId.setText(taskCardModel.getTask_id());
        holder.taskStatus.setText(taskCardModel.getStatus());
        if ("Completed".equals(taskCardModel.getStatus())) {
            holder.taskStatus.setBackgroundResource(R.drawable.status_taskcard_complete);
            holder.taskStatus.setTextColor(ContextCompat.getColor(context, R.color.White));
        }
        else if ("In Progress".equals(taskCardModel.getStatus())) {
            holder.taskStatus.setBackgroundResource(R.drawable.status_taskcard_in_progress);
            holder.taskStatus.setTextColor(ContextCompat.getColor(context, R.color.White));
        }
        holder.priority.setText(taskCardModel.getPriority_num());
        holder.timer.setText(taskCardModel.getTimer());
        String start_date = taskCardModel.getStart_date();
        String due_date = taskCardModel.getDue_date();
        boolean isMultiDayTask = !start_date.equals(due_date);
        if (isMultiDayTask) {
            holder.multiDayExtension.setVisibility(View.VISIBLE);
            String[] startDateParts = start_date.split("-");
            String[] dueDateParts = due_date.split("-");
            int startMonth = Integer.parseInt(startDateParts[1]);
            int startDay = Integer.parseInt(startDateParts[2]);
            int dueMonth = Integer.parseInt(dueDateParts[1]);
            int dueDay = Integer.parseInt(dueDateParts[2]);
            String formattedStartDate = startDay + " " + getMonthName(startMonth) + " - " + dueDay + " " + getMonthName(dueMonth);
            holder.multiDayStartnEndDateTV.setText(formattedStartDate);

            String start_time = taskCardModel.getStart_time();
            String end_time = taskCardModel.getEnd_time();
            LocalDateTime startDateTime = LocalDateTime.parse(start_date + " " + start_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime endDateTime = LocalDateTime.parse(due_date + " " + end_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String check1 = start_date + " " + start_time;
            String check2 = due_date + " " + end_time;
            Log.i("check1", check1);
            Log.i("check2", check2);
            Log.i("check1.length", ""+check1.length());
            Log.i("check2.length", ""+check2.length());
            long totalMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
            ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
            ZonedDateTime currentIST = ZonedDateTime.now(istZoneId);
            long minutesToCurrent = Duration.between(startDateTime, currentIST).toMinutes();
            long progressPercent = (long)(((double)minutesToCurrent/totalMinutes)*100);
            holder.progress_barEXT.setProgress((int) progressPercent);
        } else {
            holder.multiDayExtension.setVisibility(View.GONE);
        }

    }
    private String getMonthName(int month) {
        String[] monthNames = {"Jan", "Feb", "March", "April", "May", "June",
                "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }
    private void toggleAlarmImage(final ImageButton imageButton, final int imageResource1, final int imageResource2, final String task_id, final String typeBtn, final String column_name, final String state) {
        imageButton.setOnClickListener(new View.OnClickListener() {
            boolean isImage1 = Objects.equals(state, "1");
            final String id = task_id.substring("PVH-TD".length());

            @Override
            public void onClick(View view) {
                if (isImage1) {
                    imageButton.setImageResource(imageResource1);
                    ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                    Call<UpdateNotifyStatusModel> call = apiService.updateTask(id, column_name, "0");
                    call.enqueue(new Callback<UpdateNotifyStatusModel>() {

                        @Override
                        public void onResponse(Call<UpdateNotifyStatusModel> call, Response<UpdateNotifyStatusModel> response) {
                            String responseBody = String.valueOf(response.body());
                            Log.i("Successful Response", responseBody);
                            showToast(typeBtn+" disabled for "+task_id);
                            Intent intent = new Intent("REFRESH_LAYOUT");
                            context.sendBroadcast(intent);
                        }

                        @Override
                        public void onFailure(Call<UpdateNotifyStatusModel> call, Throwable t) {
                            showToast("Server Error");
                        }
                    });
                } else {
                    imageButton.setImageResource(imageResource2);
                    ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                    Call<UpdateNotifyStatusModel> call = apiService.updateTask(id, column_name, "1");
                    call.enqueue(new Callback<UpdateNotifyStatusModel>() {

                        @Override
                        public void onResponse(Call<UpdateNotifyStatusModel> call, Response<UpdateNotifyStatusModel> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(view.getContext(), typeBtn+" enabled for "+task_id, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent("REFRESH_LAYOUT");
                                context.sendBroadcast(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateNotifyStatusModel> call, Throwable t) {
                            Toast.makeText(view.getContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                isImage1 = !isImage1;
            }
        });
    }
    @Override
    public int getItemCount() {
        return taskCards.size();
    }
    private MenuItem startStopMenuItem;
    public void showMenu(final View v, int adapterPosition) {

        final ImageButton menuButton = (ImageButton) v;
        final PopupMenu popupMenu = new PopupMenu(context, menuButton);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.action_start_stop) {
                    if (Objects.equals(taskCards.get(adapterPosition).getStatus(), "In Progress")) {
                        stopTask(taskCards.get(adapterPosition).getTask_id());
                    } else {
                        startTask(taskCards.get(adapterPosition).getTask_id());
                    }
                    popupMenu.dismiss();
                    return true;
                } else if (itemId == R.id.action_mark_completed) {
                    markAsCompleted(taskCards.get(adapterPosition).getTask_id());
                    popupMenu.dismiss();
                    return true;
                } else if (itemId == R.id.action_update) {
                    UpdateTask updateTask = new UpdateTask();
                    updateTask.onUpdateTaskClicked(taskCards.get(adapterPosition).getTask_id(), context);
                    popupMenu.dismiss();
                    return true;
                } else if (itemId == R.id.action_delete) {
                    deleteTask(taskCards.get(adapterPosition).getTask_id());
                    popupMenu.dismiss();
                    return true;
                } else if (itemId == R.id.action_create_subtask) {
                    showToast("Created Subtask");
                    popupMenu.dismiss();
                    return true;
                } else {
                    return false;
                }

            }
        });
        popupMenu.inflate(R.menu.taskcard_menu);
        startStopMenuItem = popupMenu.getMenu().findItem(R.id.action_start_stop);
        if(Objects.equals(taskCards.get(adapterPosition).getStatus(), "In Progress")) startStopMenuItem.setTitle("Stop Task");
        else startStopMenuItem.setTitle("Start Task");
        popupMenu.show();
    }

    private void startTask(String task_id) {
        final String id = task_id.substring("PVH-TD".length());
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<String> call = apiService.startTask(id);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    showToast("Task has started");
                    Intent intent = new Intent("REFRESH_LAYOUT");
                    context.sendBroadcast(intent);
                } else {
                    showToast("Server Error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("Network error " + t.getMessage());
            }
        });
    }

    private void stopTask(String task_id) {
        final String id = task_id.substring("PVH-TD".length());
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<String> call = apiService.stopTask(id);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    showToast("Task has stopped");
                    Intent intent = new Intent("REFRESH_LAYOUT");
                    context.sendBroadcast(intent);
                } else {
                    showToast("Server Error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("Network error " + t.getMessage());
            }
        });
    }

    private void markAsCompleted(String task_id) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_mark_as_completed_dialog);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        Button btnDialogNo = dialog.findViewById(R.id.btnDialogNo);
        Button btnDialogYes = dialog.findViewById(R.id.btnDialogYes);
        final String id = task_id.substring("PVH-TD".length());

        btnDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Call<UpdateNotifyStatusModel> call = apiService.updateTask(id, "status", "Completed");
                call.enqueue(new Callback<UpdateNotifyStatusModel>() {
                    @Override
                    public void onResponse(Call<UpdateNotifyStatusModel> call, Response<UpdateNotifyStatusModel> response) {
                        if(response.isSuccessful()) {
                            showToast("Task with Task id: PVH-TD78 marked as complete");
                            Log.d("Server Response" , response.toString());
                            Intent intent = new Intent("REFRESH_LAYOUT");
                            context.sendBroadcast(intent);
                        }
                        else {
                            showToast("Server Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateNotifyStatusModel> call, Throwable t) {
                        showToast("Network Error" + t.getMessage());
                    }
                });
                dialog.dismiss();
            }
        });
    }

    private void deleteTask(String task_id) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_delete_dialog);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        Button btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        Button btnDialogDelete = dialog.findViewById(R.id.btnDialogDelete);
        final String id = task_id.substring("PVH-TD".length());

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnDialogDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Call<String> call = apiService.deleteTask(id);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            String responseBody = response.body();
                            Log.i("Successful Response", responseBody);
                            showToast("Task deleted");
                            Intent intent = new Intent("REFRESH_LAYOUT");
                            context.sendBroadcast(intent);
                        }
                        else{
                            try {
                                String errorBodyString = response.errorBody().string();
                                Log.e("Unsuccessful response", errorBodyString);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            showToast("Server Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Network Error", t.getMessage());
                        showToast("Network Error");
                    }
                });
                dialog.dismiss();
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
