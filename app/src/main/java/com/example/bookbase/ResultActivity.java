package com.example.bookbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "extra_mode";
    public static final int MODE_SEARCH = 1;
    public static final int MODE_BOOKMARK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        // ウィンドウインセットの適用
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int mode = getIntent().getIntExtra(EXTRA_MODE, MODE_SEARCH);
        ArrayList<HashMap<String, String>> listData;


        // データの設定
        if (mode == MODE_SEARCH) {
            // 検索結果のデータを読み込む（例：ここは固定データとしてますが、実際には検索結果をロードする処理に置き換え）
            listData = loadSearchResults();  // 検索結果を読み込む
        } else {
            // ブックマークされたデータを読み込む（例：ブックマークから実際のデータを取得）
            listData = loadBookmarkedBooks();
        }

        // アダプターの設定
        SimpleAdapter adapter = new SimpleAdapter(this,
                listData,   // ListViewに表示するデータ
                android.R.layout.simple_list_item_2, // ListViewで使用するレイアウト（2つのテキスト）
                new String[]{"title", "author"},     // 表示するHashMapのキー
                new int[]{android.R.id.text1, android.R.id.text2} // データを表示するID
        );

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // アイテムクリックリスナーの設定
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResultActivity.this, BookInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<HashMap<String, String>> loadSearchResults() {
        ArrayList<HashMap<String, String>> searchResults = new ArrayList<>();

        // 例：検索結果データ（固定データ）
        searchResults.add(new HashMap<String, String>() {{
            put("title", "Androidの基本");
            put("author", "立命太郎");
        }});
        searchResults.add(new HashMap<String, String>() {{
            put("title", "Androidの応用");
            put("author", "立命次郎");
        }});
        searchResults.add(new HashMap<String, String>() {{
            put("title", "Androidのススメ");
            put("author", "立命三郎");
        }});

        // 実際の検索結果取得処理に置き換え可能
        return searchResults;
    }


    private ArrayList<HashMap<String, String>> loadBookmarkedBooks() {
        ArrayList<HashMap<String, String>> bookmarkedList = new ArrayList<>();

        // ブックマークデータを取得してリストに追加
        // 例としてデモデータを追加
        bookmarkedList.add(new HashMap<String, String>() {{
            put("title", "ブックマークされたAndroid入門");
            put("author", "立命一郎");
        }});
        bookmarkedList.add(new HashMap<String, String>() {{
            put("title", "ブックマークされたAndroidマスター");
            put("author", "立命四郎");
        }});

        return bookmarkedList;
    }
}