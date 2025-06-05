package com.example.bookbase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onAddBookButtonClicked(View view) {

        EditText keywordText = findViewById(R.id.isbnAddText);
        String isbn = keywordText.getText().toString();

        if (isbn.isEmpty()) {
            BookDialog.showErrorDialog(this, "ISBN を入力してください");
            return;
        }

        // ISBN のバリデーション
        if (!BookManager.isValidIsbn(isbn)) {
            BookDialog.showErrorDialog(this, "無効なISBNです");
            return;
        }


        // 書籍がすでに存在するか確認
        if (BookManager.isBookInLibrary(this, isbn)) {
            // すでに存在する場合は何もせず終了
            BookDialog.showErrorDialog(this, "書籍がすでに存在します");
            return;
        }

        // 書籍情報を取得する
        new Thread(() -> {
            try {
                // 書籍情報を取得
                Book book = OpenBDManager.fetchBookFromOpenBD(isbn);

                // メインスレッドで確認ダイアログを表示
                runOnUiThread(() -> BookDialog.showBookConfirmationDialog(this, book, () -> {
                    // ユーザーが「はい」を選んだ場合に書籍を追加
                    BookManager.addBookToUserLibrary(this, book);
                    BookDialog.showSuccessDialog(this, "書籍を追加しました！");
                }));
            } catch (Exception e) {
                // エラーをキャッチしてダイアログで表示
                runOnUiThread(() -> BookDialog.showErrorDialog(this, "書籍情報の取得に失敗しました: " + e.getMessage()));
            }
        }).start();
    }
}