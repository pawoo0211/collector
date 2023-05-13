package com.techblog.api.controller;

import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.in.UpdatePostIn;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.DeletePostOut;
import com.techblog.api.post.out.SearchPostOut;
import com.techblog.api.post.out.UpdatePostOut;
import com.techblog.common.CommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("{company}/{id}")
    public CommonResponse collectPost(@PathVariable("company") String company, @PathVariable("id") String id,
                                      @RequestBody CollectPostIn collectPostIn) {
        return CommonResponse.ok(new CollectPostOut(id));
    }

    @PutMapping("/{company}/{id}")
    public CommonResponse updateCollectedPost(@PathVariable("company") String company, @PathVariable("id") String id,
                                              @RequestBody UpdatePostIn updatePostIn) {
        return CommonResponse.ok(new UpdatePostOut(id));
    }

    @DeleteMapping("/{company}/{id}")
    public CommonResponse deleteCollectedPost(@PathVariable("company") String company, @PathVariable("id") String id) {
        return CommonResponse.ok(new DeletePostOut(id));
    }

    @GetMapping("/{keyword}")
    public CommonResponse searchPost(@PathVariable("keyword") String keyword) {
        return CommonResponse.ok(new SearchPostOut("https://d2.naver.com/home"));
    }
}