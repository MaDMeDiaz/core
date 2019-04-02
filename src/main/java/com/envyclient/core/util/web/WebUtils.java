package com.envyclient.core.util.web;

import com.envyclient.core.util.GsonUtils;
import com.google.gson.JsonArray;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.envyclient.core.Envy.ThirdParty.EXECUTOR_SERVICE;

public class WebUtils {

    private static final String USER_AGENT = "Mozilla/5.0";

    private WebUtils() {
    }

    public static String get(String url) throws IOException {

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

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

    public static String post(String url, Map<String, String> requestMap, String body) throws IOException {

        HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        if (requestMap != null) {
            requestMap.forEach(con::setRequestProperty);
        }

        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();

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
                                                get("https://download.envyclient.com/latest/latest.html")
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