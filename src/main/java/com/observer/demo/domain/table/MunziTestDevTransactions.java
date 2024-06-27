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
public class MunziTestDevTransactions extends MunziTestDev {

    private String txHash;

    private String methodId;

    private LocalDateTime timestamp;

    private String from;
    private String to;
    private Double value;

    private Double txFee;

    private TransactionAddInfo addInfo;

    //TODO 데이터 밀고나선 필요없음 지울것
    private Long blockTxNo;


    @DynamoDBDocument
    @NoArgsConstructor
    @Setter
    public static class TransactionAddInfo {

        public Integer internalTxCount;
        public Double gasPrice;
        public Long nonce;

        @DynamoDBAttribute(attributeName = "internalTxCount")
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
        public Integer getInternalTxCount() {
            return internalTxCount;
        }

        @DynamoDBAttribute(attributeName = "gasPrice")
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
        public Double getGasPrice() {
            return gasPrice;
        }

        @DynamoDBAttribute(attributeName = "nonce")
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
        public Long getNonce() {
            return nonce;
        }

        private TransactionAddInfo(Integer internalTxCount, Double gasPrice, Long nonce) {
            this.internalTxCount = internalTxCount;
            this.gasPrice = gasPrice;
            this.nonce = nonce;
        }

        public static TransactionAddInfo of(Integer internalTxCount, Double gasPrice, Long nonce) {
            return new TransactionAddInfo(internalTxCount, gasPrice, nonce);
        }
    }

    public Long testGetBlockNumber() {
        return this.numSk1;
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    @DynamoDBTypeConverted(converter = LocalDateTimeToStringConverter.class)
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @DynamoDBAttribute(attributeName = "txHash")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    public String getTxHash() {
        return txHash;
    }

    @DynamoDBAttribute(attributeName = "methodId")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    public String getMethodId() {
        return methodId;
    }

    @DynamoDBAttribute(attributeName = "from")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    public String getFrom() {
        return from;
    }

    @DynamoDBAttribute(attributeName = "to")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    public String getTo() {
        return to;
    }

    @DynamoDBAttribute(attributeName = "value")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Double getValue() {
        return value;
    }

    @DynamoDBAttribute(attributeName = "txFee")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    public Double getTxFee() {
        return txFee;
    }

    @DynamoDBAttribute(attributeName = "addInfo")
    public TransactionAddInfo getAddInfo() {
        return addInfo;
    }

    public Long testGetBlockTxNo() {
        return this.numSk2;
    }

    //TODO 데이터 밀고나선 필요없음 지울것
    public Long getBlockTxNo() {
        return this.blockTxNo;
    }

    private MunziTestDevTransactions(String txHash, String methodId, long blockNumber, long blockTxNo, LocalDateTime timestamp,
                                     String from, String to, Double value, Double txFee, TransactionAddInfo addInfo) {
        super("transactions", txHash, String.format("%019d#%019d", blockNumber, blockTxNo), blockNumber, blockTxNo);
        this.txHash = txHash;
        this.methodId = methodId;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.value = value;
        this.txFee = txFee;
        this.addInfo = addInfo;
    }

    private MunziTestDevTransactions(String txHash) {
        super("transactions", txHash);
    }

    public static MunziTestDevTransactions of(String txHash, String methodId, long blockNumber, long blockTxNo, LocalDateTime timestamp,
                                              String from, String to, Double value, Double txFee, TransactionAddInfo addInfo) {
        return new MunziTestDevTransactions(txHash, methodId, blockNumber, blockTxNo, timestamp, from, to, value, txFee, addInfo);
    }

    public static MunziTestDevTransactions transactionOf(String txHash) {
        return new MunziTestDevTransactions(txHash);
    }


}