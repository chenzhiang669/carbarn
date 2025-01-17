package com.carbarn.im.translator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LanguageMap {

    private static final Map<String, String> LANGUAGE_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("zh", "中文(简体)");
        map.put("zh-Hant", "中文(繁体)");
        map.put("zh-Hant-hk", "中文(香港繁体)");
        map.put("zh-Hant-tw", "中文(台湾繁体)");
        map.put("tn", "札那语");
        map.put("vi", "越南语");
        map.put("iu", "伊努克提图特语");
        map.put("it", "意大利语");
        map.put("id", "印尼语");
        map.put("hi", "印地语");
        map.put("en", "英语");
        map.put("ho", "希里莫图语");
        map.put("he", "希伯来语");
        map.put("es", "西班牙语");
        map.put("el", "现代希腊语");
        map.put("uk", "乌克兰语");
        map.put("ur", "乌尔都语");
        map.put("tk", "土库曼语");
        map.put("tr", "土耳其语");
        map.put("ti", "提格里尼亚语");
        map.put("ty", "塔希提语");
        map.put("tl", "他加禄语");
        map.put("to", "汤加语");
        map.put("th", "泰语");
        map.put("ta", "泰米尔语");
        map.put("te", "泰卢固语");
        map.put("sl", "斯洛文尼亚语");
        map.put("sk", "斯洛伐克语");
        map.put("ss", "史瓦帝语");
        map.put("eo", "世界语");
        map.put("sm", "萨摩亚语");
        map.put("sg", "桑戈语");
        map.put("st", "塞索托语");
        map.put("sv", "瑞典语");
        map.put("ja", "日语");
        map.put("tw", "契维语");
        map.put("qu", "奇楚瓦语");
        map.put("pt", "葡萄牙语");
        map.put("pa", "旁遮普语");
        map.put("no", "挪威语");
        map.put("nb", "挪威布克莫尔语");
        map.put("nr", "南恩德贝勒语");
        map.put("my", "缅甸语");
        map.put("bn", "孟加拉语");
        map.put("mn", "蒙古语");
        map.put("mh", "马绍尔语");
        map.put("mk", "马其顿语");
        map.put("ml", "马拉亚拉姆语");
        map.put("mr", "马拉提语");
        map.put("ms", "马来语");
        map.put("lu", "卢巴卡丹加语");
        map.put("ro", "罗马尼亚语");
        map.put("lt", "立陶宛语");
        map.put("lv", "拉脱维亚语");
        map.put("lo", "老挝语");
        map.put("kj", "宽亚玛语");
        map.put("hr", "克罗地亚语");
        map.put("kn", "坎纳达语");
        map.put("ki", "基库尤语");
        map.put("cs", "捷克语");
        map.put("ca", "加泰隆语");
        map.put("nl", "荷兰语");
        map.put("ko", "韩语");
        map.put("ht", "海地克里奥尔语");
        map.put("gu", "古吉拉特语");
        map.put("ka", "格鲁吉亚语");
        map.put("kl", "格陵兰语");
        map.put("km", "高棉语");
        map.put("lg", "干达语");
        map.put("kg", "刚果语");
        map.put("fi", "芬兰语");
        map.put("fj", "斐济语");
        map.put("fr", "法语");
        map.put("ru", "俄语");
        map.put("ng", "恩敦加语");
        map.put("de", "德语");
        map.put("tt", "鞑靼语");
        map.put("da", "丹麦语");
        map.put("ts", "聪加语");
        map.put("cv", "楚瓦什语");
        map.put("fa", "波斯语");
        map.put("bs", "波斯尼亚语");
        map.put("pl", "波兰语");
        map.put("bi", "比斯拉玛语");
        map.put("nd", "北恩德贝勒语");
        map.put("ba", "巴什基尔语");
        map.put("bg", "保加利亚语");
        map.put("az", "阿塞拜疆语");
        map.put("ar", "阿拉伯语");
        map.put("af", "阿非利堪斯语");
        map.put("sq", "阿尔巴尼亚语");
        map.put("ab", "阿布哈兹语");
        map.put("os", "奥塞梯语");
        map.put("ee", "埃维语");
        map.put("et", "爱沙尼亚语");
        map.put("ay", "艾马拉语");
        map.put("lzh", "中文（文言文）");
        map.put("am", "阿姆哈拉语");
        map.put("ckb", "中库尔德语");
        map.put("cy", "威尔士语");
        map.put("gl", "加利西亚语");
        map.put("ha", "豪萨语");
        map.put("hy", "亚美尼亚语");
        map.put("ig", "伊博语");
        map.put("kmr", "北库尔德语");
        map.put("ln", "林加拉语");
        map.put("nso", "北索托语");
        map.put("ny", "齐切瓦语");
        map.put("om", "奥洛莫语");
        map.put("sn", "绍纳语");
        map.put("so", "索马里语");
        map.put("sr", "塞尔维亚语");
        map.put("sw", "斯瓦希里语");
        map.put("xh", "科萨语");
        map.put("yo", "约鲁巴语");
        map.put("zu", "祖鲁语");
        map.put("bo", "藏语");
        map.put("nan", "闽南语");
        map.put("wuu", "吴语");
        map.put("yue", "粤语");
        map.put("cmn", "西南官话");
        map.put("ug", "维吾尔语");
        map.put("fuv", "尼日利亚富拉语");
        map.put("hu", "匈牙利语");
        map.put("kam", "坎巴语");
        map.put("luo", "肯尼亚语");
        map.put("rw", "基尼阿万达语");
        map.put("umb", "卢欧语");
        map.put("wo", "沃洛夫语");

        LANGUAGE_MAP = Collections.unmodifiableMap(map);
    }

    public static String getLanguageName(String code) {
        return LANGUAGE_MAP.getOrDefault(code, "Unknown");
    }
}
