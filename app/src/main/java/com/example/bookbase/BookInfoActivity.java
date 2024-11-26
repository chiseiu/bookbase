package com.example.bookbase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BookInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // EdgeToEdgeモードを有効にする
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_info);

        // システムバーの余白を設定
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Intentからデータを取得
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String publisher = intent.getStringExtra("publisher");
        int price = intent.getIntExtra("price", 0);
        String isbn = intent.getStringExtra("isbn");

        // ユーザインタフェースコンポーネントへの参照を取得
        TextView titleView = findViewById(R.id.titleView);
        TextView authorView = findViewById(R.id.authorView);
        TextView publisherView = findViewById(R.id.publisherView);
        TextView priceView = findViewById(R.id.priceView);
        TextView isbnView = findViewById(R.id.isbnView);

        // 書籍情報を画面に表示
        titleView.setText(title);
        authorView.setText(author);
        publisherView.setText(publisher);
        priceView.setText(Integer.toString(price));
        isbnView.setText(isbn);
    }
}
