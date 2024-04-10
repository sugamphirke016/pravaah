package com.example.pravaah.services.dataModel;

import com.google.gson.annotations.SerializedName;

public class UpdateNotifyStatusModel {
    @SerializedName("column_name")
    String column_name;

    @SerializedName("task_id")
    String task_id;

    @SerializedName("column_val")
    String column_val;

    public UpdateNotifyStatusModel(String column_name, String task_id, String column_val) {
        this.column_name = column_name;
        this.task_id = task_id;
        this.column_val = column_val;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getColumn_val() {
        return column_val;
    }

    public void setColumn_val(String column_val) {
        this.column_val = column_val;
    }
}
