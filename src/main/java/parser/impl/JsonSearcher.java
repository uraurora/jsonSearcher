package parser.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Data;
import parser.Pair;

import java.util.Collection;

/**
 * @author : gaoxiaodong04
 * @program : jsonSearcher
 * @date : 2021-03-12 17:37
 * @description :
 */
public class JsonSearcher {

    private String json;

    private boolean needPrefix;

    public JsonSearcher(String json, boolean needPrefix){
        this.json = json;
        this.needPrefix = needPrefix;
    }

    public void dfs(){

    }

    public void bfs(){

    }

    protected static void dfsHelper(InternalPair pair,
                                    boolean needPrefix,
                                    String splitter,
                                    Collection<JsonPair> collection){
        final String key = pair.getKey();
        final JsonElement element = pair.getValue();
        if (element != null && !element.isJsonNull()) {
            if (element.isJsonPrimitive()) {
                collection.add(JsonPair.of(key, element.getAsString()));
            } else if (element.isJsonArray()) {
                for (JsonElement jsonElement : element.getAsJsonArray()) {
                    dfsHelper(InternalPair.of(key, jsonElement), needPrefix, splitter, collection);
                }
            } else if (element.isJsonObject()) {
                element.getAsJsonObject().entrySet().forEach(e -> {
                    final String key1 = (needPrefix)? key + splitter + e.getKey() : e.getKey();
                    final JsonElement value = e.getValue();
                    dfsHelper(InternalPair.of(key1, value), needPrefix, splitter, collection);
                });
            }
        }
    }

    @Data(staticConstructor = "of")
    protected static class InternalPair implements Pair<String, JsonElement> {

        private final String key;

        private final JsonElement value;
    }

    protected static void bfsHelper(){

    }

    protected static boolean checkJson(String str){
        try {
            final JsonElement parse = JsonParser.parseString(str);
            return !parse.isJsonNull() && (parse.isJsonObject() || parse.isJsonArray());
        } catch (Exception e) {
            return false;
        }
    }

}
