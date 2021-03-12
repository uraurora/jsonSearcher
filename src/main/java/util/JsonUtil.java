package util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author : gaoxiaodong04
 * @program : jsonSearcher
 * @date : 2021-03-12 18:18
 * @description :
 */
public abstract class JsonUtil {

    public static boolean isJson(String str){
        try {
            final JsonElement parse = JsonParser.parseString(str);
            return !parse.isJsonNull() && (parse.isJsonObject() || parse.isJsonArray());
        } catch (Exception e) {
            return false;
        }
    }

}
