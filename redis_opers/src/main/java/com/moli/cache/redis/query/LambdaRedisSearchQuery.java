package com.moli.cache.redis.query;

import com.moli.cache.redis.entity.DocumentEntity;
import com.moli.cache.redis.annotation.RedisSearch;
import com.moli.cache.redis.utils.RedisSearchClientUtil;
import io.redisearch.Document;
import io.redisearch.Query;
import io.redisearch.SearchResult;
import io.redisearch.client.Client;

import java.util.List;

/**
 * @author moli
 * @time 2024-03-22 15:30:31
 * @description 查询数据
 */
public class LambdaRedisSearchQuery<T> {

    private static final String FIELD_PREFIX = "@";
    private static final String NOT_FIELD_PREFIX = "-@";
    private static final String FIELD_VALUE_INTERVAL = ":";
    private static final String FIELD_LEFT_BRACKETS = "(";
    private static final String FIELD_RIGHT_BRACKETS = ")";
    private static final String FIELD_LEFT_MIDDLE_BRACKETS = "[";
    private static final String FIELD_RIGHT_MIDDLE_BRACKETS = "]";
    private static final String ADD_INF = "+inf";
    private static final String SUB_INF = "-inf";
    private static final String STAR = "*";


    private static final String BLANK = " ";
    private static final String OR = "|";


    private final ColumnUtil columnUtil = new ColumnUtil();

    private final StringBuffer search = new StringBuffer();
    private final T t;
    Client client = null;


    public LambdaRedisSearchQuery(T t) {
        this.t = t;
        String index = getIndexName();
        client = RedisSearchClientUtil.getClient(index);
    }

    public LambdaRedisSearchQuery<T> eq(RedisFunction<T, ?> function, String value) {
        String name = ColumnUtil.getName(function);
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append("\"")
                .append(value)
                .append("\"")
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;

    }

    public LambdaRedisSearchQuery<T> ne(RedisFunction<T, ?> function, String value) {
        String name = ColumnUtil.getName(function);
        search.append(FIELD_LEFT_BRACKETS)
                .append(NOT_FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append("\"")
                .append(value)
                .append("\"")
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public LambdaRedisSearchQuery<T> in(RedisFunction<T, ?> function, List<?> value) {
        String name = ColumnUtil.getName(function);
        search.append(FIELD_LEFT_BRACKETS);
        search.append(FIELD_PREFIX);
        search.append(name);
        search.append(FIELD_VALUE_INTERVAL);
        search.append(FIELD_LEFT_BRACKETS);

        for (int i = 0; i < value.size(); i++) {
            search.append("\"");
            search.append(value.get(i).toString());
            search.append("\"");
            if (i != value.size() - 1) {
                search.append(OR);
            }
        }

        search.append(FIELD_RIGHT_BRACKETS);
        search.append(FIELD_RIGHT_BRACKETS);
        search.append(BLANK);
        return this;

    }

    public LambdaRedisSearchQuery<T> nin(RedisFunction<T, ?> function, List<?> value) {
        String name = ColumnUtil.getName(function);
        search.append(FIELD_LEFT_BRACKETS);
        search.append(NOT_FIELD_PREFIX);
        search.append(name);
        search.append(FIELD_VALUE_INTERVAL);
        search.append(FIELD_LEFT_BRACKETS);

        for (int i = 0; i < value.size(); i++) {
            search.append("\"");
            search.append(value.get(i).toString());
            search.append("\"");
            if (i != value.size() - 1) {
                search.append(OR);
            }
        }

        search.append(FIELD_RIGHT_BRACKETS);
        search.append(FIELD_RIGHT_BRACKETS);
        search.append(BLANK);
        return this;

    }

    public LambdaRedisSearchQuery<T> between(RedisFunction<T, ?> function, Double min, Double max) {
        String name = ColumnUtil.getName(function);
//        @num:[10 20]
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append(FIELD_LEFT_MIDDLE_BRACKETS)
                .append(min.toString())
                .append(BLANK)
                .append(max.toString())
                .append(FIELD_RIGHT_MIDDLE_BRACKETS)
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public LambdaRedisSearchQuery<T> ge(RedisFunction<T, ?> function, Double min) {
        String name = ColumnUtil.getName(function);
//        @num:[10 +inf]
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append(FIELD_LEFT_MIDDLE_BRACKETS)
                .append(min.toString())
                .append(BLANK)
                .append(ADD_INF)
                .append(FIELD_RIGHT_MIDDLE_BRACKETS)
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public LambdaRedisSearchQuery<T> gt(RedisFunction<T, ?> function, Double min) {
        String name = ColumnUtil.getName(function);
//        @num:[(10 +inf]
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append(FIELD_LEFT_MIDDLE_BRACKETS)
                .append(FIELD_LEFT_BRACKETS)
                .append(min.toString())
                .append(BLANK)
                .append(ADD_INF)
                .append(FIELD_RIGHT_MIDDLE_BRACKETS)
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public LambdaRedisSearchQuery<T> le(RedisFunction<T, ?> function, Double min) {
        String name = ColumnUtil.getName(function);
//        @num:[-inf (10]
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append(FIELD_LEFT_MIDDLE_BRACKETS)
                .append(SUB_INF)
                .append(BLANK)
                .append(min.toString())
                .append(FIELD_RIGHT_MIDDLE_BRACKETS)
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public LambdaRedisSearchQuery<T> lt(RedisFunction<T, ?> function, Double min) {
        String name = ColumnUtil.getName(function);
//        @num:[-inf (10]
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append(FIELD_LEFT_MIDDLE_BRACKETS)
                .append(SUB_INF)
                .append(BLANK)
                .append(FIELD_LEFT_BRACKETS)
                .append(min.toString())
                .append(FIELD_RIGHT_MIDDLE_BRACKETS)
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public LambdaRedisSearchQuery<T> likeRight(RedisFunction<T, ?> function, Object value) {
        String name = ColumnUtil.getName(function);
//        @name:john*
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append(value)
                .append(STAR)
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public LambdaRedisSearchQuery<T> like(RedisFunction<T, ?> function, Object value) {
        String name = ColumnUtil.getName(function);
//        @name:john*
        search.append(FIELD_LEFT_BRACKETS)
                .append(FIELD_PREFIX)
                .append(name)
                .append(FIELD_VALUE_INTERVAL)
                .append(value)
                .append(STAR)
                .append(FIELD_RIGHT_BRACKETS)
                .append(BLANK);
        return this;
    }

    public Document selectById(String docId) {
        return client.getDocument(docId);
    }

    public LambdaRedisSearchQuery<T> or() {
        search.append(OR).append(BLANK);
        return this;

    }

    public void search() {
        String s = search.toString();
        System.out.println(s);
        Query query = new Query(s).limit(0, 10);
        SearchResult result = client.search(query);
        printResult(result);
    }


    private String getIndexName() {

        RedisSearch annotation = t.getClass().getAnnotation(RedisSearch.class);
        if (null != annotation) {
            return annotation.index();
        }
        throw new RuntimeException(t.getClass().getName() + "没找到 @RedisSearch 注解");

    }

    private static void printResult(SearchResult result) {
        List<Document> documentList = result.docs;
        for (Document document : documentList) {
            System.out.println(document.toString());
        }
    }

    public static void main(String[] args) {
        LambdaRedisSearchQuery<DocumentEntity> search = new LambdaRedisSearchQuery<>(new DocumentEntity());
        search.like(DocumentEntity::getTitle, "000");
        search.search();
    }
}
