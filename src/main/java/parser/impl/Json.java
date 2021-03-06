package parser.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import enums.SearchType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import parser.Pair;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static util.Options.*;

/**
 * @author : gaoxiaodong04
 * @program : jsonSearcher
 * @date : 2021-03-12 17:37
 * @description :
 */
public class Json {

    private String json;

    private boolean needPrefix;

    private String splitter;

    private JsonElement element;

    public Json(JsonBuilder builder){
        this.json = builder.json;
        this.needPrefix = builder.needPrefix;
        checkJson(this.json);
        this.splitter = builder.splitter;
        this.element = JsonParser.parseString(json);
    }

    public static JsonBuilder builder() {
        return new JsonBuilder();
    }

    public Stream<JsonPair> dfsStream(){
        return dfs().stream();
    }

    public List<JsonPair> dfs(){
        final List<JsonPair> res = listOf();
        dfsHelper(InternalPair.of(emptyString, element), needPrefix, splitter, res);
        return res;
    }

    public List<JsonPair> bfs(){
        return bfsHelper(element, needPrefix, splitter);
    }

    public Stream<JsonPair> bfsStream(){
        return bfs().stream();
    }

    public JsonSearcher searcher(){
        return JsonSearcher.from(this, SearchType.DFS);
    }

    public JsonSearcher searcher(SearchType type){
        return JsonSearcher.from(this, type);
    }

    protected static List<JsonPair> bfsHelper(JsonElement element, boolean needPrefix, String splitter){
        final List<JsonPair> res = listOf();
        final Deque<InternalPair> deque = buildDeque(q->q.add(InternalPair.of("", element)));

        while(!deque.isEmpty()){
            final InternalPair jsonPair = deque.pollFirst();
            final String jsonKey = jsonPair.getKey();
            final JsonElement jsonValue = jsonPair.getValue();

            if(jsonValue != null && !jsonValue.isJsonNull()){
                if(jsonValue.isJsonArray()){
                    for (JsonElement jsonElement : jsonValue.getAsJsonArray()) {
                        final InternalPair pair = InternalPair.of(jsonKey, jsonElement);
                        deque.addLast(pair);

                    }
                }else if(jsonValue.isJsonObject()){
                    jsonValue.getAsJsonObject().entrySet().forEach(e -> {
                        final JsonElement value = e.getValue();
                        final String key = needPrefix && StringUtils.isNotBlank(jsonKey)? jsonKey + splitter + e.getKey() : e.getKey();
                        final InternalPair pair = InternalPair.of(key, value);
                        deque.addLast(pair);
                    });
                }else if(jsonValue.isJsonPrimitive()){
                    res.add(JsonPair.of(jsonKey, jsonValue.getAsString()));
                }
            }
        }
        return res;
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
                    final String key1 = (needPrefix) && StringUtils.isNotBlank(key)? key + splitter + e.getKey() : e.getKey();
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

    protected static boolean isJson(String str){
        try {
            final JsonElement parse = JsonParser.parseString(str);
            return !parse.isJsonNull() && (parse.isJsonObject() || parse.isJsonArray());
        } catch (Exception e) {
            return false;
        }
    }

    protected static void checkJson(String str){
        if(!isJson(str)){
            throw new IllegalArgumentException("the args string is not json format");
        }
    }

    public static final class JsonBuilder {
        private String json;
        private boolean needPrefix = false;
        private String splitter = ".";

        private JsonBuilder() {
        }

        public JsonBuilder withJson(String json) {
            this.json = json;
            return this;
        }

        public JsonBuilder withPrefix() {
            this.needPrefix = true;
            return this;
        }

        public JsonBuilder withSplitter(String splitter) {
            this.splitter = splitter;
            return this;
        }


        public Json build() {
            return new Json(this);
        }
    }
}
