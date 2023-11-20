package com.example.forums_backend.api.admin;

import com.example.forums_backend.dto.BookmarkReqDto;
import com.example.forums_backend.dto.PostRequestDto;
import com.example.forums_backend.dto.VoteDto;
import com.example.forums_backend.dto.VoteRequestDto;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Post;
import com.example.forums_backend.entity.Tag;
import com.example.forums_backend.entity.my_enum.SortPost;
import com.example.forums_backend.entity.my_enum.Subject;
import com.example.forums_backend.entity.my_enum.VoteType;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.PostRepository;
import com.example.forums_backend.service.BookmarkService;
import com.example.forums_backend.service.PostService;
import com.example.forums_backend.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.forums_backend.config.constant.route.AdminRoute.*;

@RestController
@RequestMapping(PREFIX_ADMIN_ROUTE)
@RequiredArgsConstructor
public class AdminPostController {
    @Autowired
    PostService postService;
//    @RequestMapping(value = POST_PATH, method = RequestMethod.GET)
//    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "none") SortPost sort){
//        return ResponseEntity.ok(repository.findAll());
//    }

    @RequestMapping(value = POST_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.ok(postService.savePost(postRequestDto));
    }

    @RequestMapping(value = POST_PATH_WITH_ID, produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestBody Post post, @PathVariable Long id) {
        return ResponseEntity.ok("Updated successfully");
    }

    @RequestMapping(value = POST_PATH_WITH_ID, method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.ok("Deleted");
    }

//    @RequestMapping(value = POST_PATH, method = RequestMethod.GET)
//    public Page<Post> findPage(@RequestParam int page, @RequestParam int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return repository.findAll(pageRequest);
//    }
}