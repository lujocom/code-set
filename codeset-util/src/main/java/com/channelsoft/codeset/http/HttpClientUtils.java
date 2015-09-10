package com.channelsoft.codeset.http;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 *
 */
public class HttpClientUtils {

    public static final String UTF8 = "utf-8";
    public static final String GBK = "gbk";

    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static HttpClientBuilder httpBuilder = null;
    private static RequestConfig requestConfig = null;

    protected transient final static Log logger = LogFactory.getLog(HttpClientUtils.class);

    private static int MAX_CONNECTION = 10;

    private static int DEFAULT_MAX_CONNECTION = 5;

    static {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_CONNECTION);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTION);
        httpBuilder = HttpClients.custom();
        httpBuilder.setConnectionManager(connectionManager);
    }

    private static CloseableHttpClient getConnection() {
        return httpBuilder.build();
    }


    private static HttpUriRequest getRequest(Map<String, String> map, String URI, String method) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            String name = e.getKey();
            String value = e.getValue();
            NameValuePair pair = new BasicNameValuePair(name, value);
            params.add(pair);
        }
        //设置http的状态参数
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        return RequestBuilder.create(method).setUri(URI)
                .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                .setConfig(requestConfig).build();

    }

    private static HttpUriRequest getRequest(String uri, String method,
                                             HttpEntity entity, Map<String, String> parameters) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> entrySet = parameters.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            String name = e.getKey();
            String value = e.getValue();
            NameValuePair pair = new BasicNameValuePair(name, value);
            params.add(pair);
        }

        requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000000)
                .setConnectTimeout(5000000)
                .setConnectionRequestTimeout(5000000)
                .build();

        return RequestBuilder.create(method).setUri(uri).setEntity(entity).
                addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                .setConfig(requestConfig).build();
    }

    public static HttpResponse getResponse(String url, Map<String,
            String> parameter, String method) throws IOException {
        return getConnection().execute(getRequest(parameter, url, method));
    }


    /**
     * 获取返回结果的JSONObject对象
     *
     * @param url       请求连接
     * @param parameter 请求参数
     * @param method    请求方法，POST、GET等
     * @return 请求结果
     */
    public static JSONObject getJSONObject(String url, Map<String, String> parameter, String method) {
        return getJSONObject(url, parameter, method, HttpClientUtils.UTF8);
    }

    /**
     * 获取返回结果的JSONObject对象
     *
     * @param url               请求连接
     * @param parameter         请求参数
     * @param method            请求方法，POST、GET等
     * @param characterEncoding 字符编码
     * @return 请求结果
     */
    public static JSONObject getJSONObject(String url, Map<String, String> parameter,
                                           String method, String characterEncoding) {
        JSONObject jsonObject = new JSONObject();
        try {
            HttpResponse response = getConnection().execute(getRequest(parameter, url, method));
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String message = EntityUtils.toString(entity, characterEncoding);
                logger.debug("getJSONObject ---> message : " + message);
                return JSONObject.fromObject(message);
            } else {
                jsonObject.put("returnCode", "500");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put("returnCode", "500");
            jsonObject.put("returnMsg", e.getMessage());
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 构造文件实体
     * @param entityPartName 参数名称
     * @param file 文件
     * @return httpEntity
     */

    private static HttpEntity buildMultipartEntity(String entityPartName, File file) {
        FileBody fileBody = new FileBody(file);
        return MultipartEntityBuilder.create().addPart(entityPartName, fileBody).build();
    }

    /**
     * 上传文件
     *
     * @param url            上传文件完整链接
     * @param entityPartName entityPartName
     * @param file           文件
     * @param parameters     附带参数
     * @return HttpResponse
     */
    public static HttpResponse getMultipartResponse(String url, String entityPartName,
                                                    File file, Map<String, String> parameters) {
        try {

            HttpUriRequest httpUriRequest = getRequest(url, HttpPost.METHOD_NAME,
                    buildMultipartEntity(entityPartName, file), parameters);

            return getConnection().execute(httpUriRequest);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 上传文件
     *
     * @param url            上传文件完整链接
     * @param entityPartName entityPartName
     * @param file           文件
     * @param parameters     附带参数
     * @return HttpResponse
     */
    public static JSONObject getMultipartResponse2JsonObject(String url, String entityPartName, File file,
                                                             Map<String, String> parameters) {
        return getMultipartResponse2JsonObject(url, entityPartName, file, parameters, HttpClientUtils.UTF8);

    }

    /**
     * 上传文件
     *
     * @param url               上传文件完整链接
     * @param entityPartName    entityPartName
     * @param file              文件
     * @param parameters        附带参数
     * @param characterEncoding 返回实体字符编码
     * @return HttpResponse
     */
    public static JSONObject getMultipartResponse2JsonObject(
            String url, String entityPartName, File file,Map<String, String> parameters,
            String characterEncoding) {

        JSONObject jsonObject = new JSONObject();
        HttpResponse httpResponse = getMultipartResponse(url, entityPartName, file, parameters);

        if (!ObjectUtils.notEqual(httpResponse, null)) {
            jsonObject.put("returnCode", "500");
            jsonObject.put("returnMsg", "上传文件接口出错");
            return jsonObject;
        }

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = httpResponse.getEntity();
            try {
                return JSONObject.fromObject(EntityUtils.toString(entity, characterEncoding));
            } catch (IOException e) {
                e.printStackTrace();
                jsonObject.put("returnCode", "500");
                jsonObject.put("returnMsg", e.getMessage());
                return jsonObject;
            }
        } else {
            jsonObject.put("returnCode", "500");
            jsonObject.put("returnMsg", "上传文件接口出错");
            return jsonObject;
        }

    }


    /**
     * 测试方法 http请求
     *
     * @param fileName
     * @return
     */
    @Deprecated
    public String getUploadToken(String fileName){
        Map<String, String> map = new HashMap<String, String>();
        map.put("fileSpaceName", "ent-resource");
        map.put("targetFileName", fileName);
        map.put("isPersistentOps", "true");
        map.put("entId", "1287263672");
        map.put("expires", "3600");
        map.put("isStrict", "true");
        map.put("resType", "img");

        JSONObject jsonObject = HttpClientUtils.getJSONObject("http://localhost:8899/upload/getToken",
                map, HttpPost.METHOD_NAME);
        logger.debug("get token : " + jsonObject.toString());
        if(jsonObject.containsKey("returnCode")
                && StringUtils.equals(jsonObject.getString("returnCode"), "20000")){
            return JSONObject.fromObject(jsonObject.get("data")).getString("token");
        }
        return "";
    }

    /**
     * 测试文件上传
     * @param file
     * @param token
     */
    @Deprecated
    public void uploadFile(File file, String token){

        Map<String, String> map = new HashMap<String, String>();
        map.put("isUploadToQiNiu", "true");
        map.put("logo", "");
        map.put("resType", "img");
        map.put("entId", "1287263672");
        map.put("token", token);

        JSONObject jsonObject = HttpClientUtils.getMultipartResponse2JsonObject(
                "http://localhost:8899/upload/uploadFile", "file", file, map, HttpClientUtils.GBK);
        logger.debug("uploadFile : "+jsonObject.toString());

    }


    /*public static void main(String args[]) throws IOException {
        //测试
        Map<String, String> map = new HashMap<String, String>();
        map.put("fileSpaceName", "ent-resource");
        map.put("targetFileName", "test.jsp");
        map.put("isPersistentOps", "true");
        map.put("entId", "1287263672");
        map.put("expires", "3600");
        map.put("isStrict", "true");

        HttpResponse response = null;
        try {
            response = HttpClientUtils.getResponse("http://localhost:8899/upload/getToken",
                    map, HttpPost.METHOD_NAME);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String message = EntityUtils.toString(entity, "utf-8");

                JSONObject jsonObject = JSONObject.fromObject(message);
                System.out.println(jsonObject.get("returnCode"));
            } else {
                System.out.println("请求失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
