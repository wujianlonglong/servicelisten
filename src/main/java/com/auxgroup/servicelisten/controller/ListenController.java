package com.auxgroup.servicelisten.controller;

import com.auxgroup.servicelisten.service.ListenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListenController {

    @Autowired
    ListenService listenService;

    @RequestMapping(value = "/tryApplicationState", method = RequestMethod.GET)
    public void tryApplicationState() {
        listenService.tryApplicationState();
    }
}
