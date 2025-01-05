package com.carbarn.inter.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.http.JinyutangHttp;
import com.carbarn.inter.utils.qiniuyun.QiniuyunUtils;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;

public class Test {
    public static void main(String[] args) throws IOException {
        downloadImage();

    }


    public static void downloadImage() {
        try {

            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\brand-utf8.csv")));
            BufferedWriter wr = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\brand-utf8-qinniuyun.csv")));
            String line = null;
            while ((line = br.readLine()) != null){
                String[] infos = line.split(",");
                String brand_id = infos[0];
                String brand = infos[1];
                String first_char = infos[2];
                String url_ = infos[3];
                String language = infos[4];
                String[] url_infos = url_.split("\\.");
                String end_prefix = url_infos[url_infos.length - 1];
                String filename = Utils.getRandomChar(30) + "." + end_prefix;

                URL url = new URL(url_);
//                String savePath = "D:\\carbarn\\brand_pictures\\" + brand + "-" + filename + "." + end_prefix;


//                FileOutputStream fos = new FileOutputStream(savePath);
                InputStream is = new BufferedInputStream(url.openStream());

//                byte[] data = new byte[1024];
//                int count;
//                while ((count = is.read(data, 0, 1024)) != -1) {
//                    fos.write(data, 0, count);
//                }

                byte[] datas = inputStreamToByteArray(is);
                String logo_url = QiniuyunUtils.uploadFile(datas, "carbarn/brand_logo/" + filename);


                wr.write(brand_id + "," + brand + "," + first_char + "," + logo_url + "," + language + "\n");
//                fos.close();
//                is.close();
//
//                System.out.println("图片下载成功，保存路径：" + savePath);
            }

            wr.flush();
            wr.close();


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("图片下载失败：" + e.getMessage());
        }
    }


    public static byte[] inputStreamToByteArray(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toByteArray();
    }


    public static void series() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\series.csv")));
        BufferedWriter wr = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\series-utf8.csv")));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] infos = line.split(",", 2);
            String brand_id = infos[0];

            JSONArray serieses = JSON.parseArray(infos[1]);

            for (int i = 0; i < serieses.size(); i++) {
                JSONObject json = serieses.getJSONObject(i);

                String series_id = json.getString("id");
                String series = json.getString("name");
                String first_char = json.getString("frist_letter");
                wr.write(brand_id + "," + series_id + "," + series + "," + first_char + ",zh" + "\n");
                System.out.println(brand_id + "\t\t" + series_id + "\t\t" + series + "\t\t" + first_char);
            }
        }

        wr.flush();
        wr.close();

    }


    public static void type() throws IOException {

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\model.csv")));
        BufferedWriter wr = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\model-utf8.csv")));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] infos = line.split(",", 2);
            String series_id = infos[0];
            JSONObject serieses = null;
            try {
                serieses = JSON.parseObject(infos[1]);
            } catch (Exception e) {
                System.out.println(infos[1]);
                continue;
            }

            for (String year : serieses.keySet()) {
                JSONArray datas = serieses.getJSONArray(year);

                for (int i = 0; i < datas.size(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    String type_id = data.getString("id");
                    String type = data.getString("name");
                    double guide_price = data.getDouble("guiding_price") * 10000;
                    String formatted = decimalFormat.format(guide_price);
                    guide_price = Double.parseDouble(formatted);

                    wr.write(series_id + "," + year + "," + type_id + "," + type + "," + guide_price + ",zh" +"\n");
                    System.out.println(series_id + "\t" + year + "\t" + type_id + "\t" + type + "\t" + guide_price);
                }
            }

        }

        wr.flush();
        wr.close();


    }
}
