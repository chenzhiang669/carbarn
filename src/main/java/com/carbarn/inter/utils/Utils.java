package com.carbarn.inter.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String regex = "^[a-zA-Z0-9_]+$";

    public static int baseYear = 2001;  //当前一轮的出厂日期从2001年开始计算

    public static String baseYearCode = "123456789ABCDEFGHJKLMNPRSTVWXY";  //车架的出厂日期标识符号中没有I,O,Q,U,Z

    public static String getRandomChar(int length){
        String randomchar = RandomStringUtils.randomAlphanumeric(length);
        return randomchar;
    }

    public static long getRandomLong(){
        long randomLong = ThreadLocalRandom.current().nextLong(1000L, 10001L);
        return randomLong;
    }

    public static String getRandomDate() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.now();

        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        Random random = new Random();
        long randomEpochDay = startEpochDay + (long) (random.nextDouble() * (endEpochDay - startEpochDay + 1));
        LocalDate randomDate =  LocalDate.ofEpochDay(randomEpochDay);

        // 打印结果
        return randomDate.toString();
    }

    //通过车架号解析出厂年份
    public static String getYearFromVin(String vin){
        String yearCode = vin.substring(9,10);
        int index = baseYearCode.indexOf(yearCode);
        if(index == -1){
            return Utils.getRandomDate();
        }else{
            int year = baseYear + index;
            String manufacture_date = Utils.getRandomDate().substring(4);
            String manufacture_year = "" + year + manufacture_date;
            return manufacture_year;
        }
    }


    public static String getFirstLetter(String chinese) {
        if(chinese.matches(regex)){
            return chinese.substring(0,1).toUpperCase();
        }

        StringBuilder pinyinName = new StringBuilder();
        char character = chinese.charAt(0);
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String[] pinyinArray = null;
        try {
            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(character, format);
            return pinyinArray[0].substring(0,1);
        } catch (Exception e) {
            return "V";
        }
    }

    public static void main(String[] args) {
        long year = Utils.getRandomLong();
        System.out.println(year);

    }
}
