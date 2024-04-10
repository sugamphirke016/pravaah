package com.example.pravaah.services;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.example.pravaah.R;
import com.example.pravaah.databinding.ActivityTodoServiceBinding;
import com.example.pravaah.services.dataModel.CreateTaskFormModel;
import com.example.pravaah.services.retrofit.ApiService;
import com.example.pravaah.services.retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoService extends AppCompatActivity {
    ActivityTodoServiceBinding binding;
    AutoCompleteTextView labelDropdown;
    AutoCompleteTextView priorityDropdown;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_CODE_SPEECH_INPUT_DESC = 1001;
    private EditText editTextTaskTitle;
    private EditText editTextTaskDescription;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id=item.getItemId();

            if(id == R.id.home) { replaceFragment(new HomeFragment()); }
            else if(id == R.id.pomodoro) { replaceFragment(new PomodoroFragment()); }
            else if(id == R.id.schedule) { replaceFragment(new SchedulerFragment()); }
            else if(id == R.id.habit) { replaceFragment(new HabitFragment()); }
            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_create_task);

        String[] labels = {"Work", "Personal", "Common"};
        ArrayAdapter<String> labelAdapter;
        labelAdapter = new ArrayAdapter<>(this, R.layout.list_label_item_todo, labels);
        labelDropdown = dialog.findViewById(R.id.dropdown_field_label);
        labelDropdown.setAdapter(labelAdapter);

        String[] priority = {"Low", "Moderate", "High", "Urgent"};
        ArrayAdapter<String> priorityAdapter;
        priorityAdapter = new ArrayAdapter<>(this, R.layout.list_label_item_todo, priority);
        priorityDropdown = dialog.findViewById(R.id.dropdown_field_priority);
        priorityDropdown.setAdapter(priorityAdapter);

        spinner = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> setTypeAdapter = ArrayAdapter.createFromResource(this, R.array.setType, android.R.layout.simple_spinner_item);
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
        editTextTaskTitle = dialog.findViewById(R.id.editTextTaskTitle);
        editTextTaskDescription = dialog.findViewById(R.id.editTextTaskDescription);
        CheckBox checkBoxDaily = dialog.findViewById(R.id.checkBoxDaily);
        Button buttonAddTask = dialog.findViewById(R.id.buttonAddTask);
        ImageButton micForTitle = dialog.findViewById(R.id.micButtonForTitle);
        ImageButton micForDesc = dialog.findViewById(R.id.micButtonForDesc);

        micForTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Converting your voice to text");
                try{
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch(Exception e) {
                    Toast.makeText(TodoService.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        micForDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Converting your voice to text");
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_DESC);
                } catch(Exception e) {
                    Toast.makeText(TodoService.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            boolean isImage1 = true;

            @Override
            public void onClick(View view) {
                if (!isImage1) {
                    notifyButton.setImageResource(R.drawable.baseline_notifications_active_24);
                    notifyButton.setBackgroundResource(R.drawable.notifications_taskcard);
                } else {
                    notifyButton.setImageResource(R.drawable.baseline_notifications_active_black);
                    notifyButton.setBackgroundResource(R.drawable.notifications_toggeled_taskcard);
                }
                isImage1 = !isImage1;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
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

                if (start_date == null || start_date.isEmpty()) {
                    DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
                    Date currentDate = new Date();
                    start_date = dateFormat.format(currentDate);
                }

                if (due_date == null || due_date.isEmpty()) {
                    DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
                    Date currentDate = new Date();
                    due_date = dateFormat.format(currentDate);
                }

                if (start_time == null || start_time.isEmpty()) {
                    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                    Date currentTime = new Date();
                    start_time = timeFormat.format(currentTime);
                }

                if (end_time == null || end_time.isEmpty()) {
                    end_time = "23:59";
                }

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
                Call<CreateTaskFormModel> call = apiService.createTask(title, description, label, priority_label, start_date, due_date, start_time, end_time, recurr, notifications_enabled, alarm_enabled);
                call.enqueue(new Callback<CreateTaskFormModel>() {

                    @Override
                    public void onResponse(Call<CreateTaskFormModel> call, Response<CreateTaskFormModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TodoService.this, "Task created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("REFRESH_LAYOUT");
                            TodoService.this.sendBroadcast(intent);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(TodoService.this, "Failed to create task", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateTaskFormModel> call, Throwable t) {
                        Toast.makeText(TodoService.this, "Network error", Toast.LENGTH_SHORT).show();
                        Log.e("Network Error", "Failed to create task", t);
                    }
                });
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = formatTitle(result.get(0));
                    editTextTaskTitle.setText(text);
                }
                break;
            }
            case REQUEST_CODE_SPEECH_INPUT_DESC: {
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = formatDesc(result.get(0));
                    editTextTaskDescription.setText(text);
                }
                break;
            }
        }
    }

    private String formatTitle(String inputText) {
        StringBuilder formattedText = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : inputText.toCharArray()) {
            if (capitalizeNext && Character.isLetter(c)) {
                formattedText.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                formattedText.append(c);
            }
            if (c == '.' || c == '?' || c == '!') {
                capitalizeNext = true;
            }
        }
        return formattedText.toString();
    }

    private String formatDesc(String inputText) {
        StringBuilder formattedText = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : inputText.toCharArray()) {
            if (capitalizeNext && Character.isLetter(c)) {
                formattedText.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                formattedText.append(c);
            }
            if (c == '.' || c == '?' || c == '!') {
                capitalizeNext = true;
            }
        }
        if (!inputText.endsWith(".") && !inputText.endsWith("?") && !inputText.endsWith("!")) {
            formattedText.append(".");
        }
        return formattedText.toString();
    }

}