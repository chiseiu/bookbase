package com.example.bookbase;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BookManager {

    private static final String USER_LIBRARY_FILE = "user_library.json";

    // 1. ISBN が正しいかどうかをチェックする（例として、ISBNが10または13桁かどうかを確認）
    public static boolean isValidIsbn(String isbn) {
        return isbn != null && (isbn.length() == 9 || isbn.length() == 10 || isbn.length() == 13) && isbn.matches("\\d+"); // 10桁または13桁の数字
    }

    // 2. default_book.json に ISBN が存在するかどうか確認する
    public static boolean isInDefaultBooks(Context context, String isbn) {
        try {
            String json = loadJSONFromAsset(context, "default_book.json");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("ISBN").equals(isbn)) {
                    return true;
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void addBookToUserLibrary(Context context, Book book) {
        List<Book> userLibrary = loadUserLibrary(context);
        userLibrary.add(book);
        saveUserLibrary(context, userLibrary);
    }

    // ユーザーライブラリをファイルに保存する
    private static void saveUserLibrary(Context context, List<Book> books) {
        JSONArray jsonArray = new JSONArray();
        for (Book book : books) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("TITLE", book.getTitle());
                obj.put("AUTHOR", book.getAuthor());
                obj.put("PUBLISHER", book.getPublisher());
                obj.put("PRICE", book.getPrice());
                obj.put("ISBN", book.getIsbn());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fos = context.openFileOutput(USER_LIBRARY_FILE, Context.MODE_PRIVATE)) {
            fos.write(jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ユーザーライブラリを読み込む
    public static List<Book> loadUserLibrary(Context context) {
        List<Book> books = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(USER_LIBRARY_FILE)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                books.add(new Book(
                        obj.getString("TITLE"),
                        obj.getString("AUTHOR"),
                        obj.getString("PUBLISHER"),
                        obj.getDouble("PRICE"),
                        obj.getString("ISBN")
                ));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return books;
    }


    // アセットフォルダからJSONを読み込む
    private static String loadJSONFromAsset(Context context, String fileName) throws IOException {
        try (FileInputStream is = context.openFileInput(fileName)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return new String(buffer, "UTF-8");
        }
    }
}
