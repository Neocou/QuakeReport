package com.example.test.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * 工具类
 * 处理传递的值访问URL并对返回JSON数据进行处理
 */
public final class QueryUtils {
    private static  String SAMPLE_JSON_RESPONSE ;//JSON返回数据
    public  static  StringBuffer  USGS_REQUEST_URL = new StringBuffer("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson");//基址URL

    /**
     * 静态方法
     * 处理任务
     * @param params 开始时间 截止时间 地震等级
     * @return 处理后的JSON数据 ArrayList<Earthquake>
     * @throws IOException
     */
    public static ArrayList<Earthquake> extractEarthquakes(String ...params) throws IOException {
        //拼接字符串
        USGS_REQUEST_URL.append("&starttime="+params[0]);
        USGS_REQUEST_URL.append("&endtime="+params[1]);
        USGS_REQUEST_URL.append("&minmag="+params[2]);
        URL url = createUrl(USGS_REQUEST_URL.toString());
        //返回JSON数据
        SAMPLE_JSON_RESPONSE = makeHttpRequest(url);
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            //进行JSON数据处理
            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            for (int i =0;i<earthquakeArray.length();i++){
                JSONObject currentEarquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarquake.getJSONObject("properties");
                Double magnitude = properties.getDouble("mag");//地震等级
                String location = properties.getString("place");//地址
                long time = properties.getLong("time");//时间
                String urls = properties.getString("url");//具体URL
                Earthquake earthquake = new Earthquake(magnitude, location, time, urls);
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils","Problem",e);
        }
        return earthquakes;//最终处理后的数据

    }

    //进行访问URL处理，读取响应
    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);//输入流转为字符
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    /**
     * 输入流读取后返回字符
     * @param inputStream
     * @return
     */
    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toString();
    }

    /**
     * 字符串转URL
     * @param stringUrl
     * @return
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("EEE", "Error with creating URL", exception);
            return null;
        } 
        return url;
    }
}
