package parser.impl;

import enums.SearchType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static util.Options.setOf;

public class JsonTest {

    String page = "{\"code\":null,\"data\":{\"title\":\"本周推荐设计师\",\"items\":[{\"user\":0},{\"user\":\"\"},{\"user\":[{\"user\":0},{\"user\":[]},{\"user\":{}}]}]}}";


    String o = "\"{\\\"allPageNumber\\\":1.0,\\\"totalCount\\\":3,\\\"page\\\":1,\\\"list\\\":[{\\\"spec\\\":\\\"02020732720201\\\",\\\"content\\\":\\\"衣服收到了，质量很不错，买来宝宝满月的时候穿\\\",\\\"createTime\\\":\\\"2018-10-19 18:07:45\\\",\\\"point\\\":100.0,\\\"imageList\\\":\\\"[{\\\\\\\"urlBig\\\\\\\":\\\\\\\"https://img01.meituncdn.com/group1/M00/80/06/671478c918094a49a06f7ee73eb9e850.jpg?imageView2/2/w/310/h/310\\\\\\\",\\\\\\\"url\\\\\\\":\\\\\\\"https://img01.meituncdn.com/group1/M00/80/06/671478c918094a49a06f7ee73eb9e850.jpg?imageView2/2/w/100/h/100\\\\\\\"}]\\\",\\\"babyage\\\":\\\"\\\",\\\"userName\\\":\\\"成***水\\\",\\\"isDigest\\\":0},{\\\"content\\\":\\\"用户没有做出评价，系统默认好评\\\",\\\"createTime\\\":\\\"2018-04-29 04:03:44\\\",\\\"point\\\":100.0,\\\"babyage\\\":\\\"\\\",\\\"userName\\\":\\\"树友t****l10\\\",\\\"isDigest\\\":0},{\\\"content\\\":\\\"用户没有做出评价，系统默认好评\\\",\\\"createTime\\\":\\\"2018-04-13 04:02:13\\\",\\\"point\\\":100.0,\\\"babyage\\\":\\\"\\\",\\\"userName\\\":\\\"树友o****jxl\\\",\\\"isDigest\\\":0}]}\"";


    @Test
    public void jsonParseTest(){
        final Json json = Json.builder()
                .withJson(page)
                .withPrefix()
                .build();

    }

    @Test
    public void jsonDfsWithPrefixTest(){
        final String url = Json.builder()
                .withJson(page)
                .withPrefix()
                .build()
                .searcher()
                .getByKey("data.list.user.avatar_url");

        assertEquals("https://user-platform-oss.kujiale.com/avatars/2020/10/04/SYNDQ5DI4CQTUZ4XLQM6YPY8",
                url);
    }

    @Test
    public void jsonDfsWithNoPrefixTest(){
        final String url = Json.builder()
                .withJson(page)
                .build()
                .searcher()
                .getByKey("avatar_url");

        assertEquals("https://user-platform-oss.kujiale.com/avatars/2020/10/04/SYNDQ5DI4CQTUZ4XLQM6YPY8",
                url);
    }

    @Test
    public void jsonBfsWithPrefixTest(){
        final String url = Json.builder()
                .withJson(page)
                .withPrefix()
                .build()
                .searcher(SearchType.BFS)
                .getByKey("data.list.user.avatar_url");

        assertEquals("https://user-platform-oss.kujiale.com/avatars/2020/10/04/SYNDQ5DI4CQTUZ4XLQM6YPY8",
                url);
    }

    @Test
    public void jsonBfsWithNoPrefixTest(){
        final String url = Json.builder()
                .withJson(page)
                .build()
                .searcher(SearchType.BFS)
                .getByKey("avatar_url");

        assertEquals("https://user-platform-oss.kujiale.com/avatars/2020/10/04/SYNDQ5DI4CQTUZ4XLQM6YPY8",
                url);
    }

    @Test
    public void bfsWithNoPrefixTest(){
        System.out.println(Json.builder()
                .withJson(page)
                .build()
                .bfs());
    }

    @Test
    public void bfsWithPrefixTest(){
        System.out.println(Json.builder()
                .withPrefix()
                .withJson(page)
                .build()
                .bfs());
    }

    @Test
    public void withPrefixEqualsTest(){
        final Json build1 = Json.builder()
                .withPrefix()
                .withJson(page)
                .build();
        assertTrue(build1.bfs().containsAll(build1.dfs()));
        assertTrue(build1.dfs().containsAll(build1.bfs()));
    }

    @Test
    public void withNoPrefixEqualsTest(){
        final Json build2 = Json.builder()
                .withJson(page)
                .build();
        assertTrue(build2.bfs().containsAll(build2.dfs()));
        assertTrue(build2.dfs().containsAll(build2.bfs()));
    }

    @Test
    public void jsonSearchTest(){
        final List<SearchSummary> summary = Json.builder()
                .withJson(page)
                .withPrefix()
                .build()
                .searcher()
                .summary();
    }



}