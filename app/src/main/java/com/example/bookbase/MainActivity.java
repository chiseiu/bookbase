package com.example.bookbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onSearchButtonClicked(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        EditText keywordText = findViewById(R.id.keywordText);
        String query = keywordText.getText().toString();
        if (query.isEmpty()) { // 空文字または空白のみの場合
            // トーストメッセージでユーザーに警告
            BookDialog.showErrorDialog(this, "キーワードを入力してください");
            return; // 画面遷移を中断
        }
        intent.putExtra("QUERY", query);
        startActivity(intent);
    }


    public void onAddBookButtonClicked(View view) {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    public void onAllbookButtonClicked(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("QUERY", "");
        startActivity(intent);
    }

}
