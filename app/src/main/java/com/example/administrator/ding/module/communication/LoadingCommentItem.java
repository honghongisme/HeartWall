package com.example.administrator.ding.module.communication;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.administrator.ding.model.view.CommentItem;
import com.example.administrator.ding.model.entry.MoodBadNail;
import com.example.administrator.ding.base.Constans;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoadingCommentItem {

    private Gson gson;

    public LoadingCommentItem() {
        gson = new Gson();
    }

    public void getCommentItemList(final Context context, int id, int x, int y, final OnGetRequestResultListener onGetRequestResultListener) {
        MoodBadNail nail = new MoodBadNail();
        nail.setId(id);
        nail.setX(x);
        nail.setY(y);

        System.out.println("-------------id = " + id + " ; x = " + x + " : y = " + y);

        FormBody body = new FormBody.Builder()
                .add("OperationType", "5")
                .add("SBadNail", gson.toJson(nail))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "MoodServlet")
                .post(body)
                .build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (NetStateCheckHelper.isNetWork(context)) {
                    onGetRequestResultListener.onError();
                } else {
                    onGetRequestResultListener.onFailed();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String info = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    String commentItemJsonList = jsonObject.getString("commentItem");
                    Type type = new TypeToken<ArrayList<CommentItem>>(){}.getType();
                    ArrayList<CommentItem> commentItems = gson.fromJson(commentItemJsonList, type);
                    onGetRequestResultListener.onSuccess(commentItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnGetRequestResultListener {
        void onSuccess(ArrayList<CommentItem> commentItems);
        void onFailed();
        void onError();
    }

}
