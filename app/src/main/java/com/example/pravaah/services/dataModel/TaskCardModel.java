package com.example.pravaah.services.dataModel;

public class TaskCardModel {
    private String task_id;
    private String title;
    private String description;
    private String label;
    private String status;
    private String priority_num;
    private String start_date;
    private String due_date;
    private String start_time;
    private String end_time;
    private String timer;
    private String notifications_enabled;
    private String alarm_enabled;
    private String priority_label;

    public TaskCardModel(String task_id, String title, String description, String label, String status, String priority_num, String start_date, String due_date, String start_time, String end_time, String timer, String notifications_enabled, String alarm_enabled, String priority_label) {
        this.task_id = task_id;
        this.title = title;
        this.description = description;
        this.label = label;
        this.status = status;
        this.priority_num = priority_num;
        this.start_date = start_date;
        this.due_date = due_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.timer = timer;
        this.notifications_enabled = notifications_enabled;
        this.alarm_enabled = alarm_enabled;
        this.priority_label = priority_label;
    }

    public String getPriority_label() {
        return priority_label;
    }

    public void setPriority_label(String priority_label) {
        this.priority_label = priority_label;
    }

    public void setTimer(String timer) {
        this.timer = timer;
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

    public String getTimer() {
        int minutes;
        try {
            minutes = Integer.parseInt(timer);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input format. Please provide a valid integer string.");
        }

        if (minutes < 0) {
            throw new IllegalArgumentException("Input minutes must be non-negative.");
        }

        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        String formattedHours = String.format("%02d", hours);
        String formattedMinutes = String.format("%02d", remainingMinutes);

        return formattedHours + ":" + formattedMinutes;
    }

    public String getTask_id() {
        return "PVH-TD"+task_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority_num() {
        return "P"+priority_num;
    }

    public void setPriority_num(String priority_num) {
        this.priority_num = priority_num;
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
        String[] parts = start_time.split(":");
        return parts[0] + ":" + parts[1];
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        String[] parts = end_time.split(":");
        return parts[0] + ":" + parts[1];
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

}
