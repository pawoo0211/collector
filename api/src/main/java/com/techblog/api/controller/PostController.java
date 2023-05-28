package com.techblog.api.controller;

import com.techblog.api.post.PostService;
import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.SearchPostOut;
import com.techblog.common.CommonResponse;
import com.techblog.common.constant.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/collect")
    public CommonResponse collectPost(@RequestBody CollectPostIn collectPostIn) {
        CollectPostOut collectPostOut = postService.collectPost(collectPostIn);

        return CommonResponse.ok(ResultCode.COLLECT_SUCCESS.getResultCode(), ResultCode.COLLECT_SUCCESS.getResultMessage(),
                collectPostOut);
    }

    @GetMapping("/search")
    public CommonResponse searchPost(@RequestParam("keyword") String keyword) {
        SearchPostOut searchPostOut = postService.search(keyword);

        return CommonResponse.ok(10, "성공", searchPostOut);
    }
}