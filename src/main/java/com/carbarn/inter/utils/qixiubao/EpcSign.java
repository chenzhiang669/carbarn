package com.carbarn.inter.utils.qixiubao;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class EpcSign {
    private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


    public EpcSign() {
    }

    public String sign(Map<String, Object> params)
            throws Exception
    {
        String appid = (String)params.get("appid");
        if ((appid == null) || (appid.length() == 0)) {
            throw new IllegalArgumentException("appid不能为空");
        }
        String secret = (String)params.get("secret");
        if ((secret == null) || (secret.length() == 0)) {
            throw new IllegalArgumentException("secret不能为空");
        }
        String version = (String)params.get("version");
        if ((version == null) || (version.length() == 0)) {
            version = "1.0.0";
        }

        SortedMap<String, Object> parameters = new TreeMap();
        Set<Map.Entry<String, Object>> es = params.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ((v == null) || (v.length() == 0))
            {
                continue;
            }
            String ek = URLEncoder.encode(k, "UTF-8").replace("+", "%20");
            String ev = URLEncoder.encode(v, "UTF-8").replace("+", "%20");

            parameters.put(ek, ev);
        }

        String sresult = "";
        StringBuilder sb = new StringBuilder();
        es = parameters.entrySet();
        it = es.iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();

            sb.append(new StringBuilder().append(k).append("=").append(v).append("&").toString());
        }

        if (sb.length() > 0) {
            sresult = sb.substring(0, sb.length() - 1);
        }

        String sign = MD5Encode(sresult, "utf8");
        return sign.toUpperCase();
    }

    private String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if ((charsetname == null) || ("".equals(charsetname)))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception localException) {
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte[] b)
    {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return new StringBuilder().append(hexDigits[d1]).append(hexDigits[d2]).toString();
    }
}

