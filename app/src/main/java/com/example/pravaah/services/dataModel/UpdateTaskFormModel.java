package com.example.pravaah.services.dataModel;

import com.google.gson.annotations.SerializedName;

public class UpdateTaskFormModel {

    @SerializedName("task_id")
    private String task_id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("label")
    private String label;

    @SerializedName("priority_label")
    private String priority_label;

    @SerializedName("start_date")
    private String start_date;

    @SerializedName("due_date")
    private String due_date;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_time;

    @SerializedName("recurrence")
    private String recurrence;

    @SerializedName("notifications_enabled")
    private String notifications_enabled;

    @SerializedName("alarm_enabled")
    private String alarm_enabled;

    public UpdateTaskFormModel(String task_id, String title, String description, String label, String priority_label, String start_date, String due_date, String start_time, String end_time, String recurrence, String notifications_enabled, String alarm_enabled) {
        this.task_id = task_id;
        this.title = title;
        this.description = description;
        this.label = label;
        this.priority_label = priority_label;
        this.start_date = start_date;
        this.due_date = due_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.recurrence = recurrence;
        this.notifications_enabled = notifications_enabled;
        this.alarm_enabled = alarm_enabled;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPriority_label() {
        return priority_label;
    }

    public void setPriority_label(String priority_label) {
        this.priority_label = priority_label;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getNotifications_enabled() {
        return notifications_enabled;
    }

    public void setNotifications_enabled(String notifications_enabled) {
        this.notifications_enabled = notifications_enabled;
    }

    public String getAlarm_enabled() {
        return alarm_enabled;
    }

    public void setAlarm_enabled(String alarm_enabled) {
        this.alarm_enabled = alarm_enabled;
    }
}
