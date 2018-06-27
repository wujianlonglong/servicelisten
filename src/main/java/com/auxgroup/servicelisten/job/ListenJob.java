package com.auxgroup.servicelisten.job;

import com.auxgroup.servicelisten.service.ListenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ListenJob {

    @Autowired
    ListenService listenService;

    @Scheduled(cron = "0 0/5 * * * *")
    public void tryApplicationState() {
        listenService.tryApplicationState();
    }


}
