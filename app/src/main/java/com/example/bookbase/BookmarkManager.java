package com.example.bookbase;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {

    private static final String BOOKMARK_FILE = "bookmark.json";

    // 1. bookmark.json ファイルが存在するかチェックし、初期化する
    public static void initializeBookmarksFile(Context context) {
        File file = new File(context.getFilesDir(), BOOKMARK_FILE);
        if (!file.exists()) {
            try (FileOutputStream fos = context.openFileOutput(BOOKMARK_FILE, Context.MODE_PRIVATE)) {
                // 空の JSON 配列を作成してファイルに書き込む
                fos.write("[]".getBytes()); // 空配列はまだお気に入りの本がないことを意味する
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 2. お気に入りの本のリストを読み込む
    public static List<Book> loadBookmarks(Context context) {
        initializeBookmarksFile(context); // 読み込みの際にファイルが初期化されていることを確認
        List<Book> bookmarks = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(BOOKMARK_FILE)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Book book = new Book(
                        jsonObject.getString("TITLE"),
                        jsonObject.getString("AUTHOR"),
                        jsonObject.getString("PUBLISHER"),
                        jsonObject.getInt("PRICE"),
                        jsonObject.getString("ISBN")
                );
                bookmarks.add(book);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return bookmarks;
    }

    // 3. お気に入りの本のリストを保存する
    public static void saveBookmarks(Context context, List<Book> bookmarks) {
        JSONArray jsonArray = new JSONArray();
        for (Book book : bookmarks) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("isbn", book.getIsbn());
                jsonObject.put("title", book.getTitle());
                jsonObject.put("author", book.getAuthor());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fos = context.openFileOutput(BOOKMARK_FILE, Context.MODE_PRIVATE)) {
            fos.write(jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
