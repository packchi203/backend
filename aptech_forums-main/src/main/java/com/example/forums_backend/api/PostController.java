package com.example.forums_backend.api;

import com.example.forums_backend.dto.*;
import com.example.forums_backend.entity.my_enum.SortPost;
import com.example.forums_backend.entity.my_enum.Subject;
import com.example.forums_backend.entity.my_enum.VoteType;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.PostRepository;
import com.example.forums_backend.service.BookmarkService;
import com.example.forums_backend.service.PostService;
import com.example.forums_backend.service.TagService;
import com.example.forums_backend.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.forums_backend.config.constant.route.ClientRoute.*;

@RestController
@RequestMapping(PREFIX_CLIENT_ROUTE)
@RequiredArgsConstructor
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    VoteService voteService;

    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    TagService tagService;

    @RequestMapping(value = "/posts/popular", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "",required = false) String tags) {
        String[] tagsArray;
        if(!tags.isEmpty()){
            tagsArray = tags.split("\\,");
        } else {
            tagsArray = null;
        }

        return ResponseEntity.ok(postRepository.findAllPopular().stream().filter(it-> it.getTags().containsAll(tagService.convertTagsFromString(tagsArray))).toArray());
    }

    @RequestMapping(value = POSTS_CLIENT_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> findAllPaginate(
            @RequestParam(defaultValue = "", required = false) String tags,
            @RequestParam(defaultValue = "none") SortPost sort,
            @RequestParam(defaultValue = "1") Optional<Integer> page,
            @RequestParam(defaultValue = "5") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(51);

        Page<PostResDto> dtoData = postService.findAllPaginate(sort,
                PageRequest.of(currentPage - 1, pageSize),tags);

        return ResponseEntity.ok(dtoData);
    }

    @RequestMapping(value = POSTS_NOT_SORT_CLIENT_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> getAllNotSort() {
        return ResponseEntity.ok(postService.findAllNotSort());
    }

    @RequestMapping(value = MY_POSTS_CLIENT_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> myPosts() {
        return ResponseEntity.ok(postService.myPosts());
    }

    @RequestMapping(value = USER_POSTS_CLIENT_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> userPost(@PathVariable String username) {
        return ResponseEntity.ok(postService.userPosts(username));
    }

    @RequestMapping(value = POST_CLIENT_DETAILS_POST_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> getDetails(@PathVariable("slug") String slug) throws AppException {
        if (slug.isEmpty()) {
            return ResponseEntity.status(404).body("Param not found");
        }
        return ResponseEntity.ok(postService.detailsPost(slug));
    }
    @RequestMapping(value = "/post/{slug}/static/details", method = RequestMethod.GET)
    public ResponseEntity<?> getDetailsStatic(@PathVariable("slug") String slug) throws AppException {
        if (slug.isEmpty()) {
            return ResponseEntity.status(404).body("Param not found");
        }
        return ResponseEntity.ok(postService.detailsPostStatic(slug));
    }

    @RequestMapping(value = POST_CLIENT_CREATE_POST_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.ok(postService.savePost(postRequestDto));
    }

    @RequestMapping(value = POST_UP_VOTE_CLIENT_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> votePost(@PathVariable Long id, @RequestBody VoteRequestDto voteRequestDto) throws AppException {
        try {
            VoteDto voteDto = new VoteDto();
            voteDto.setSubject_id(id);
            voteDto.setSubject(Subject.POST);
            voteDto.setType(VoteType.getVoteType(voteRequestDto.getType()));
            return ResponseEntity.ok(voteService.vote(voteDto));
        } catch (Exception exception) {
            return ResponseEntity.status(400).body("Hệ thống gặp vấn đề");
        }
    }

    @RequestMapping(value = POST_CLIENT_BOOKMARK_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> postBookmark(@PathVariable Long id) throws AppException {
        BookmarkReqDto bookmarkReqDto = new BookmarkReqDto();
        bookmarkReqDto.setSubject_id(id);
        bookmarkReqDto.setSubject(Subject.POST);
        return ResponseEntity.status(200).body(bookmarkService.Bookmark(bookmarkReqDto));
    }

    @RequestMapping(value = POST_CLIENT_UPDATE_PATH, method = RequestMethod.PUT)
    public ResponseEntity<?> updateMyPost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) throws AppException {
        return ResponseEntity.ok(postService.updateMyPost(id, postRequestDto));
    }

    @RequestMapping(value = POST_CLIENT_DELETE_PATH, method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMyPost(@PathVariable Long id) throws AppException {
        return ResponseEntity.ok(postService.deleteMyPost(id));
    }
}
