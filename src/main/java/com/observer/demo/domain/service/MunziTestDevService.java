package com.observer.demo.domain.service;

import com.observer.demo.domain.repository.MunziTestDevRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MunziTestDevService {

    private final MunziTestDevRepository munziTestDevRepository;

}
