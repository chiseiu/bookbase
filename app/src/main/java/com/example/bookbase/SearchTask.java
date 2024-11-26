package com.example.bookbase;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchTask extends AsyncTask<String, Void, String> {
    private Listener listener;

    @Override
    protected String doInBackground(String... params) {
        Log.d("SearchTask", "doInBackgroundが呼び出されました");

        String result = "[]"; // 空のJSONを初期値として設定
        HttpURLConnection urlConnection = null;
        try {
            // URLを設定
            URL url = new URL("http://172.23.100.41/cgi-bin/saproglab/booksearch_json.py");
            urlConnection = (HttpURLConnection) url.openConnection();

            // HTTPメソッドをPOSTに設定
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // リクエストボディを作成
            String query = "query=" + params[0]; // 例: "query=Android"
            Log.d("SearchTask", "送信したクエリ: " + query);

            // リクエストを送信
            try (OutputStream out = urlConnection.getOutputStream()) {
                out.write(query.getBytes("UTF-8"));
                out.flush();
            }

            // レスポンスコードを確認
            int responseCode = urlConnection.getResponseCode();
            Log.d("SearchTask", "レスポンスコード: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // HTTPステータス200の場合
                // サーバーからのレスポンスを読み取る
                try (InputStream in = urlConnection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {

                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    result = buffer.toString();
                    Log.d("SearchTask", "サーバーからのレスポンス: " + result);
                }
            } else {
                Log.e("SearchTask", "HTTPエラー: レスポンスコード " + responseCode);
            }
        } catch (Exception e) {
            Log.e("SearchTask", "通信エラーが発生しました: " + e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // サーバーからのレスポンスをリスナーに通知
        if (listener != null) {
            if (result != null && !result.isEmpty()) {
                Log.d("SearchTask", "onPostExecuteが正常に呼び出されました: " + result);
                listener.onSuccess(result);
            } else {
                Log.e("SearchTask", "結果が空です");
                listener.onSuccess("[]"); // 空データを通知
            }
        }
    }

    // リスナーを設定するメソッド
    void setListener(Listener listener) {
        this.listener = listener;
    }

    // リスナーインタフェース
    interface Listener {
        void onSuccess(String result);
    }
}
