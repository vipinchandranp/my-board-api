package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.AbstractFilterRequest;
import com.myboard.userservice.controller.model.common.AbstractFilterResponse;
import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Autowired
    private CommonService commonService;

    @PostMapping("/search/list")
    public MainResponse<List<AbstractFilterResponse>> getCommonItems(
            @RequestBody AbstractFilterRequest filterRequest
    ) throws MBException {
        List<AbstractFilterResponse> combinedResponse = commonService.getCommonItems(filterRequest);
        return new MainResponse<>(combinedResponse);
    }
}
