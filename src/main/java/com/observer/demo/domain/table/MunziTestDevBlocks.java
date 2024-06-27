package com.observer.demo.domain.table;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.observer.demo.domain.converter.LocalDateTimeToStringConverter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@DynamoDBTable(tableName = "MUNZI-TEST-DEV-2")
@NoArgsConstructor
@Setter
@ToString(callSuper = true)
public class MunziTestDevBlocks extends MunziTestDev {

    private Long blockNumber;

    private LocalDateTime timestamp;

    private Integer txCount;

    private String feeRecipientAddress;

    private Integer gasUsed;

    private Integer gasLimit;

    private Double fee;

    private Double reward;

    @DynamoDBAttribute(attributeName = "blockNumber")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Long getBlockNumber() {
        return blockNumber;
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    @DynamoDBTypeConverted(converter = LocalDateTimeToStringConverter.class)
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @DynamoDBAttribute(attributeName = "txCount")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Integer getTxCount() {
        return txCount;
    }

    @DynamoDBAttribute(attributeName = "feeRecipientAddress")
    public String getFeeRecipientAddress() {
        return feeRecipientAddress;
    }

    @DynamoDBAttribute(attributeName = "gasUsed")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Integer getGasUsed() {
        return gasUsed;
    }

    @DynamoDBAttribute(attributeName = "gasLimit")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Integer getGasLimit() {
        return gasLimit;
    }

    @DynamoDBAttribute(attributeName = "fee")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Double getFee() {
        return fee;
    }

    @DynamoDBAttribute(attributeName = "reward")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Double getReward() {
        return reward;
    }

    public Long testGetCurrentTxSum() {
        return super.getNumSk2();
    }

    private MunziTestDevBlocks(long blockNumber, LocalDateTime timestamp, Integer txCount, long currentTxSum,
                               String feeRecipientAddress, Integer gasUsed, Integer gasLimit,
                               Double fee, Double reward) {
        super("blocks", Long.toString(blockNumber), null, blockNumber, currentTxSum);
        this.blockNumber = blockNumber;
        this.timestamp = timestamp;
        this.txCount = txCount;
        this.feeRecipientAddress = feeRecipientAddress;
        this.gasUsed = gasUsed;
        this.gasLimit = gasLimit;
        this.fee = fee;
        this.reward = reward;
    }

    public static MunziTestDevBlocks of(long blockNumber, LocalDateTime timestamp, Integer txCount, long currentTxSum,
                                        String feeRecipientAddress, Integer gasUsed, Integer gasLimit,
                                        Double fee, Double reward) {
        return new MunziTestDevBlocks(blockNumber, timestamp, txCount, currentTxSum, feeRecipientAddress, gasUsed, gasLimit,
                fee, reward);
    }


}