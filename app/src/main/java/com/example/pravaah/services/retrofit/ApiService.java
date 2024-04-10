package com.example.pravaah.services.retrofit;

import com.example.pravaah.services.dataModel.CreateTaskFormModel;
import com.example.pravaah.services.dataModel.TaskCardModel;
import com.example.pravaah.services.dataModel.UpdateNotifyStatusModel;
import com.example.pravaah.services.dataModel.UpdateTaskFormModel;
import com.google.gson.JsonObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("pravaah/root/fetch_todays_tasks.php")
    Call<List<TaskCardModel>> getTasks();

    @GET("pravaah/root/fetch_count_of_todays_tasks.php")
    Call<JsonObject> getCountOfTasks();

    @GET("pravaah/root/fetch_task_from_id.php")
    Call<TaskCardModel> getTaskFromId(
            @Query("task_id") String taskId
    );

    @FormUrlEncoded
    @POST("pravaah/root/create_task.php")
    Call<CreateTaskFormModel> createTask(
            @Field("title") String title,
            @Field("description") String description,
            @Field("label") String label,
            @Field("priority_label") String priority_label,
            @Field("start_date") String start_date,
            @Field("due_date") String due_date,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,
            @Field("recurrence") String recurrence,
            @Field("alarm_enabled") String alarm_enabled,
            @Field("notifications_enabled") String notifications_enabled
    );

    @FormUrlEncoded
    @POST("pravaah/root/update_task_by_id.php")
    Call<UpdateTaskFormModel> updateTaskById(
            @Field("task_id") String task_id,
            @Field("title") String title,
            @Field("description") String description,
            @Field("label") String label,
            @Field("priority_label") String priority_label,
            @Field("start_date") String start_date,
            @Field("due_date") String due_date,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,
            @Field("recurrence") String recurrence,
            @Field("alarm_enabled") String alarm_enabled,
            @Field("notifications_enabled") String notifications_enabled
    );

    @FormUrlEncoded
    @POST("pravaah/root/update_task.php")
    Call<UpdateNotifyStatusModel> updateTask(
            @Field("task_id") String task_id,
            @Field("column_name") String column_name,
            @Field("column_val") String column_val
    );

    @FormUrlEncoded
    @POST("pravaah/root/delete_task.php")
    Call<String> deleteTask(
            @Field("task_id") String task_id
    );

    @FormUrlEncoded
    @POST("pravaah/root/start_task.php")
    Call<String> startTask(
            @Field("task_id") String task_id
    );

    @FormUrlEncoded
    @POST("pravaah/root/stop_task.php")
    Call<String> stopTask(
            @Field("task_id") String task_id
    );

}
