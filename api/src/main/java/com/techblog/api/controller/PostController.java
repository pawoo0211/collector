package com.techblog.api.controller;

import com.techblog.api.post.PostService;
import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.in.SaveUrlIn;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.SaveUrlOut;
import com.techblog.api.post.out.SearchPostOut;
import com.techblog.common.CommonResponse;
import com.techblog.common.constant.ResultCode;
import com.techblog.common.exception.domain.CustomAsyncException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/collect")
    public CommonResponse collectPost(@RequestBody CollectPostIn collectPostIn) {
        try {
            CollectPostOut collectPostOut = postService.collectPost(collectPostIn);

            return CommonResponse.ok(ResultCode.COLLECT_SUCCESS.getMessage(), ResultCode.COLLECT_SUCCESS.getDescription(),
                    collectPostOut);
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomAsyncException();
        }
    }

    @GetMapping("/search")
    public CommonResponse searchPost(@RequestParam("keyword") String keyword) {
        SearchPostOut searchPostOut = postService.search(keyword);

        return CommonResponse.ok("TEMP", "성공", searchPostOut);
    }

    @PostMapping("/url/save")
    public CommonResponse saveUrl(@RequestBody SaveUrlIn saveUrlIn) {
        SaveUrlOut saveUrlOut = postService.saveUrl(saveUrlIn);

        return CommonResponse.ok(ResultCode.SAVE_URL_SUCCESS.getMessage(),
                ResultCode.SAVE_URL_SUCCESS.getDescription(), saveUrlOut);
    }
}