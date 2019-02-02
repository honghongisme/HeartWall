package com.example.administrator.ding.factory.model;

import com.example.administrator.ding.bean.*;
import com.example.administrator.ding.config.Constans;
import com.example.administrator.ding.listener.OnGetCheckResultListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CardDateGetHelper {

    private OkHttpClient client;
    private Gson gson;

    public CardDateGetHelper() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    /**
     * 向服务器请求要展示的数据
     * @param onGetRequestResultListener
     */
    public void requestDataFromServer(final OnGetRequestResultListener onGetRequestResultListener) {
        FormBody body = new FormBody.Builder()
                .add("OperationType", "6")
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "MoodServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetRequestResultListener.onFailed();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    Type type = new TypeToken<ArrayList<CommentItem>>(){}.getType();
                    ArrayList<CommentItem> commentItems = gson.fromJson(jsonObject.getString("random"), type);

                    type = new TypeToken<ArrayList<Integer>>(){}.getType();
                    ArrayList<Integer> commentTags = gson.fromJson(jsonObject.getString("commentTag"), type);

                    onGetRequestResultListener.onSuccess(commentItems, commentTags);
                } catch (JSONException e) {
                    onGetRequestResultListener.onFailed();
                    e.printStackTrace();
                }

            }
        });
    }

    public void insertOneCommentToServer(MoodBadComment comment, final OnGetCheckResultListener onGetCheckResultListener) {
        FormBody body = new FormBody.Builder()
                .add("OperationType", "7")
                .add("Comment", gson.toJson(comment))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "MoodServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetCheckResultListener.onFailed();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getBoolean("CheckResult")) {
                        onGetCheckResultListener.onSuccess();
                    } else {
                        onGetCheckResultListener.onCheckFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnGetRequestResultListener {
        void onFailed();
        void onSuccess(ArrayList<CommentItem> items, ArrayList<Integer> commentTags);
    }


}
