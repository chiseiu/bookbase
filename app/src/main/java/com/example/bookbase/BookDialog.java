package com.example.bookbase;

import android.app.AlertDialog;
import android.content.Context;

public class BookDialog {

    public static void showBookConfirmationDialog(Context context, Book book, Runnable onConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("書籍を追加しますか？");
        builder.setMessage("タイトル: " + book.getTitle() + "\n" +
                "著者: " + book.getAuthor() + "\n" +
                "出版社: " + book.getPublisher() + "\n" +
                "価格: " + book.getPrice() + "円\n" +
                "ISBN: " + book.getIsbn());
        builder.setPositiveButton("はい", (dialog, which) -> {
            if (onConfirm != null) {
                onConfirm.run();
            }
        });
        builder.setNegativeButton("いいえ", null);
        builder.show();
    }

    public static void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("エラー");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}

