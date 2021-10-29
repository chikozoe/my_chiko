package com.chiko;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import jdk.internal.util.xml.PropertiesDefaultHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.*;
import java.util.Scanner;

public class ImgGo {
    //经度
    private static Double longitude;
    //纬度
    private static Double latitude;




    public static void main(String[] args) throws Exception, Exception {
        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            System.out.println("请输入图片的绝对地址地址到盘符");
            //获取键盘输入
            String Path = scanner.nextLine();
            //地址存入文件流
            File file = new File(Path);
            //地址图片传入方法
            printImageTags(file);


            System.out.println("请输入1/2");
            System.out.println("1代表继续查询/2代表结束查询");
            //输入参数
            String isOne = scanner.nextLine();
            if (isOne.equals("2")) {
                break;
            }

        }
    }

    private static void printImageTags(File file) throws ImageProcessingException, Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(file);

        for (Directory directory : metadata.getDirectories()) {

            for (Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息

                if (tagName.equals("Image Height")) {
                    System.out.println("图片高度: " + desc);

                } else if (tagName.equals("Image Width")) {
                    System.out.println("图片宽度: " + desc);

                } else if (tagName.equals("Date/Time Original")) {
                    System.out.println("拍摄时间: " + desc);

                } else if (tagName.equals("GPS Latitude")) {

                    latitude =pointToLatlong(desc);
                    System.err.println("纬度 : " + latitude);
                    // System.err.println("纬度(度分秒格式) : "+pointToLatlong(desc));

                } else if (tagName.equals("GPS Longitude")) {
                    longitude = pointToLatlong(desc);

                    System.err.println("经度: " + longitude);
                    //System.err.println("经度(度分秒格式): "+pointToLatlong(desc));

                }


            }
        }
        if (longitude != null || latitude != null) {
            getAddressByHttp(longitude, latitude);
        }
    }

    /**
     * 访问高德官网http
     *
     * @param longitude 经度
     * @param latitude  纬度
     *                  http://restapi.amap.com/v3/geocode/regeo?key=XXX&s=rsv3&language=zh_cn&location=116.39,39.9&extensions=all&callback=&platform=JS
     * @return
     */
    public static Address getAddressByHttp(double longitude, double latitude) throws IOException {
        Address address = new Address();
        address.setLon(longitude);
        address.setLat(latitude);
        String url = "http://restapi.amap.com/v3/geocode/regeo?key=6e089c5053d8e4897851eaa7d1e23f26&s=rsv3&language=zh_cn&extensions=all&callback=&platform=JS&location=" + longitude + "," + latitude;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            method.getStatusLine();
        }
        byte[] responseBody = method.getResponseBody();
        String responseStr = new String(responseBody, "UTF-8");
        JSONObject parse = (JSONObject) JSONObject.parse(responseStr);
        JSONObject regeocode = parse.getJSONObject("regeocode");//上次测试这里报了空指针
        //地址
        address.setAddress(regeocode.getString("formatted_address"));
        JSONArray roads = regeocode.getJSONArray("roads");
        if (roads.size() >= 2) {
            JSONObject road1 = (JSONObject) roads.get(0);
            JSONObject road2 = (JSONObject) roads.get(1);
            address.setRoads(road1.getString("name") + "-" + road2.getString("name"));
        }
        System.out.println(address);
        System.out.println(address.getAddress()+address.getRoads());
        return address;
    }

    public static double pointToLatlong(String point) {
        Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
        Double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("'")).trim());
        Double miao = Double.parseDouble(point.substring(point.indexOf("'") + 1, point.indexOf("\"")).trim());
        Double duStr = du + fen / 60 + miao / 60 / 60;
        return duStr;
    }

}

