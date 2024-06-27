package com.observer.demo.domain.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MunziTestDevUtil {

    private final DynamoDBMapper mapper;

    public <RETURN_TYPE> List<RETURN_TYPE> batchLoad(String pk, Iterable<String> skList, Class<RETURN_TYPE> returnType) {
        List<KeyPair> keyPairList = new ArrayList<>();
        for (String sk : skList) {
            KeyPair keyPair = new KeyPair();
            keyPair.withHashKey(pk);
            keyPair.withRangeKey(sk);
            keyPairList.add(keyPair);
        }

        Map<Class<?>, List<KeyPair>> keyPairForTable = new HashMap<>();
        keyPairForTable.put(returnType, keyPairList);

        List<RETURN_TYPE> result = new ArrayList<>();
        Map<String, List<Object>> stringListMap = mapper.batchLoad(keyPairForTable);
        if (stringListMap.isEmpty() || !stringListMap.containsKey("MUNZI-TEST-DEV-2") || CollectionUtils.isEmpty(stringListMap.get("MUNZI-TEST-DEV-2"))) {
            return result;
        }

        for (Object obj : stringListMap.get("MUNZI-TEST-DEV-2")) {
            if (obj == null) continue;
            result.add((RETURN_TYPE) obj);
        }

        return result;
    }
}
