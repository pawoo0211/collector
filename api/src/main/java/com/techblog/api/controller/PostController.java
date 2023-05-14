package com.techblog.api.controller;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.SearchPostOut;
import com.techblog.common.CommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @PostMapping("/collect")
    public CommonResponse collectPost(@RequestBody CollectPostIn collectPostIn) {
        return CommonResponse.ok(new CollectPostOut("1"));
    }

    @GetMapping("/search")
    public CommonResponse searchPost(@RequestParam("keyword") String keyword) {
        return CommonResponse.ok(new SearchPostOut("https://d2.naver.com/home"));
    }
}