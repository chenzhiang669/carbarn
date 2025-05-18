package com.carbarn.inter.utils.qixiubao.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.test.Translator1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static List<String> datas = new ArrayList<String>();
    static {
        datas.add("unknown");
        datas.add("unknown");
        datas.add("unknown");
        datas.add("unknown");
        datas.add("unknown");
        datas.add("unknown");
        datas.add("unknown");
        datas.add("unknown");
        datas.add("高保值");
        datas.add("代步车");
        datas.add("准新车");
        datas.add("全车原漆");
        datas.add("0过户");
        datas.add("低油耗");
        datas.add("高性价比");
        datas.add("全车原版");
        datas.add("车况精品");
        datas.add("一手车");
        datas.add("实表里程");
        datas.add("平行进口车");
        datas.add("营转非");
        datas.add("事故车");
        datas.add("瑕疵车");
        datas.add("泡水车");
        datas.add("火烧车");
        datas.add("非事故车");
        datas.add("碰撞事故");
        datas.add("全车原漆");
        datas.add("划痕");
        datas.add("凹痕");
        datas.add("原版");
        datas.add("受损");
        datas.add("换件");
        datas.add("正常");
        datas.add("异常");
        datas.add("拆卸");
        datas.add("正常");
        datas.add("异常");
        datas.add("拆卸");
        datas.add("一手车");
        datas.add("一次过户");
        datas.add("多次过户");
        datas.add("实表");
        datas.add("调表");
        datas.add("表显");
        datas.add("unknown");
        datas.add("泡水车");
        datas.add("unknown");
        datas.add("国二");
        datas.add("欧五");
        datas.add("欧六");
    }
    public static void main(String[] args) throws IOException {

        LocalRunForbidenLog4j.forbidenlog();
        Translator1 translator1 = new Translator1();
        datas.forEach(x -> {
            String result = translator1.translate(x, "zh", "ru");
            System.out.println(result);

        });
    }

}
