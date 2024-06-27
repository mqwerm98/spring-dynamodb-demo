package com.observer.demo.domain.table;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@DynamoDBTable(tableName = "MUNZI-TEST-DEV-2")
@NoArgsConstructor
@Setter
@ToString
public class MunziTestDev {

    // partition key
    protected String pk;

    // sort key
    protected String sk;

    // str GSI 1
    protected String timestampGsi;

    // str GSI 1
    protected String strSk2;

    // number GSI 1
    protected Long numSk1;

    // number GSI 2
    protected Long numSk2;

    protected Map<String, Object> additionalAttributes;

    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk() {
        return sk;
    }

    @DynamoDBHashKey(attributeName = "pk")
    @DynamoDBIndexHashKey(globalSecondaryIndexNames = {"pk-s-gsi-1-index", "pk-s-gsi-2-index",
            "pk-n-gsi-1-index", "pk-n-gsi-2-index"},
            attributeName = "pk")
    public String getPk() {
        return pk;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "pk-s-gsi-1-index", attributeName = "s-gsi-1")
    public String getTimestampGsi() {
        return timestampGsi;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "pk-s-gsi-2-index", attributeName = "s-gsi-2")
    public String getStrSk2() {
        return strSk2;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "pk-n-gsi-1-index", attributeName = "n-gsi-1")
    public Long getNumSk1() {
        return numSk1;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "pk-n-gsi-2-index", attributeName = "n-gsi-2")
    public Long getNumSk2() {
        return numSk2;
    }

    @DynamoDBAttribute(attributeName = "additionalAttributes")
    public Map<String, Object> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(Map<String, Object> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    protected MunziTestDev(String pk, String sk) {
        this.pk = pk;
        this.sk = sk;
        this.timestampGsi = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + "#" + UUID.randomUUID();
    }

    protected MunziTestDev(String pk, String sk, String strSk2, Long numSk1, Long numSk2) {
        this.pk = pk;
        this.sk = sk;
        this.timestampGsi = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + "#" + UUID.randomUUID();
        this.strSk2 = strSk2;
        this.numSk1 = numSk1;
        this.numSk2 = numSk2;
    }

    public static MunziTestDev of(String pk, String sk) {
        return new MunziTestDev(pk, sk);
    }

}
