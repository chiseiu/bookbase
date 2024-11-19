package com.example.bookbase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OpenBDManager {

    private static final String API_URL = "https://api.openbd.jp/v1/get?isbn=";

    public static Book fetchBookFromOpenBD(String isbn) throws Exception {
        URL url = new URL(API_URL + isbn);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // ネットワークエラーチェック
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("ネットワークエラー: " + responseCode);
        }

        // JSONレスポンスを取得
        Scanner scanner = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        // JSONデータを解析
        JSONArray jsonArray = new JSONArray(response.toString());
        if (jsonArray.isNull(0)) {
            throw new Exception("書籍が見つかりません");
        }

        // summary フィールドを取得
        JSONObject summary = jsonArray.getJSONObject(0).getJSONObject("summary");

        // 各フィールドを抽出
        String title = summary.optString("title", "不明なタイトル");
        String rawAuthor = summary.optString("author", "不明な著者");
        String formattedAuthor = formatAuthor(rawAuthor); // 著者フォーマット処理
        String publisher = summary.optString("publisher", "不明な出版社");
        double price = 0.0;
        try {
            JSONObject onix = jsonArray.getJSONObject(0).getJSONObject("onix");
            if (onix != null) {
                JSONObject productSupply = onix.optJSONObject("ProductSupply");
                if (productSupply != null) {
                    JSONObject supplyDetail = productSupply.optJSONObject("SupplyDetail");
                    if (supplyDetail != null) {
                        JSONArray priceArray = supplyDetail.optJSONArray("Price");
                        if (priceArray != null && priceArray.length() > 0) {
                            JSONObject priceObject = priceArray.getJSONObject(0);
                            price = priceObject.optDouble("PriceAmount", 0.0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 価格が見つからない場合はログに記録
            System.err.println("価格情報の取得に失敗しました: " + e.getMessage());
        }

        // Bookオブジェクトを返却
        return new Book(title, formattedAuthor, publisher, price, isbn);
    }

    private static String formatAuthor(String rawAuthor) {
        // "稲盛,和夫,1932-2022" のような文字列をフォーマット
        String[] authors = rawAuthor.split("\\s+"); // スペースで分割
        StringBuilder formattedAuthors = new StringBuilder();

        for (String author : authors) {
            // 誕生年や死亡年（例: ",1932-2022"）を削除
            author = author.replaceAll(",\\d{4}(-\\d{4})?", ""); // ",1932-2022" を削除
            author = author.replaceAll("-", "");
            // 名前の間にスペースを追加
            author = author.replace(",", " ");

            // 著者をリストに追加（複数著者はカンマで区切る）
            if (formattedAuthors.length() > 0) {
                formattedAuthors.append(", ");
            }
            formattedAuthors.append(author);
        }

        return formattedAuthors.toString();
    }

}
