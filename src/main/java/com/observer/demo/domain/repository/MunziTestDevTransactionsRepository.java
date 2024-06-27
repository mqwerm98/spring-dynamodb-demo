package com.observer.demo.domain.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.observer.demo.domain.table.MunziTestDevBlocks;
import com.observer.demo.domain.table.MunziTestDevTransactions;
import com.observer.demo.domain.util.MunziTestDevUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class MunziTestDevTransactionsRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final MunziTestDevUtil util;

    private final String PK = "transactions";

    public void save(MunziTestDevTransactions munziTestDevTransactions) {
        dynamoDBMapper.save(munziTestDevTransactions);
    }

    public void saveAll(List<MunziTestDevTransactions> items) {
        List<MunziTestDevTransactions> batch = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            batch.add(items.get(i));
            if (batch.size() == 25 || i == items.size() - 1) {
                dynamoDBMapper.batchSave(batch);
                batch.clear();
            }
        }
    }

    public List<MunziTestDevTransactions> getTransacionsByTxHashList(Iterable<String> txHashList) {
        return util.batchLoad(PK, txHashList, MunziTestDevTransactions.class);
    }

    public Optional<MunziTestDevTransactions> findByTxHash(String txHash) {
        MunziTestDevTransactions result = dynamoDBMapper.load(MunziTestDevTransactions.class, PK, txHash);
        return Optional.of(result);
    }

    public QueryResultPage<MunziTestDevTransactions> queryWithPagination(String lastEvaluatedKey, String sk, int size) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
                .build();

        DynamoDBQueryExpression<MunziTestDevTransactions> queryExpression = new DynamoDBQueryExpression<MunziTestDevTransactions>()
                .withIndexName("pk-s-gsi-2-index")
                .withKeyConditionExpression("pk = :v_pk")
                .withExpressionAttributeValues(Map.of(":v_pk", new AttributeValue().withS(PK)))
                .withScanIndexForward(true)
                .withLimit(size)
                .withConsistentRead(false); // ASC 정렬 (기본값)

        if (lastEvaluatedKey != null && sk != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("pk", new AttributeValue().withS(PK));
            startKey.put("sk", new AttributeValue().withS(sk));
            startKey.put("s-gsi-2", new AttributeValue().withS(lastEvaluatedKey));
            queryExpression.setExclusiveStartKey(startKey);
        }


        return dynamoDBMapper.queryPage(MunziTestDevTransactions.class, queryExpression, config);
    }

    public QueryResultPage<MunziTestDevTransactions> queryWithPagination(Map<String, AttributeValue> lastEvaluatedKeyMap, int size) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
                .build();

        DynamoDBQueryExpression<MunziTestDevTransactions> queryExpression = new DynamoDBQueryExpression<MunziTestDevTransactions>()
                .withIndexName("pk-s-gsi-2-index")
                .withKeyConditionExpression("pk = :v_pk")
                .withExpressionAttributeValues(Map.of(":v_pk", new AttributeValue().withS(PK)))
                .withScanIndexForward(true)
                .withLimit(size)
                .withConsistentRead(false); // ASC 정렬 (기본값)

        queryExpression.setExclusiveStartKey(lastEvaluatedKeyMap);


        return dynamoDBMapper.queryPage(MunziTestDevTransactions.class, queryExpression, config);
    }

    public List<MunziTestDevTransactions> getTransacionsByBlockNumberBetween(long from, long to) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":v_pk", new AttributeValue().withS(PK));
        expressionAttributeValues.put(":v_from", new AttributeValue().withN(Long.toString(from)));
        expressionAttributeValues.put(":v_to", new AttributeValue().withN(Long.toString(to)));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#ngsi1", "n-gsi-1");

        DynamoDBQueryExpression<MunziTestDevTransactions> queryExpression = new DynamoDBQueryExpression<MunziTestDevTransactions>()
                .withIndexName("pk-n-gsi-1-index")
                .withKeyConditionExpression("pk = :v_pk AND #ngsi1 BETWEEN :v_from AND :v_to")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withExpressionAttributeNames(expressionAttributeNames)
                .withConsistentRead(false); // GSI에서는 일관된 읽기를 사용할 수 없습니다.

        return dynamoDBMapper.query(MunziTestDevTransactions.class, queryExpression);
    }


}
