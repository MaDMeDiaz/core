package com.envyclient.core.util.web;

import com.envyclient.core.util.GsonUtils;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.envyclient.core.Envy.ThirdParty.EXECUTOR_SERVICE;

public class WebUtils {

    private WebUtils() {
    }

    public static String get(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        con.disconnect();
        in.close();

        return response.toString();
    }

    public static void getLatestVersion(Callback<Double> callback) {
        EXECUTOR_SERVICE.submit(() -> {
            try {
                callback.onSuccess
                        (
                                Double.parseDouble
                                        (
                                                get("https://download.envyclient.com/latest/latest.php")
                                        )
                        );
            } catch (IOException | NumberFormatException e) {
                callback.onFail(e);
            }
        });
    }

    public static void getIssues(Callback<JsonArray> callback) {
        EXECUTOR_SERVICE.submit(() -> {
            try {
                callback.onSuccess
                        (
                                GsonUtils.toJsonArray
                                        (
                                                get("https://api.github.com/repos/envyclient/issues/issues")
                                        )
                        );
            } catch (IOException | NumberFormatException e) {
                callback.onFail(e);
            }
        });
    }

}