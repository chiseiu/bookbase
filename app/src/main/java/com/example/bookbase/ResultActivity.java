package com.example.bookbase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        listView.setEmptyView(emptyText); // データがない場合の表示を設定

        Log.d("ResultActivity", "ListView に emptyView が設定されました");
        // 書籍情報のリスト
        List<BookInfo> bookInfoList = new ArrayList<>();

        // SearchTaskのインスタンスを作成
        SearchTask task = new SearchTask(this);
        task.setListener(new SearchTask.Listener() {
            @Override
            public void onSuccess(List<Book> result) {
                Log.d("ResultActivity", "Success: " + result.size() + " books");

                // 結果をBookInfoリストに変換
                bookInfoList.clear();
                List<HashMap<String, String>> listData = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    Book book = result.get(i);

                    // BookをBookInfoに変換してリストに追加
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setId(i + 1);
                    bookInfo.setTitle(book.getTitle());
                    bookInfo.setAuthor(book.getAuthor());
                    bookInfo.setPublisher(book.getPublisher());
                    bookInfo.setPrice(book.getPrice());
                    bookInfo.setIsbn(book.getIsbn());
                    bookInfoList.add(bookInfo);

                    // ListView用のデータ作成
                    HashMap<String, String> map = new HashMap<>();
                    map.put("title", book.getTitle());
                    map.put("author", book.getAuthor());
                    listData.add(map);
                }

                // ListViewのアダプターを設定
                SimpleAdapter adapter = new SimpleAdapter(
                        ResultActivity.this,
                        listData,
                        android.R.layout.simple_list_item_2,
                        new String[]{"title", "author"},
                        new int[]{android.R.id.text1, android.R.id.text2}
                );
                listView.setAdapter(adapter);

                // リスト項目のクリックイベントを設定
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    BookInfo selectedBookInfo = bookInfoList.get(position);

                    // 書籍情報をBookInfoActivityに渡す
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

        // サーバー通信を実行
        task.execute(query);
    }
}
