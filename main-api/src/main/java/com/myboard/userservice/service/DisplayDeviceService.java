package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class DisplayDeviceService {

    @Autowired
    private MBUserDetailsService mbUserDetailsService;

    @Autowired
    private WorkFlow flow;

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UtilService utilService;

    @Autowired
    private TimeslotRepository timeslotRepository;

}
