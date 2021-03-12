package parser.impl;

import com.google.gson.JsonElement;
import lombok.Data;
import parser.Pair;

/**
 * @author : gaoxiaodong04
 * @program : jsonSearcher
 * @date : 2021-03-12 17:18
 * @description :
 */
@Data(staticConstructor = "of")
public class JsonPair implements Pair<String, String> {

    private final String key;

    private final String value;

    @Override
    public String toString() {
        return "{" + key +
                ": " + value +
                "}";
    }

}
