package com.example.forums_backend.api;

import com.example.forums_backend.dto.BookmarkReqDto;
import com.example.forums_backend.dto.CommentReqDto;
import com.example.forums_backend.dto.VoteDto;
import com.example.forums_backend.dto.VoteRequestDto;
import com.example.forums_backend.entity.my_enum.Subject;
import com.example.forums_backend.entity.my_enum.VoteType;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.service.BookmarkService;
import com.example.forums_backend.service.CommentService;
import com.example.forums_backend.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.forums_backend.config.constant.route.ClientRoute.*;

@RestController
@RequestMapping(PREFIX_CLIENT_ROUTE)
@RequiredArgsConstructor
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    VoteService voteService;
    @Autowired
    BookmarkService bookmarkService;

    @RequestMapping(value = FIND_COMMENTS_BY_POST_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> findCommentsByPostId(@PathVariable Long post_id) {
        return ResponseEntity.ok(commentService.findCommentByPost_Id(post_id));
    }
    @RequestMapping(value = MY_COMMENTS_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> myComments(@RequestParam(name = "limit", defaultValue = "5") int limit){
        return ResponseEntity.ok(commentService.myComments(limit));
    }

    @RequestMapping(value = USER_COMMENTS_PATH, method = RequestMethod.GET)

    public ResponseEntity<?> userComment(@PathVariable String username){
        return ResponseEntity.ok(commentService.userComments(username));
    }
    @RequestMapping(value = COMMENT_POST_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> commentPost(@PathVariable Long id, @RequestBody CommentReqDto commentReqDto) {
        try {
            return ResponseEntity.ok(commentService.saveComment(id, commentReqDto));
        } catch (Exception exception) {
            return ResponseEntity.status(400).body("Hệ thống gặp vấn đề");
        }
    }
    @RequestMapping(value = COMMENT_VOTE_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> voteComment(@PathVariable Long id, @RequestBody VoteRequestDto voteRequestDto) {
        try {
            VoteDto voteDto = new VoteDto();
            voteDto.setSubject_id(id);
            System.out.println(voteRequestDto.getType());
            voteDto.setSubject(Subject.COMMENT);
            voteDto.setType(VoteType.getVoteType(voteRequestDto.getType()));
            return ResponseEntity.ok(voteService.vote(voteDto));
        } catch (Exception exception) {
            return ResponseEntity.status(400).body("Hệ thống gặp vấn đề");
        }
    }
    @RequestMapping(value = "/comment/{id}/bookmark", method = RequestMethod.POST)
    public ResponseEntity<?> commentBookmark(@PathVariable Long id) throws AppException {
        BookmarkReqDto bookmarkReqDto = new BookmarkReqDto();
        bookmarkReqDto.setSubject_id(id);
        bookmarkReqDto.setSubject(Subject.COMMENT);
        return ResponseEntity.status(200).body(bookmarkService.Bookmark(bookmarkReqDto));
    }
    @RequestMapping(value = DETAILS_COMMENT_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> findCommentById(@PathVariable Long id) throws AppException {
        return ResponseEntity.ok(commentService.findByIdToDto(id));
    }
    @RequestMapping(value = DELETE_MY_COMMENT, method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMyComment(@PathVariable Long id) throws AppException {
        return ResponseEntity.ok(commentService.deleteMyComment(id));
    }
    @RequestMapping(value = UPDATE_MY_COMMENT, method = RequestMethod.PUT)
    public ResponseEntity<?> updateMyComment(@PathVariable Long id,@RequestBody CommentReqDto commentReqDto)throws AppException{
        return ResponseEntity.ok(commentService.updateMyComment(id,commentReqDto));
    }
}
