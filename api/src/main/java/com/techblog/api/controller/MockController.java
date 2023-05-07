package com.techblog.api.controller;

import com.techblog.api.mock.out.CollectPostOut;
import com.techblog.common.CommonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockController {

    @PostMapping("/collect/{company}/post/{id}")
    public CommonResponse collectPost(@PathVariable("company") String company, @PathVariable("id") String id) {
        return CommonResponse.ok(new CollectPostOut(id));
    }
}