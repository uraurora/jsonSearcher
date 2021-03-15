package parser.impl;

import enums.SearchType;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static util.Options.*;

/**
 * @author : gaoxiaodong04
 * @program : jsonSearcher
 * @date : 2021-03-14 21:50
 * @description :
 */
public class JsonSearcher {

    private final Json json;

    private final List<JsonPair> pairs;

    private final SearchType type;

    private final List<Predicate<? super JsonPair>> selectors = listOf();

    public JsonSearcher(Json json, SearchType type) {
        this.json = json;
        this.type = type;
        this.pairs = this.type == SearchType.BFS ? this.json.bfs():this.json.dfs();
    }

    public String getByKey(String key){
        return listByKeySelector(k->k.equalsIgnoreCase(key))
                .stream()
                .map(JsonPair::getValue)
                .findFirst()
                .orElse(emptyString);
    }

    public List<String> listByKey(String key){
        return listByKeySelector(k->k.equalsIgnoreCase(key))
                .stream()
                .map(JsonPair::getValue)
                .collect(Collectors.toList());
    }

    public String getByValue(String value){
        return listByValueSelector(v->v.equalsIgnoreCase(value))
                .stream()
                .map(JsonPair::getKey)
                .findFirst()
                .orElse(emptyString);
    }

    public List<String> listByValue(String value){
        return listByValueSelector(v->v.equalsIgnoreCase(value))
                .stream()
                .map(JsonPair::getKey)
                .collect(Collectors.toList());
    }


    public List<JsonPair> listByKeySelector(Predicate<? super String> keySelector){
        return pairs.stream()
                .filter(p->keySelector.test(p.getKey()))
                .collect(Collectors.toList());
    }

    public List<JsonPair> listByValueSelector(Predicate<? super String> valueSelector){
        return pairs.stream()
                .filter(p->valueSelector.test(p.getValue()))
                .collect(Collectors.toList());
    }

    public List<JsonPair> listBySelector(Predicate<? super JsonPair> selector){
        return pairs.stream()
                .filter(selector)
                .collect(Collectors.toList());
    }

    public static JsonSearcher from(Json json, SearchType type) {
        return new JsonSearcher(json, type);
    }

    public JsonSearcher addSelector(Predicate<? super JsonPair> selector){
        selectors.add(selector);
        return this;
    }

    public JsonSearcher addSelectors(List<Predicate<? super JsonPair>> selectors){
        this.selectors.addAll(selectors);
        return this;
    }

    @SafeVarargs
    public final JsonSearcher addSelectors(Predicate<? super JsonPair>... selectors){
        this.selectors.addAll(listOf(selectors));
        return this;
    }

    public List<SearchSummary> summary(){
        return buildList(l->{
            for (int i = 0; i < selectors.size(); i++) {
                final SearchSummary summary = SearchSummary.of(i, listBySelector(selectors.get(i)));
                l.add(summary);
            }
        });
    }

    public JsonSearcher clearSelectors(){
        this.selectors.clear();
        return this;
    }
}
