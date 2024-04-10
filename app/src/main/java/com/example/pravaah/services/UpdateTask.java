package com.example.pravaah.services;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.example.pravaah.R;
import com.example.pravaah.services.adapter.TaskCardListAdapter;
import com.example.pravaah.services.dataModel.CreateTaskFormModel;
import com.example.pravaah.services.dataModel.TaskCardModel;
import com.example.pravaah.services.dataModel.UpdateTaskFormModel;
import com.example.pravaah.services.retrofit.ApiService;
import com.example.pravaah.services.retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTask extends AppCompatActivity implements UpdateTaskListener {

    TaskCardListAdapter taskCardListAdapter = new TaskCardListAdapter();
    AutoCompleteTextView labelDropdown;
    AutoCompleteTextView priorityDropdown;
    AutoCompleteTextView setTypeDropdown;
    private TextInputEditText editTextTaskTitle;
    private EditText editTextTaskDescription;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);
    }

    @Override
    public void onUpdateTaskClicked(String task_id, Context context) {
        final String id = task_id.substring("PVH-TD".length());
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_update_task);

        String[] labels = {"Work", "Personal", "Common"};
        ArrayAdapter<String> labelAdapter;
        labelAdapter = new ArrayAdapter<>(context, R.layout.list_label_item_todo, labels);
        labelDropdown = dialog.findViewById(R.id.dropdown_field_label);
        if(labelDropdown != null) {
            labelDropdown.setAdapter(labelAdapter);
        }

        String[] priority = {"Low", "Moderate", "High", "Urgent"};
        ArrayAdapter<String> priorityAdapter;
        priorityAdapter = new ArrayAdapter<>(context, R.layout.list_label_item_todo, priority);
        priorityDropdown = dialog.findViewById(R.id.dropdown_field_priority);
        priorityDropdown.setAdapter(priorityAdapter);

        spinner = dialog.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> setTypeAdapter = ArrayAdapter.createFromResource(context, R.array.setType, android.R.layout.simple_spinner_item);
        setTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(setTypeAdapter);

        ImageButton cancelButton = dialog.findViewById(R.id.closeBtn);
        ImageButton buttonStartDate = dialog.findViewById(R.id.buttonStartDate);
        TextView editTextStartDate = dialog.findViewById(R.id.editTextStartDate);
        ImageButton buttonDueDate = dialog.findViewById(R.id.buttonDueDate);
        ImageButton buttonStartTime = dialog.findViewById(R.id.buttonStartTime);
        ImageButton buttonEndTime = dialog.findViewById(R.id.buttonEndTime);
        TextView editTextDueDate = dialog.findViewById(R.id.editTextDueDate);
        TextView editTextStartTime = dialog.findViewById(R.id.editTextStartTime);
        TextView editTextEndTime = dialog.findViewById(R.id.editTextEndTime);
        ImageButton notifyButton = dialog.findViewById(R.id.notifyButton);
        ImageButton alarmButton = dialog.findViewById(R.id.alarmButton);
        editTextTaskTitle = dialog.findViewById(R.id.editTextTaskTitleUpdate);
        editTextTaskDescription = dialog.findViewById(R.id.editTextTaskDescription);
        CheckBox checkBoxDaily = dialog.findViewById(R.id.checkBoxDaily);
        Button buttonUpdateTask = dialog.findViewById(R.id.buttonUpdateTask);

        buttonStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                editTextStartDate.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        buttonDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                editTextDueDate.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editTextStartTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hourOfDay, minute, true);
                timePickerDialog.show();
            }
        });

        buttonEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editTextEndTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hourOfDay, minute, true);
                timePickerDialog.show();
            }
        });

        alarmButton.setOnClickListener(new View.OnClickListener() {
            boolean isImage1 = true;

            @Override
            public void onClick(View view) {
                if (!isImage1) {
                    alarmButton.setImageResource(R.drawable.baseline_alarm_on_24);
                    alarmButton.setBackgroundResource(R.drawable.notifications_taskcard);
                } else {
                    alarmButton.setImageResource(R.drawable.baseline_alarm_on_black);
                    alarmButton.setBackgroundResource(R.drawable.notifications_toggeled_taskcard);
                }
                isImage1 = !isImage1;
            }
        });

        notifyButton.setOnClickListener(new View.OnClickListener() {
            boolean isImage12 = true;

            @Override
            public void onClick(View view) {
                if (!isImage12) {
                    notifyButton.setImageResource(R.drawable.baseline_notifications_active_24);
                    notifyButton.setBackgroundResource(R.drawable.notifications_taskcard);
                } else {
                    notifyButton.setImageResource(R.drawable.baseline_notifications_active_black);
                    notifyButton.setBackgroundResource(R.drawable.notifications_toggeled_taskcard);
                }
                isImage12 = !isImage12;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTaskTitle.getText().toString();
                String description = editTextTaskDescription.getText().toString();
                String label = labelDropdown.getText().toString();
                String priority_label = priorityDropdown.getText().toString();

                String start_date = editTextStartDate.getText().toString();
                String due_date = editTextDueDate.getText().toString();
                String start_time = editTextStartTime.getText().toString();
                String end_time = editTextEndTime.getText().toString();

                int alarmEnabled = 0;
                if (alarmButton.getBackground().getConstantState().equals(ContextCompat.getDrawable(view.getContext(), R.drawable.notifications_toggeled_taskcard).getConstantState())) {
                    alarmEnabled = 1;
                }
                String alarm_enabled = (alarmEnabled == 1) ? "1" : "0";
                int notificationEnabled = 0;
                if (notifyButton.getBackground().getConstantState().equals(ContextCompat.getDrawable(view.getContext(), R.drawable.notifications_toggeled_taskcard).getConstantState())) {
                    notificationEnabled = 1;
                }
                String notifications_enabled = (notificationEnabled==1) ? "1" : "0";
                String recurr = (checkBoxDaily.isChecked()) ? spinner.getSelectedItem().toString() : "0";

                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Call<UpdateTaskFormModel> call = apiService.updateTaskById(id, title, description, label, priority_label, start_date, due_date, start_time, end_time, recurr, notifications_enabled, alarm_enabled);
                call.enqueue(new Callback<UpdateTaskFormModel>() {

                    @Override
                    public void onResponse(Call<UpdateTaskFormModel> call, Response<UpdateTaskFormModel> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Task [" + task_id + "] Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("REFRESH_LAYOUT");
                            context.sendBroadcast(intent);
                            dialog.dismiss();
                        }else {
                            Toast.makeText(view.getContext(), "Error Updating Task", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UpdateTaskFormModel> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<TaskCardModel> call = apiService.getTaskFromId(id);
        call.enqueue(new Callback<TaskCardModel>() {
            @Override
            public void onResponse(Call<TaskCardModel> call, Response<TaskCardModel> response) {
                if(response.isSuccessful()) {
                    TaskCardModel task = response.body();
                    editTextTaskTitle.setText(task.getTitle());
                    if(!Objects.equals(task.getDescription(), "") || task.getDescription() != null)
                        editTextTaskDescription.setText(task.getDescription());
                    else editTextTaskDescription.setText("N.A.");
                    editTextStartDate.setText(convertDateFormat(task.getStart_date()));
                    editTextStartTime.setText(task.getStart_time());
                    editTextDueDate.setText(convertDateFormat(task.getDue_date()));
                    editTextEndTime.setText(task.getEnd_time());
                    labelDropdown.setText(task.getLabel(), false);
                    priorityDropdown.setText(task.getPriority_label(), false);

                    if ("0".equals(task.getAlarm_enabled())) {
                        alarmButton.setImageResource(R.drawable.baseline_alarm_on_24);
                        alarmButton.setBackgroundResource(R.drawable.notifications_taskcard);
                    } else {
                        alarmButton.setImageResource(R.drawable.baseline_alarm_on_black);
                        alarmButton.setBackgroundResource(R.drawable.notifications_toggeled_taskcard);
                    }

                    if ("0".equals(task.getNotifications_enabled())) {
                        notifyButton.setImageResource(R.drawable.baseline_notifications_active_24);
                        notifyButton.setBackgroundResource(R.drawable.notifications_taskcard);
                    } else {
                        notifyButton.setImageResource(R.drawable.baseline_notifications_active_black);
                        notifyButton.setBackgroundResource(R.drawable.notifications_toggeled_taskcard);
                    }


                } else {
                    Log.e("Server Error", "The server response wasn't good enough!");
                }
            }

            @Override
            public void onFailure(Call<TaskCardModel> call, Throwable t) {
                Log.e("Network Error", "Cannot fetch the task details, try again later!");
            }
        });
    }

    public static String convertDateFormat(String dateString) {
        String formattedDate = "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("d/M/yyyy");
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}