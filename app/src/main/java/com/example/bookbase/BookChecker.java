package com.example.bookbase;

import android.content.Context;

import java.util.List;

public class BookChecker {

    public static boolean isBookInLibrary(Context context, String isbn) {
        // チェック：デフォルトの書籍リスト
        if (BookManager.isInDefaultBooks(context, isbn)) {
            return true;
        }

        // チェック：ユーザーの書籍ライブラリ
        List<Book> userLibrary = BookManager.loadUserLibrary(context);
        for (Book book : userLibrary) {
            if (book.getIsbn().equals(isbn)) {
                return true;
            }
        }

        return false;
    }
}
