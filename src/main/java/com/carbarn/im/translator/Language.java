package com.carbarn.im.translator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zoulingxi
 * @description 语言数据
 * @date 2025/1/13 23:28
 */
@Data
public class Language {

    private final String code;
    private final String name;
    private final String englishName;
    private final String remarks;

    public Language(String code, String name, String englishName, String remarks) {
        this.code = code;
        this.name = name;
        this.englishName = englishName;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Language{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", englishName='" + englishName + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }


    public static List<Language> getLanguages() {
        List<Language> languages = new ArrayList<>();
        languages.add(new Language("zh", "中文(简体)", "Chinese (simplified)", ""));
        languages.add(new Language("zh-Hant", "中文(繁体)", "Chinese (traditional)", ""));
        languages.add(new Language("zh-Hant-hk", "中文(香港繁体)", "Chinese (Hongkong traditional)", ""));
        languages.add(new Language("zh-Hant-tw", "中文(台湾繁体)", "Chinese (Taiwan traditional)", ""));
        languages.add(new Language("tn", "札那语", "Tswana", ""));
        languages.add(new Language("vi", "越南语", "Vietnamese", ""));
        languages.add(new Language("iu", "伊努克提图特语", "Inuktitut", ""));
        languages.add(new Language("it", "意大利语", "Italian", ""));
        languages.add(new Language("id", "印尼语", "Indonesian", ""));
        languages.add(new Language("hi", "印地语", "Hindi", ""));
        languages.add(new Language("en", "英语", "English", ""));
        languages.add(new Language("ho", "希里莫图语", "Hiri", ""));
        languages.add(new Language("he", "希伯来语", "Hebrew", ""));
        languages.add(new Language("es", "西班牙语", "Spanish", ""));
        languages.add(new Language("el", "现代希腊语", "Modern", ""));
        languages.add(new Language("uk", "乌克兰语", "Ukrainian", ""));
        languages.add(new Language("ur", "乌尔都语", "Urdu", ""));
        languages.add(new Language("tk", "土库曼语", "Turkmen", ""));
        languages.add(new Language("tr", "土耳其语", "Turkish", ""));
        languages.add(new Language("ti", "提格里尼亚语", "Tigrinya", ""));
        languages.add(new Language("ty", "塔希提语", "Tahitian", ""));
        languages.add(new Language("tl", "他加禄语", "Tagalog", ""));
        languages.add(new Language("to", "汤加语", "Tongan", ""));
        languages.add(new Language("th", "泰语", "Thai", ""));
        languages.add(new Language("ta", "泰米尔语", "Tamil", ""));
        languages.add(new Language("te", "泰卢固语", "Telugu", ""));
        languages.add(new Language("sl", "斯洛文尼亚语", "Slovenian", ""));
        languages.add(new Language("sk", "斯洛伐克语", "Slovak", "只支持 其它语种 翻 斯洛伐克语"));
        languages.add(new Language("ss", "史瓦帝语", "Swati", ""));
        languages.add(new Language("eo", "世界语", "Esperanto", ""));
        languages.add(new Language("sm", "萨摩亚语", "Samoan", ""));
        languages.add(new Language("sg", "桑戈语", "Sango", ""));
        languages.add(new Language("st", "塞索托语", "Southern", ""));
        languages.add(new Language("sv", "瑞典语", "Swedish", ""));
        languages.add(new Language("ja", "日语", "Japanese", ""));
        languages.add(new Language("tw", "契维语", "Twi", ""));
        languages.add(new Language("qu", "奇楚瓦语", "Quechua", ""));
        languages.add(new Language("pt", "葡萄牙语", "Portuguese", ""));
        languages.add(new Language("pa", "旁遮普语", "Punjabi", ""));
        languages.add(new Language("no", "挪威语", "Norwegian", ""));
        languages.add(new Language("nb", "挪威布克莫尔语", "Norwegian", ""));
        languages.add(new Language("nr", "南恩德贝勒语", "", ""));
        languages.add(new Language("my", "缅甸语", "Burmese", ""));
        languages.add(new Language("bn", "孟加拉语", "Bengali", ""));
        languages.add(new Language("mn", "蒙古语", "Mongolian", ""));
        languages.add(new Language("mh", "马绍尔语", "Marshallese", ""));
        languages.add(new Language("mk", "马其顿语", "Macedonian", ""));
        languages.add(new Language("ml", "马拉亚拉姆语", "Malayalam", ""));
        languages.add(new Language("mr", "马拉提语", "Marathi", ""));
        languages.add(new Language("ms", "马来语", "Malay", ""));
        languages.add(new Language("lu", "卢巴卡丹加语", "Luba-Katanga", ""));
        languages.add(new Language("ro", "罗马尼亚语", "Romanian", ""));
        languages.add(new Language("lt", "立陶宛语", "Lithuanian", ""));
        languages.add(new Language("lv", "拉脱维亚语", "Latvian", ""));
        languages.add(new Language("lo", "老挝语", "Lao", ""));
        languages.add(new Language("kj", "宽亚玛语", "Kwanyama", ""));
        languages.add(new Language("hr", "克罗地亚语", "Croatian", ""));
        languages.add(new Language("kn", "坎纳达语", "Kannada", ""));
        languages.add(new Language("ki", "基库尤语", "Kikuyu", ""));
        languages.add(new Language("cs", "捷克语", "Czech", ""));
        languages.add(new Language("ca", "加泰隆语", "Catalan", ""));
        languages.add(new Language("nl", "荷兰语", "Dutch", ""));
        languages.add(new Language("ko", "韩语", "Korean", ""));
        languages.add(new Language("ht", "海地克里奥尔语", "Haitian", ""));
        languages.add(new Language("gu", "古吉拉特语", "Gujarati", ""));
        languages.add(new Language("ka", "格鲁吉亚语", "Georgian", ""));
        languages.add(new Language("kl", "格陵兰语", "Greenlandic", ""));
        languages.add(new Language("km", "高棉语", "Khmer", ""));
        languages.add(new Language("lg", "干达语", "Ganda", ""));
        languages.add(new Language("kg", "刚果语", "Kongo", ""));
        languages.add(new Language("fi", "芬兰语", "Finnish", ""));
        languages.add(new Language("fj", "斐济语", "Fijian", ""));
        languages.add(new Language("fr", "法语", "French", ""));
        languages.add(new Language("ru", "俄语", "Russian", ""));
        languages.add(new Language("ng", "恩敦加语", "Ndonga", ""));
        languages.add(new Language("de", "德语", "German", ""));
        languages.add(new Language("tt", "鞑靼语", "Tatar", ""));
        languages.add(new Language("da", "丹麦语", "Danish", ""));
        languages.add(new Language("ts", "聪加语", "Tsonga", ""));
        languages.add(new Language("cv", "楚瓦什语", "Chuvash", ""));
        languages.add(new Language("fa", "波斯语", "Persian", ""));
        languages.add(new Language("bs", "波斯尼亚语", "Bosnian", ""));
        languages.add(new Language("pl", "波兰语", "Polish", ""));
        languages.add(new Language("bi", "比斯拉玛语", "Bislama", ""));
        languages.add(new Language("nd", "北恩德贝勒语", "North Ndebele", ""));
        languages.add(new Language("ba", "巴什基尔语", "Bashkir", ""));
        languages.add(new Language("bg", "保加利亚语", "Bulgarian", ""));
        languages.add(new Language("az", "阿塞拜疆语", "Azerbaijani", ""));
        languages.add(new Language("ar", "阿拉伯语", "Arabic", ""));
        languages.add(new Language("ar", "阿拉伯语", "Arabic", ""));
        languages.add(new Language("af", "阿非利堪斯语", "Afrikaans", ""));
        languages.add(new Language("sq", "阿尔巴尼亚语", "Albanian", ""));
        languages.add(new Language("ab", "阿布哈兹语", "Abkhazian", ""));
        languages.add(new Language("os", "奥塞梯语", "Ossetian", ""));
        languages.add(new Language("ee", "埃维语", "Ewe", ""));
        languages.add(new Language("et", "爱沙尼亚语", "Estonian", ""));
        languages.add(new Language("ay", "艾马拉语", "Aymara", ""));
        languages.add(new Language("lzh", "中文（文言文）", "Chinese (classical)", ""));
        languages.add(new Language("am", "阿姆哈拉语", "Amharic", ""));
        languages.add(new Language("ckb", "中库尔德语", "Central Kurdish", ""));
        languages.add(new Language("cy", "威尔士语", "Welsh", ""));
        languages.add(new Language("gl", "加利西亚语", "Galician", ""));
        languages.add(new Language("ha", "豪萨语", "Hausa", ""));
        languages.add(new Language("hy", "亚美尼亚语", "Armenian", ""));
        languages.add(new Language("ig", "伊博语", "Igbo", ""));
        languages.add(new Language("kmr", "北库尔德语", "Northern Kurdish", ""));
        languages.add(new Language("ln", "林加拉语", "Lingala", ""));
        languages.add(new Language("nso", "北索托语", "Northern Sotho", ""));
        languages.add(new Language("ny", "齐切瓦语", "Chewa", ""));
        languages.add(new Language("om", "奥洛莫语", "Oromo", ""));
        languages.add(new Language("sn", "绍纳语", "Shona", ""));
        languages.add(new Language("so", "索马里语", "Somali", ""));
        languages.add(new Language("sr", "塞尔维亚语", "Serbian", ""));
        languages.add(new Language("sw", "斯瓦希里语", "Swahili", ""));
        languages.add(new Language("xh", "科萨语", "Xhosa", ""));
        languages.add(new Language("yo", "约鲁巴语", "Yoruba", ""));
        languages.add(new Language("zu", "祖鲁语", "Zulu", ""));
        languages.add(new Language("bo", "藏语", "Tibetan", ""));
        languages.add(new Language("nan", "闽南语", "Hokkien", ""));
        languages.add(new Language("wuu", "吴语", "Wuyue Chinese", ""));
        languages.add(new Language("yue", "粤语", "Cantonese", ""));
        languages.add(new Language("cmn", "西南官话", "Southwestern Mandarin", ""));
        languages.add(new Language("ug", "维吾尔语", "Uighur", ""));
        languages.add(new Language("fuv", "尼日利亚富拉语", "Nigerian Fulfulde", ""));
        languages.add(new Language("hu", "匈牙利语", "Hungarian", ""));
        languages.add(new Language("kam", "坎巴语", "Kamba", ""));
        languages.add(new Language("luo", "肯尼亚语", "Dholuo", ""));
        languages.add(new Language("rw", "基尼阿万达语", "Kinyarwanda", ""));
        languages.add(new Language("umb", "卢欧语", "Umbundu", ""));
        languages.add(new Language("wo", "沃洛夫语", "Wolof", ""));
        return languages;
    }
}

