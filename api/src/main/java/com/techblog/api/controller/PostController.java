package com.techblog.api.controller;

import com.techblog.api.post.PostService;
import com.techblog.api.post.in.CollectPostIn;
import com.techblog.api.post.out.CollectPostOut;
import com.techblog.api.post.out.SearchPostOut;
import com.techblog.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * TODO
     * CollectPostIn에 Company만 넘기기 -> 그 후 List or Enum을 이용해서 url에 접근한 후 진행하기
     * Company도 List 진행하기
     * ENUM - FindBy 이용하기
     * url을 넘겨주면 url에 너무 의존적이라 url이 문제가 생기면 전체 코드가 무너짐
     *
     * CollectPostOut의 checkUpdate는 굳이 필요 없는 필드인듯
     */
    @PostMapping("/collect")
    public CommonResponse collectPost(@RequestBody CollectPostIn collectPostIn) {
        CollectPostOut collectPostOut = postService.collectPost(collectPostIn);

        if (collectPostOut.getCheckUpdate()) {
            return CommonResponse.ok(01, "성공", collectPostOut);
        }

        return CommonResponse.ok(00, "성공", collectPostOut);
    }

    @GetMapping("/search")
    public CommonResponse searchPost(@RequestParam("keyword") String keyword) {
        SearchPostOut searchPostOut = postService.search(keyword);

        return CommonResponse.ok(10, "성공", searchPostOut);
    }
}