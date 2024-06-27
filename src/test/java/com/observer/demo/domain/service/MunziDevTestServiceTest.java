//package com.observer.demo.domain.service;
//
//import com.observer.demo.domain.table.MunziDevTest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class MunziDevTestServiceTest {
//
//    @Autowired
//    private MunziDevTestService munziDevTestService;
//
//    @Test
//    void findById() {
//        String pk = "";
//        String sk = "";
//        Optional<MunziDevTest> result = munziDevTestService.findById(pk, sk);
//        assertTrue(result.isPresent());
//        MunziDevTest munziDevTest = result.get();
//        munziDevTest.getPk()
//
//    }
//
//    @Test
//    void save() {
//    }
//}