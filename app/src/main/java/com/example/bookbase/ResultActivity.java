package com.example.bookbase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class ResultActivity extends AppCompatActivity {



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

        List<BookInfo> bookInfoList = new ArrayList<>();


        // データリストの初期化
        //final ArrayList<HashMap<String, String>> listData = new ArrayList<>();
        //final ArrayList<BookInfo> bookList = new ArrayList<>();

        // SearchTaskのインスタンスを作成
        SearchTask task = new SearchTask();
        task.setListener(new SearchTask.Listener() {
            @Override
            public void onSuccess(List<Book> result) {
                Log.d("ResultActivity", "Success: " + result.size() + " books");

                bookInfoList.clear();
                for (int i = 0; i < result.size(); i++) {
                    Book book = result.get(i);
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setId(i + 1);
                    bookInfo.setTitle(book.getTitle());
                    bookInfo.setAuthor(book.getAuthor());
                    bookInfo.setPublisher(book.getPublisher());
                    bookInfo.setPrice(book.getPrice());
                    bookInfo.setIsbn(book.getIsbn());
                    bookInfoList.add(bookInfo);
                }
                // 更新 ListView
                List<HashMap<String, String>> listData = new ArrayList<>();
                for (Book book : result) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("title", book.getTitle());
                    map.put("author", book.getAuthor());
                    listData.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(
                        ResultActivity.this,
                        listData,
                        android.R.layout.simple_list_item_2,
                        new String[]{"title", "author"},
                        new int[]{android.R.id.text1, android.R.id.text2}
                );

                ListView listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    BookInfo selectedBookInfo = bookInfoList.get(position);

                    Intent detailIntent = new Intent(ResultActivity.this, BookInfoActivity.class);
                    detailIntent.putExtra("id", selectedBookInfo.getId());
                    detailIntent.putExtra("title", selectedBookInfo.getTitle());
                    detailIntent.putExtra("author", selectedBookInfo.getAuthor());
                    detailIntent.putExtra("publisher", selectedBookInfo.getPublisher());
                    detailIntent.putExtra("price", selectedBookInfo.getPrice());
                    detailIntent.putExtra("isbn", selectedBookInfo.getIsbn());

                    startActivity(detailIntent);
                });
            }

            @Override
            public void onError(String error) {
                Log.e("ResultActivity", "Error: " + error);
                Toast.makeText(ResultActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        //task.execute("Android"); // 测试查询

        if (emptyText != null) {
            listView.setEmptyView(emptyText); // データがない場合の表示を設定
        }

        // サーバー通信を実行
        task.execute(query);
    }



}
