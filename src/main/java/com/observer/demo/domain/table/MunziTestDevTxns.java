package com.observer.demo.domain.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@DynamoDBTable(tableName = "MUNZI-TEST-DEV-2")
@NoArgsConstructor
@Setter
@ToString(callSuper = true)
public class MunziTestDevTxns extends MunziTestDev {

    private Set<String> txHashSet;

    @DynamoDBAttribute(attributeName = "txHashSet")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.SS)
    public Set<String> getTxHashSet() {
        return txHashSet;
    }

    private MunziTestDevTxns(long blockNumber, Set<String> txHashSet) {
        super("txns", Long.toString(blockNumber));
        this.txHashSet = txHashSet;
    }

    public static MunziTestDevTxns of(long blockNumber, Set<String> txHashSet) {
        return new MunziTestDevTxns(blockNumber, txHashSet);
    }


}