package com.example.bookbase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchTask extends AsyncTask<String, Void, List<Book>> {
    private Listener listener;

    public interface Listener {
        void onSuccess(List<Book> result);
        void onError(String error);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Book> doInBackground(String... params) {
        List<Book> books = new ArrayList<>();
        String query = params[0].trim();
        HttpURLConnection urlConnection = null;

        try {
            // 1. 设置 URL
            URL url = new URL("http://172.23.100.41/cgi-bin/saproglab/booksearch_json.py");
            urlConnection = (HttpURLConnection) url.openConnection();

            // 2. 设置 POST 请求属性
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // 3. 发送请求
            String body = "query=" + query;
            Log.d("SearchTask", "发送的查询: " + body);

            try (OutputStream out = urlConnection.getOutputStream()) {
                out.write(body.getBytes("UTF-8"));
                out.flush();
            }

            // 4. 检查响应代码
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 5. 读取响应
                try (InputStream in = urlConnection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }

                    // 6. 解析 JSON 响应
                    String jsonResponse = responseBuilder.toString();
                    Log.d("SearchTask", "响应数据: " + jsonResponse);
                    books = parseJsonResponse(jsonResponse);
                }
            } else {
                Log.e("SearchTask", "HTTP 错误: 响应代码 " + responseCode);
                throw new Exception("HTTP 错误: 响应代码 " + responseCode);
            }
        } catch (Exception e) {
            Log.e("SearchTask", "通信错误: " + e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return books;
    }

    @Override
    protected void onPostExecute(List<Book> result) {
        if (listener != null) {
            if (result != null && !result.isEmpty()) {
                listener.onSuccess(result);
            } else {
                listener.onError("没有找到结果");
            }
        }
    }

    // 解析 JSON 响应为书籍列表
    private List<Book> parseJsonResponse(String jsonResponse) throws JSONException {
        List<Book> books = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Book book = new Book();
            book.setTitle(jsonObject.getString("TITLE"));
            book.setAuthor(jsonObject.getString("AUTHOR"));
            book.setPublisher(jsonObject.getString("PUBLISHER"));
            book.setPrice(jsonObject.getInt("PRICE"));
            book.setIsbn(jsonObject.getString("ISBN"));

            books.add(book);
        }

        return books;
    }
}
