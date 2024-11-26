package com.example.bookbase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private class BookInfo extends Book {
        int ID;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 前の画面からクエリを取得
        Intent intent = getIntent();
        String query = intent.getStringExtra("QUERY");

        // ListViewと空データ用TextViewを取得
        ListView listView = findViewById(R.id.listView);
        TextView emptyText = findViewById(R.id.emptyText);


        // データリストの初期化
        final ArrayList<HashMap<String, String>> listData = new ArrayList<>();
        final ArrayList<BookInfo> bookList = new ArrayList<>();

        // SearchTaskのインスタンスを作成
        SearchTask task = new SearchTask();
        task.setListener(new SearchTask.Listener() {
            @Override
            public void onSuccess(String result) {
                Log.d("ResultActivity", "サーバーからのレスポンス: " + result);

                try {
                    // JSONデータを解析
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d("ResultActivity", "JSONデータの数: " + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.d("ResultActivity", "JSONオブジェクト: " + jsonObject.toString());

                        // JSONからBookInfoオブジェクトを作成
                        BookInfo bookInfo = new BookInfo();
                        bookInfo.ID = jsonObject.getInt("ID");
                        bookInfo.title = jsonObject.getString("TITLE");
                        bookInfo.author = jsonObject.getString("AUTHOR");
                        bookInfo.publisher = jsonObject.getString("PUBLISHER");
                        bookInfo.price = jsonObject.getInt("PRICE");
                        bookInfo.isbn = jsonObject.getString("ISBN");

                        // データリストに追加
                        bookList.add(bookInfo);
                        listData.add(new HashMap<String, String>() {{
                            put("title", bookInfo.getTitle());
                            put("author", bookInfo.getAuthor());
                        }});
                    }

                    Log.d("ResultActivity", "bookListのサイズ: " + bookList.size());
                    Log.d("ResultActivity", "listDataのサイズ: " + listData.size());

                    // ListViewにデータをバインド
                    SimpleAdapter adapter = new SimpleAdapter(
                            ResultActivity.this,
                            listData,
                            android.R.layout.simple_list_item_2,
                            new String[]{"title", "author"},
                            new int[]{android.R.id.text1, android.R.id.text2}
                    );
                    listView.setAdapter(adapter);

                    Log.d("ResultActivity", "アダプタのアイテム数: " + listView.getAdapter().getCount());

                    // ListViewのアイテムクリック時の処理
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        BookInfo bookInfo = bookList.get(position);
                        Log.d("ResultActivity", "クリックされた本: " + bookInfo.toString());

                        // BookInfoActivityに遷移
                        Intent intent = new Intent(ResultActivity.this, BookInfoActivity.class);
                        intent.putExtra("title", bookInfo.getTitle());
                        intent.putExtra("author", bookInfo.getAuthor());
                        intent.putExtra("publisher", bookInfo.getPublisher());
                        intent.putExtra("price", bookInfo.getPrice());
                        intent.putExtra("isbn", bookInfo.getIsbn());
                        startActivity(intent);
                    });
                } catch (JSONException e) {
                    Log.e("ResultActivity", "JSON解析エラー: ", e);
                }
            }
        });
        if (emptyText != null) {
            listView.setEmptyView(emptyText); // データがない場合の表示を設定
        }

        // サーバー通信を実行
        task.execute(query);
    }



}
