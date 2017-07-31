package com.yqq.commonUtils.http;

import android.text.TextUtils;

import com.yqq.commonUtils.Log.LogUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/7/18 0018.
 * post 网络请求工具类
 */

public class PostRequestUtils {

    private static  final String TAG="PostRequestUtils";

    private  static  final String mUrl= NetHostConfig.HOST_URL;
    public static final String SEND_MSG_ERROR = "SEND_MSG_ERROR";

    /**
     * 发送post请求  读写超时默认15秒，默认服务器
     * @param reqParams 请求参数
     * @param channel 地址
     * @return
     */
    public static String PostHttpRequest(String reqParams, String channel) {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(urlencode(reqParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SEND_MSG_ERROR;// 消息发送失败，返回错误，执行重发
        }
        // StringBuilder response = new StringBuilder();
        StringBuilder response = new StringBuilder();



        String serverUrl = mUrl + channel;

        String params = sb.toString();


        HttpRequest(serverUrl, params, response);
        LogUtils.e(TAG, "url address=" + mUrl + channel);
        String deUtfString = "";
        try {
            LogUtils.e(TAG,"后台返回的数据===="+response.toString());

            deUtfString = URLDecoder.decode(response.toString(), "utf-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SEND_MSG_ERROR;// 消息发送失败，返回错误，执行重发
        }





        return deUtfString;
    }




    /**
     * 发送post请求，服务器域名可配置  读写超时默认15秒，默认服务器
     * @param reqParams 请求参数
     * @param channel 地址
     * @param serverHost 服务器域名
     * @return
     */
    public static String PostHttpRequest(String reqParams, String channel,String serverHost) {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(urlencode(reqParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SEND_MSG_ERROR;// 消息发送失败，返回错误，执行重发
        }
        // StringBuilder response = new StringBuilder();
        StringBuilder response = new StringBuilder();



        String serverUrl = serverHost + channel;

        String params = sb.toString();


        HttpRequest(serverUrl, params, response);
        LogUtils.e(TAG, "url address=" + serverHost + channel);
        String deUtfString = "";
        try {
            LogUtils.e(TAG,"后台返回的数据===="+response.toString());

            deUtfString = URLDecoder.decode(response.toString(), "utf-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SEND_MSG_ERROR;// 消息发送失败，返回错误，执行重发
        }




        return deUtfString;
    }





    /**
     * 发送post请求，服务器域名可配置  读写时间可配置 读写超时默认15秒，默认服务器
     * @param reqParams 请求参数
     * @param channel 地址
     * @param serverHost 服务器域名  没有为null
     * @param  readTimeOut  读超时 没有为null
     * @param  connTimeOut  连接超时  没有为 null
     * @return
     */
    public static String PostHttpRequest(String reqParams, String channel,String serverHost, String readTimeOut,String connTimeOut) {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(urlencode(reqParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SEND_MSG_ERROR;// 消息发送失败，返回错误，执行重发
        }
        // StringBuilder response = new StringBuilder();
        StringBuilder response = new StringBuilder();



        String serverUrl = TextUtils.isEmpty(serverHost)?mUrl:serverHost + channel;

        String params = sb.toString();


        HttpRequest4SetTime(serverUrl, params,readTimeOut,connTimeOut, response);
        LogUtils.e(TAG, "url address=" + serverHost + channel);
        String deUtfString = "";
        try {
            LogUtils.e(TAG,"后台返回的数据===="+response.toString());

            deUtfString = URLDecoder.decode(response.toString(), "utf-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SEND_MSG_ERROR;// 消息发送失败，返回错误，执行重发
        }




        return deUtfString;
    }






















    /**
     *
     * @param url
     * @param
     * @param out
     * @return
     */
    private  static int HttpRequest(String url, String query, StringBuilder out) {


        URL urlobj;
        HttpURLConnection connection = null;
        DataOutputStream outStream=null;
        InputStream is=null;
        BufferedReader in=null;
        String result="";
        ByteArrayOutputStream outos=null;
        try {
            urlobj = new URL(url);
            connection = (HttpURLConnection) urlobj.openConnection();
            if (null != connection)
                connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection
                    .setRequestProperty("Content-Length", "" + query.length());

            connection.setRequestProperty("charset", "utf-8"); // encoding

            connection.setUseCaches(false);// Post can not using caches
            // //不要使用缓存,容易出现问题
            connection.setDoInput(true);
            connection.setDoOutput(true);// 如果通过post提交数据，必须设置允许对外输出数据

            connection.setConnectTimeout(NetHostConfig.HTTP_CONNECT_TIMEOUT);
            connection.setReadTimeout(NetHostConfig.HTTP_READ_TIMEOUT);
            outStream = new DataOutputStream(
                    connection.getOutputStream());
            outStream.writeBytes(query);
            outStream.flush();
            outStream.close();
            // 如果请求响应码是200，则表示成功
            int resPonseCode = connection.getResponseCode();
            LogUtils.e(TAG, "resPonseCode=" + String.valueOf(resPonseCode));

            is = connection.getInputStream();

//             in = new BufferedReader(new InputStreamReader(is));//
//            String line=null;
//            while ((line = in.readLine()) != null) {
//                result+=line;
//               // out.append(line);
//                //out.append('\r');
//            }
            outos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outos.write(buffer, 0, len);
                outos.flush();
            }

            result = outos.toString();


            out.append(result);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "HttpRequest Exception=" + e.toString());
            // out.append(SEND_MSG_ERROR);// 消息发送失败，返回错误，执行重发
            String utfString = "";
            try {
                utfString = urlencode(SEND_MSG_ERROR);
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            out.append(utfString);// 消息发送失败，返回错误，执行重发
        }finally {

//            if(null!=outStream){
//                try {
//                    outStream.close();
//                    outStream=null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            if(null!=is){
                try {
                    is.close();
                    is=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null!=in){
                try {
                    in.close();
                    in=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null!=outos){
                try {
                    outos.close();
                    outos=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null)
                connection.disconnect();
        }

        return 0;
    }


    /**
     * 设置请求超时
     * @param url 服务器地址
     * @param query 请求参数
     * @param readTimeOut 读超时
     * @param writeTimeOut 连接超时
     * @param out 返回结果
     * @return
     */
    private  static int HttpRequest4SetTime(String url, String query, String readTimeOut,String writeTimeOut,StringBuilder out) {


        // String query=query2;

        URL urlobj;
        HttpURLConnection connection = null;
        DataOutputStream outStream=null;
        InputStream is=null;
        BufferedReader in=null;
        String result="";
        ByteArrayOutputStream outos=null;
        try {
            urlobj = new URL(url);
            connection = (HttpURLConnection) urlobj.openConnection();
            if (null != connection)
                connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection
                    .setRequestProperty("Content-Length", "" + query.length());

            connection.setRequestProperty("charset", "utf-8"); // encoding

            connection.setUseCaches(false);// Post can not using caches
            // //不要使用缓存,容易出现问题
            connection.setDoInput(true);
            connection.setDoOutput(true);// 如果通过post提交数据，必须设置允许对外输出数据

            connection.setConnectTimeout(TextUtils.isEmpty(writeTimeOut)?NetHostConfig.HTTP_CONNECT_TIMEOUT:Integer.valueOf(NetHostConfig.HTTP_CONNECT_TIMEOUT));
            connection.setReadTimeout(TextUtils.isEmpty(readTimeOut)?NetHostConfig.HTTP_READ_TIMEOUT:Integer.valueOf(readTimeOut));
            outStream = new DataOutputStream(
                    connection.getOutputStream());
            outStream.writeBytes(query);
            outStream.flush();
            outStream.close();
            // 如果请求响应码是200，则表示成功
            int resPonseCode = connection.getResponseCode();
            LogUtils.e(TAG, "resPonseCode=" + String.valueOf(resPonseCode));

            is = connection.getInputStream();

//             in = new BufferedReader(new InputStreamReader(is));//
//            String line=null;
//            while ((line = in.readLine()) != null) {
//                result+=line;
//               // out.append(line);
//                //out.append('\r');
//            }
            outos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outos.write(buffer, 0, len);
                outos.flush();
            }

            result = outos.toString();


            out.append(result);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "HttpRequest Exception=" + e.toString());
            // out.append(SEND_MSG_ERROR);// 消息发送失败，返回错误，执行重发
            String utfString = "";
            try {
                utfString = urlencode(SEND_MSG_ERROR);
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            out.append(utfString);// 消息发送失败，返回错误，执行重发
        }finally {

//            if(null!=outStream){
//                try {
//                    outStream.close();
//                    outStream=null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            if(null!=is){
                try {
                    is.close();
                    is=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null!=in){
                try {
                    in.close();
                    in=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null!=outos){
                try {
                    outos.close();
                    outos=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null)
                connection.disconnect();
        }

        return 0;
    }




    /**
     * url编码方式
     *
     * @param str 指定编码方式，未指定默认为utf-8
     * @return
     * @throws UnsupportedEncodingException
     */
    private  static String urlencode(String str)
            throws UnsupportedEncodingException {
        String rc = URLEncoder.encode(str, "utf-8");
        return rc.replace("*", "%2A");
    }



}
