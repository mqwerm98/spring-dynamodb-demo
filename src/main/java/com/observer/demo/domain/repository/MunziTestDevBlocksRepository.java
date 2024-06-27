package com.observer.demo.domain.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.observer.demo.domain.table.MunziTestDevBlocks;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class MunziTestDevBlocksRepository {

    private final DynamoDBMapper dynamoDBMapper;

    private final String PK = "blocks";

    public Optional<MunziTestDevBlocks> findByBlockNumber(long blockNumber) {
        MunziTestDevBlocks block = dynamoDBMapper.load(MunziTestDevBlocks.class, PK, Long.toString(blockNumber));
        return Optional.of(block);
    }

    public void save(MunziTestDevBlocks munziTestDevBlocks) {
        dynamoDBMapper.save(munziTestDevBlocks);
    }

    public void saveAll(List<MunziTestDevBlocks> items) {
        List<MunziTestDevBlocks> batch = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            batch.add(items.get(i));
            if (batch.size() == 25 || i == items.size() - 1) {
                dynamoDBMapper.batchSave(batch);
                batch.clear();
            }
        }
    }

    public QueryResultPage<MunziTestDevBlocks> queryWithPagination(String lastEvaluatedKey, String sk, int size) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
                .build();

        DynamoDBQueryExpression<MunziTestDevBlocks> queryExpression = new DynamoDBQueryExpression<MunziTestDevBlocks>()
                .withIndexName("pk-s-gsi-1-index")
                .withKeyConditionExpression("pk = :v_pk")
                .withExpressionAttributeValues(Map.of(":v_pk", new AttributeValue().withS(PK)))
                .withScanIndexForward(true)
                .withLimit(size)
                .withConsistentRead(false); // ASC 정렬 (기본값)

        if (lastEvaluatedKey != null && sk != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("pk", new AttributeValue().withS(PK));
            startKey.put("sk", new AttributeValue().withS(sk));
            startKey.put("s-gsi-1", new AttributeValue().withS(lastEvaluatedKey));
            queryExpression.setExclusiveStartKey(startKey);
        }


        return dynamoDBMapper.queryPage(MunziTestDevBlocks.class, queryExpression, config);
    }

    public QueryResultPage<MunziTestDevBlocks> queryWithPagination(Map<String, AttributeValue> lastEvaluatedKeyMap, int size) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
                .build();

        DynamoDBQueryExpression<MunziTestDevBlocks> queryExpression = new DynamoDBQueryExpression<MunziTestDevBlocks>()
                .withIndexName("pk-s-gsi-1-index")
                .withKeyConditionExpression("pk = :v_pk")
                .withExpressionAttributeValues(Map.of(":v_pk", new AttributeValue().withS(PK)))
                .withScanIndexForward(true)
                .withLimit(size)
                .withConsistentRead(false); // ASC 정렬 (기본값)

        queryExpression.setExclusiveStartKey(lastEvaluatedKeyMap);


        return dynamoDBMapper.queryPage(MunziTestDevBlocks.class, queryExpression, config);
    }

    public QueryResultPage<MunziTestDevBlocks> queryWithPagination(long maxBlockNumber, int page, int size) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
                .build();

        DynamoDBQueryExpression<MunziTestDevBlocks> queryExpression = new DynamoDBQueryExpression<MunziTestDevBlocks>()
                .withIndexName("pk-n-gsi-1-index")
                .withKeyConditionExpression("pk = :v_pk")
                .withExpressionAttributeValues(Map.of(":v_pk", new AttributeValue().withS(PK)))
                .withScanIndexForward(false)
                .withLimit(size)
                .withConsistentRead(false);

        long startBlockNumber = maxBlockNumber - (long) page * size + 1;

        Map<String, AttributeValue> startKey = new HashMap<>();
        startKey.put("pk", new AttributeValue().withS(PK));
        startKey.put("sk", new AttributeValue().withS(String.valueOf(startBlockNumber)));
        startKey.put("n-gsi-1", new AttributeValue().withN(String.valueOf(startBlockNumber)));
        queryExpression.setExclusiveStartKey(startKey);

        return dynamoDBMapper.queryPage(MunziTestDevBlocks.class, queryExpression, config);
    }

    public List<MunziTestDevBlocks> getBlocksByCurrentTxSumBetween(long from, long to) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":v_pk", new AttributeValue().withS(PK));
        expressionAttributeValues.put(":v_from", new AttributeValue().withN(Long.toString(from)));
        expressionAttributeValues.put(":v_to", new AttributeValue().withN(Long.toString(to)));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#ngsi2", "n-gsi-2");

        DynamoDBQueryExpression<MunziTestDevBlocks> queryExpression = new DynamoDBQueryExpression<MunziTestDevBlocks>()
                .withIndexName("pk-n-gsi-2-index")
                .withKeyConditionExpression("pk = :v_pk AND #ngsi2 BETWEEN :v_from AND :v_to")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withExpressionAttributeNames(expressionAttributeNames)
                .withConsistentRead(false); // GSI에서는 일관된 읽기를 사용할 수 없습니다.

        return dynamoDBMapper.query(MunziTestDevBlocks.class, queryExpression);
    }
}
