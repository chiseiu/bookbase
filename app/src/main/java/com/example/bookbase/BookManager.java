package com.example.bookbase;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

public class BookManager {

    private static final String USER_LIBRARY_FILE = "user_library.json";

    // ISBN が正しいかどうかをチェックする
    public static boolean isValidIsbn(String isbn) {
        return isbn != null && (isbn.length() == 9 || isbn.length() == 10 || isbn.length() == 13) && isbn.matches("\\d+"); // 10桁または13桁の数字
    }

    public static boolean isBookInLibrary(Context context, String isbn) {

        // チェック：ユーザーの書籍ライブラリ
        List<Book> userLibrary = BookManager.loadUserLibrary(context);
        for (Book book : userLibrary) {
            if (book.getIsbn().equals(isbn)) {
                return true;
            }
        }

        return false;
    }



    public static void addBookToUserLibrary(Context context, Book book) {
        List<Book> userLibrary = loadUserLibrary(context);
        if(!isBookInLibrary(context, book.isbn)){
            userLibrary.add(book);
            saveUserLibrary(context, userLibrary);
        }
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

            // 共通のJSON解析メソッドを利用
            books = parseJsonToBooks(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return books;
    }



    // JSON文字列をBookリストに変換する共通メソッド
    public static List<Book> parseJsonToBooks(String json) throws JSONException {
        List<Book> books = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Book book = new Book();
            book.setTitle(jsonObject.getString("TITLE"));
            book.setAuthor(jsonObject.getString("AUTHOR"));
            book.setPublisher(jsonObject.getString("PUBLISHER"));
            book.setPrice(jsonObject.getInt("PRICE"));
            book.setIsbn(jsonObject.getString("ISBN"));

            books.add(book);
        }

        return books;
    }


    public static List<Book> mergeAndRemoveDuplicates(List<Book> list1, List<Book> list2) {
        Set<Book> mergedSet = new HashSet<>(list1); // リスト1をSetに追加
        mergedSet.addAll(list2);                   // リスト2をSetに追加（重複自動削除）
        return new ArrayList<>(mergedSet);         // SetをListに変換して返す
    }

    public static List<Book> searchByTitleKeyword(List<Book> books, String keyword) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase())) // 大文字小文字を無視して検索
                .collect(Collectors.toList());
    }
}




