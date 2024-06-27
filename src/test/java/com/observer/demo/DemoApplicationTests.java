package com.observer.demo;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.observer.demo.common.util.CommonUtil;
import com.observer.demo.domain.repository.MunziTestDevBlocksRepository;
import com.observer.demo.domain.repository.MunziTestDevRepository;
import com.observer.demo.domain.repository.MunziTestDevTransactionsRepository;
import com.observer.demo.domain.repository.MunziTestDevTxnsRepository;
import com.observer.demo.domain.table.MunziTestDev;
import com.observer.demo.domain.table.MunziTestDevBlocks;
import com.observer.demo.domain.table.MunziTestDevTransactions;
import com.observer.demo.domain.table.MunziTestDevTxns;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private MunziTestDevRepository munziTestDevRepository;

    @Autowired
    private MunziTestDevBlocksRepository munziTestDevBlocksRepository;

    @Autowired
    private MunziTestDevTransactionsRepository munziTestDevTransactionsRepository;

    @Autowired
    private MunziTestDevTxnsRepository munziTestDevTxnsRepository;

    @Test
    void contextLoads() {
        String pk = "blocks";
        String sk = "1";
        MunziTestDev munziTestDev = MunziTestDev.of(pk, sk);
        munziTestDevRepository.save(munziTestDev);

        Optional<MunziTestDev> opt = munziTestDevRepository.findById(pk, sk);
        assertTrue(opt.isPresent());
        MunziTestDev result = opt.get();
        System.out.println(result);
    }

    @Test
    void createBlocks() {
        long blockNumber;
        LocalDateTime timestamp = LocalDateTime.now().minusYears(10);
        Integer txCount;
        String feeRecipientAddress;
        Integer gasUsed = 29987621;
        Integer gasLimit = 30000000;
        Double fee = 2.53;
        Double reward = 0.02332;

        String txHash;
        String methodId = "0x9871efa4";
        String from = "0x22dc7c04cad39aac468f7391be19d357da9b6782";
        String to = "0xdfaa460f07205a05e0669255bda08a4bb66a3ab9";
        Double value = 1.1;
        Double txFee = 2.53;
        MunziTestDevTransactions.TransactionAddInfo addInfo;

        List<MunziTestDevBlocks> blocksList = new ArrayList<>();
        List<MunziTestDevTransactions> transactionsList = new ArrayList<>();
        List<MunziTestDevTxns> txns = new ArrayList<>();
        long currentTxSum = 15000L;
        Set<String> txHashList;
        boolean first = false;

        Long maxBlockNumber = 1200L;
        for (int i = 1101; i <= 1200; i++) {
            blockNumber = i;
            timestamp = timestamp.plusSeconds(9);
            txCount = i % 10 + 10;
            feeRecipientAddress = CommonUtil.generateEthereumAddress(blockNumber + "-" + timestamp);

            if (first) {
                first = false;
                if (maxBlockNumber != null) {
                    Optional<MunziTestDevBlocks> lastBlock = munziTestDevBlocksRepository.findByBlockNumber(maxBlockNumber);
                    if (lastBlock.isPresent()) {
                        currentTxSum = lastBlock.get().testGetCurrentTxSum();
                    }
                }
            }
            currentTxSum += txCount;
            blocksList.add(MunziTestDevBlocks.of(blockNumber, timestamp, txCount, currentTxSum,
                    feeRecipientAddress, gasUsed, gasLimit, fee, reward));

            txHashList = new HashSet<>();
            for (int j = 0; j < txCount; j++) {
                txHash = CommonUtil.generateEthereumAddress(blockNumber + "-" + j + "-" + timestamp);
                addInfo = MunziTestDevTransactions.TransactionAddInfo.of(5, 2.53, blockNumber * 100 + txCount);
                transactionsList.add(MunziTestDevTransactions.of(txHash, methodId, blockNumber, j + 1, timestamp,
                        from, to, value, txFee, addInfo));
                txHashList.add(txHash);
            }

            txns.add(MunziTestDevTxns.of(blockNumber, txHashList));
        }

        System.out.println("blocksList : " + blocksList.size());
        System.out.println("transactionsList : " + transactionsList.size());

        munziTestDevBlocksRepository.saveAll(blocksList);
        munziTestDevTransactionsRepository.saveAll(transactionsList);
        munziTestDevTxnsRepository.saveAll(txns);
    }

    @Test
    void createTransactions() {
        String txHash;
        String methodId = "0x9871efa4";
        long blockNumber;
        LocalDateTime timestamp = LocalDateTime.now().minusYears(10);
        String from = "0x22dc7c04cad39aac468f7391be19d357da9b6782";
        String to = "0xdfaa460f07205a05e0669255bda08a4bb66a3ab9";
        Double value = 1.1;
        Double txFee = 2.53;
        MunziTestDevTransactions.TransactionAddInfo addInfo;

        List<MunziTestDevTransactions> transactionsList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            blockNumber = i;
            timestamp = timestamp.plusSeconds(9);
            int txCount = i % 10 + 10;
            for (int j = 0; j < txCount; j++) {
                txHash = CommonUtil.generateEthereumAddress(blockNumber + "-" + j + "-" + timestamp);
                addInfo = MunziTestDevTransactions.TransactionAddInfo.of(5, 2.53, blockNumber * 100 + txCount);
                transactionsList.add(MunziTestDevTransactions.of(txHash, methodId, blockNumber, j + 1, timestamp,
                        from, to, value, txFee, addInfo));
            }
        }
        munziTestDevTransactionsRepository.saveAll(transactionsList);
    }

//    @Test
//    void deleteAllItems() {
//        munziTestDevRepository.batchDeleteItems();
//    }

    @Test
    void getTransactionsByTxHashList() {
        List<String> txHashList = new ArrayList<>();
        txHashList.add("0x001e6ec47d0bb4aea28e1f465c96b2663efc4ce0");
        txHashList.add("0x0be92d1dae529852b6a8999c2d3aa2a1705cef54");
        txHashList.add("0x11d1f607681481fa00d8be60949163e69f40069e");
        List<MunziTestDevTransactions> transacionsByTxHashList = munziTestDevTransactionsRepository.getTransacionsByTxHashList(txHashList);
        System.out.println(transacionsByTxHashList);
    }

    @Test
    void getTransactionsByBlockNumber() {
        long blockNumber = 909;
        long start = System.currentTimeMillis();
        Set<String> txHashList = munziTestDevTxnsRepository.getTxHashListByBlockNumber(blockNumber);
        if (txHashList.isEmpty()) {
            System.out.println("empty!!!");
            return;
        }
//        System.out.println(txHashList);

        List<MunziTestDevTransactions> transacionsByTxHashList = munziTestDevTransactionsRepository.getTransacionsByTxHashList(txHashList);
        long end = System.currentTimeMillis();
        System.out.println("=============================================================");
        System.out.println(end - start + "ms 경과");
        System.out.println("=============================================================");
        System.out.println(transacionsByTxHashList);
        System.out.println("=============================================================");

    }

    @Test
    void getTransaction() {
        String txHash = "0x00b7856a591beff965e05aa353c581fb983f5da0";
        long start = System.currentTimeMillis();
        Optional<MunziTestDevTransactions> result = munziTestDevTransactionsRepository.findByTxHash(txHash);
        long end = System.currentTimeMillis();
        System.out.println("=============================================================");
        System.out.println(end - start + "ms 경과");
        System.out.println("=============================================================");
        System.out.println(result.get());
    }

    @Test
    void getBlockListWithNextNextPagination() {
        String lastEvaluatedKey = null;
//        String lastEvaluatedKey = "20240626120925646#b6f0b6a4-581b-435d-b06f-bbc578eb8e53";
        int pageSize = 3;

        // 첫 페이지 가져오기
        QueryResultPage<MunziTestDevBlocks> firstPage = munziTestDevBlocksRepository.queryWithPagination(lastEvaluatedKey, null, pageSize);
        List<MunziTestDevBlocks> results = firstPage.getResults();
        System.out.println("=====================================================================");
        System.out.println("result!!!!!");
        System.out.println("=====================================================================");
        System.out.println(results);

        Map<String, AttributeValue> lastEvaluatedKeyMap = firstPage.getLastEvaluatedKey();

        if (lastEvaluatedKeyMap == null || lastEvaluatedKeyMap.isEmpty()) {
            System.out.println("=============================================================");
            System.out.println("end");
            System.out.println("=============================================================");
            return;
        }

        // 다음 페이지 가져오기
        QueryResultPage<MunziTestDevBlocks> secondPage = munziTestDevBlocksRepository.queryWithPagination(lastEvaluatedKeyMap, pageSize);
        List<MunziTestDevBlocks> secondPageResults = secondPage.getResults();
        System.out.println("=====================================================================");
        System.out.println("secondPageResults!!!!!");
        System.out.println("=====================================================================");
        System.out.println(secondPageResults);

    }

    @Test
    void getBlockListWithPagination() {
        long maxBlockNumber = 1000;
        int page = 82;
        int size = 10;

        long start = System.currentTimeMillis();

        QueryResultPage<MunziTestDevBlocks> firstPage = munziTestDevBlocksRepository.queryWithPagination(maxBlockNumber, page, size);
        long end = System.currentTimeMillis();
        System.out.println("=============================================================");
        System.out.println(end - start + "ms 경과");
        List<MunziTestDevBlocks> results = firstPage.getResults();
        System.out.println("=====================================================================");
        System.out.println("result!!!!!");
        System.out.println("=====================================================================");
        System.out.println(results);
    }

    @Test
    void getTransactionListWithNextNextPagination() {
        String lastEvaluatedKey = null;
//        String lastEvaluatedKey = "20240626120925646#b6f0b6a4-581b-435d-b06f-bbc578eb8e53";
        int pageSize = 3;

        long start = System.currentTimeMillis();
        // 첫 페이지 가져오기
        QueryResultPage<MunziTestDevTransactions> firstPage = munziTestDevTransactionsRepository.queryWithPagination(lastEvaluatedKey, null, pageSize);
        List<MunziTestDevTransactions> results = firstPage.getResults();
        System.out.println("=====================================================================");
        System.out.println("result!!!!!");
        System.out.println("=====================================================================");
        System.out.println(results);

        Map<String, AttributeValue> lastEvaluatedKeyMap = firstPage.getLastEvaluatedKey();

        if (lastEvaluatedKeyMap == null || lastEvaluatedKeyMap.isEmpty()) {
            System.out.println("=============================================================");
            System.out.println("end");
            System.out.println("=============================================================");
            return;
        }

        // 다음 페이지 가져오기
        QueryResultPage<MunziTestDevTransactions> secondPage = munziTestDevTransactionsRepository.queryWithPagination(lastEvaluatedKeyMap, pageSize);

        long end = System.currentTimeMillis();
        System.out.println("=============================================================");
        System.out.println(end - start + "ms 경과");

        List<MunziTestDevTransactions> secondPageResults = secondPage.getResults();
        System.out.println("=====================================================================");
        System.out.println("secondPageResults!!!!!");
        System.out.println("=====================================================================");
        System.out.println(secondPageResults);

    }

    @Test
    void getTransactionListWithPaging() {
        int maxTxNumber = 16450; // 현재 최대 블록 번호
        int size = 10;
        int page = 2;

        long from = maxTxNumber - size * page + 1;
        long to = maxTxNumber - size * (page - 1);
        List<MunziTestDevBlocks> blocks = munziTestDevBlocksRepository.getBlocksByCurrentTxSumBetween(from, to);
        if (blocks.isEmpty()) {
            System.out.println("empty!");
            return;
        }

        if (blocks.get(blocks.size() - 1).testGetCurrentTxSum() != to) {
            Optional<MunziTestDevBlocks> topBlock = munziTestDevBlocksRepository.findByBlockNumber(blocks.get(blocks.size() - 1).getBlockNumber() + 1);
            if (topBlock.isPresent()) {
                List<MunziTestDevBlocks> tmpList = new ArrayList<>();
                tmpList.add(topBlock.get());
                tmpList.addAll(blocks);
                blocks = new ArrayList<>(tmpList);
            }
        }

        List<MunziTestDevTransactions> transacions = munziTestDevTransactionsRepository.getTransacionsByBlockNumberBetween(
                blocks.get(blocks.size() - 1).getBlockNumber(), blocks.get(0).getBlockNumber());
        List<MunziTestDevTransactions> sortedTransactions = transacions.stream()
                .sorted(Comparator.comparing(MunziTestDevTransactions::testGetBlockNumber, Comparator.reverseOrder())
                        .thenComparing(MunziTestDevTransactions::getBlockTxNo, Comparator.reverseOrder()))
                .toList();

        int no = 1;
        int remainSize = size;
        Map<Long, Pair<Integer, Integer>> blockNumberFromToMap = new HashMap<>();

        for (MunziTestDevBlocks block : blocks) {
            if (no == blocks.size()) { // last
                if (no == 1) {
                    int tmp = (int) (block.testGetCurrentTxSum() - to);
                    blockNumberFromToMap.put(block.getBlockNumber(), Pair.of((int) (to - from) + 1, block.getTxCount() - tmp));
                } else {

                    blockNumberFromToMap.put(block.getBlockNumber(), Pair.of(block.getTxCount() - remainSize + 1, block.getTxCount()));
                }
            } else {
                int tmp = (int) (block.getTxCount() - block.testGetCurrentTxSum() + to);
                blockNumberFromToMap.put(block.getBlockNumber(), Pair.of(1, tmp));
                to -= tmp;
                remainSize -= tmp;
            }

            no++;
        }

        List<MunziTestDevTransactions> result = new ArrayList<>();
        for (MunziTestDevTransactions transaction : sortedTransactions) {
            Pair<Integer, Integer> fromTo = blockNumberFromToMap.get(transaction.testGetBlockNumber());
            if (transaction.getBlockTxNo() >= fromTo.getLeft() && transaction.getBlockTxNo() <= fromTo.getRight()) {
                result.add(transaction);
            }
        }

        result.forEach(System.out::println);
    }
}
