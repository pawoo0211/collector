package com.techblog.api.controller;

import com.techblog.api.mock.in.CollectPostIn;
import com.techblog.api.mock.in.UpdatePostIn;
import com.techblog.api.mock.out.CollectPostOut;
import com.techblog.api.mock.out.DeletePostOut;
import com.techblog.api.mock.out.SearchPostOut;
import com.techblog.api.mock.out.UpdatePostOut;
import com.techblog.common.CommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock")
public class MockController {

    @PostMapping("/collect/{company}/post/{id}")
    public CommonResponse collectPost(@PathVariable("company") String company, @PathVariable("id") String id,
                                      @RequestBody CollectPostIn collectPostIn) {
        return CommonResponse.ok(new CollectPostOut(id));
    }

    @PutMapping("/collect/{company}/post/{id}")
    public CommonResponse updateCollectedPost(@PathVariable("company") String company, @PathVariable("id") String id,
                                              @RequestBody UpdatePostIn updatePostIn) {
        return CommonResponse.ok(new UpdatePostOut(id));
    }

    @DeleteMapping("/collect/{company}/post/{id}")
    public CommonResponse deleteCollectedPost(@PathVariable("company") String company, @PathVariable("id") String id) {
        return CommonResponse.ok(new DeletePostOut(id));
    }

    @GetMapping("/search/{keyword}")
    public CommonResponse searchPost(@PathVariable("keyword") String keyword) {
        return CommonResponse.ok(new SearchPostOut("https://d2.naver.com/home"));
    }
}