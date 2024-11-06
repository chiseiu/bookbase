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

        // データの設定
        ArrayList<HashMap<String, String>> listData = new ArrayList<>();
        listData.add(new HashMap<String, String>() {{
            put("title", "Androidの基本");
            put("author", "立命太郎");
        }});
        listData.add(new HashMap<String, String>() {{
            put("title", "Androidの応用");
            put("author", "立命次郎");
        }});
        listData.add(new HashMap<String, String>() {{
            put("title", "Androidのススメ");
            put("author", "立命三郎");
        }});

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
}