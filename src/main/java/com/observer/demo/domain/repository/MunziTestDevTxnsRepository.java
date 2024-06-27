package com.observer.demo.domain.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.observer.demo.domain.table.MunziTestDevTxns;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MunziTestDevTxnsRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public Set<String> getTxHashListByBlockNumber(long blockNumber) {
        MunziTestDevTxns txns = dynamoDBMapper.load(MunziTestDevTxns.class, "txns", Long.toString(blockNumber));
        if (txns == null) {
            return new HashSet<>();
        }
        return txns.getTxHashSet();
    }

    public void saveAll(List<MunziTestDevTxns> items) {
        List<MunziTestDevTxns> batch = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            batch.add(items.get(i));
            if (batch.size() == 25 || i == items.size() - 1) {
                dynamoDBMapper.batchSave(batch);
                batch.clear();
            }
        }
    }

}
