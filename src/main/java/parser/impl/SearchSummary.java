package parser.impl;

import lombok.Data;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : jsonSearcher
 * @date : 2021-03-15 16:57
 * @description :
 */
@Data(staticConstructor = "of")
public class SearchSummary {

    private final int index;

    private final List<JsonPair> pairs;

}
