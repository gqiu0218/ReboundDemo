package com.gqiu.materialdesgin;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 请求接口类
 * Created by gqiu on 2015/7/14.
 */
public class HttpEngine {
    public volatile static HttpEngine instance;
    private static AsyncHttpClient client;

    public static HttpEngine getInstance() {

        if (instance == null) {
            instance = new HttpEngine();
        }
        if (client == null) {
            client = new AsyncHttpClient(true, 80, 443);
            client.setTimeout(3000 * 10);
        }
        return instance;
    }


    public void getImageList(AsyncHttpResponseHandler handler) {
        String url = "http://image.baidu.com/data/imgs?col=%E7%BE%8E%E5%A5%B3&tag=%E5%B0%8F%E6%B8%85%E6%96%B0&sort=0&pn=10&rn=500&p=channel&from=1";

        client.get(url, handler);
    }


}
