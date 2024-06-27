package com.observer.demo.domain.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.observer.demo.domain.table.MunziTestDev;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MunziTestDevRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public Optional<MunziTestDev> findById(String pk, String sk) {
        MunziTestDev munziTestDev = dynamoDBMapper.load(MunziTestDev.class, pk, sk);
        return Optional.of(munziTestDev);
    }

    public void save(MunziTestDev munziTestDev) {
        dynamoDBMapper.save(munziTestDev);
    }

    public void saveAll(List<MunziTestDev> items) {
        List<MunziTestDev> batch = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            batch.add(items.get(i));
            if (batch.size() == 25 || i == items.size() - 1) {
                dynamoDBMapper.batchSave(batch);
                batch.clear();
            }
        }
    }

    public void batchDeleteItems() {
        // Define a scan expression to retrieve all items in the table
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        // Retrieve the items to delete
        List<MunziTestDev> items = dynamoDBMapper.scan(MunziTestDev.class, scanExpression);

        // Delete the items
        dynamoDBMapper.batchDelete(items);
    }

    public void deleteItem(String pk, String sk) {
        MunziTestDev itemToDelete = MunziTestDev.of(pk, sk);

        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .build();

        dynamoDBMapper.delete(itemToDelete, config);
    }

}
