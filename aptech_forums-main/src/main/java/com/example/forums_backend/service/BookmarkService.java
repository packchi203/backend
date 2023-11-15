package com.example.forums_backend.service;

import com.example.forums_backend.dto.BookmarkReqDto;
import com.example.forums_backend.dto.BookmarkResDto;
import com.example.forums_backend.dto.CommentResDto;
import com.example.forums_backend.dto.PostResDto;
import com.example.forums_backend.entity.*;
import com.example.forums_backend.entity.my_enum.Subject;
import com.example.forums_backend.entity.my_enum.VoteType;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    AccountService accountService;


    public List<BookmarkResDto> BookmarkList() {
        Account account = accountService.getUserInfoData();
        List<Bookmark> bookmarkList = bookmarkRepository.findAllByAccount_Id(account.getId());
        return bookmarkList.stream().distinct().map(this::toDto).collect(Collectors.toList());
    }

    public boolean Bookmark(BookmarkReqDto bookmarkReqDto) throws AppException {
        Account author = accountService.getUserInfoData();
        if (bookmarkReqDto.getSubject() == Subject.POST) {
            PostResDto postResDto = postBookmark(bookmarkReqDto.getSubject_id(), author);
            return postResDto.isBookmark();
        } else if (bookmarkReqDto.getSubject() == Subject.COMMENT) {
            CommentResDto commentResDto = commentBookmark(bookmarkReqDto.getSubject_id(), author);
            return commentResDto.isBookmark();
        }
        return false;
    }

    public PostResDto postBookmark(Long postId, Account account) throws AppException {
        Post post = postService.findByID(postId);
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findFirstByPost_IdAndAccount_Id(post.getId(), account.getId());
        Bookmark bookmark = new Bookmark();
        if (bookmarkOptional.isPresent()) {
            Bookmark bookmarkGet = bookmarkOptional.get();
            delete(bookmarkGet.getId());
            return postService.detailsPost(post.getSlug());
        }
        bookmark.setPost(post);
        bookmark.setAccount(account);
        bookmark.setSubject(Subject.POST);
        bookmarkRepository.save(bookmark);
        return postService.detailsPost(post.getSlug());
    }

    public CommentResDto commentBookmark(Long commentId, Account account) throws AppException {
        Comment comment = commentService.findById(commentId);
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findFirstByComment_IdAndAccount_Id(comment.getId(), account.getId());
        Bookmark bookmark = new Bookmark();
        if (bookmarkOptional.isPresent()) {
            Bookmark bookmarkGet = bookmarkOptional.get();
            delete(bookmarkGet.getId());
            return commentService.fromEntityCommentDto(comment, account);
        }
        bookmark.setComment(comment);
        bookmark.setAccount(account);
        bookmark.setSubject(Subject.COMMENT);
        bookmarkRepository.save(bookmark);
        return commentService.fromEntityCommentDto(comment, account);
    }
    public BookmarkResDto toDto(Bookmark bookmark){
        String content = null;
        String url_redirect = null;
        if(bookmark.getSubject().equals(Subject.COMMENT)){
            content = bookmark.getComment().getContent();
            url_redirect = "/bai-dang/".concat(bookmark.getComment().getPost().getSlug()+"?to_comment=comment-"+bookmark.getComment().getId());
        }else {
            content = bookmark.getPost().getTitle();
            url_redirect = "/bai-dang/".concat(bookmark.getPost().getSlug());
        }
        return BookmarkResDto.builder()
                .id(bookmark.getId())
                .subject(bookmark.getSubject())
                .content(content)
                .createdAt(bookmark.getCreatedAt())
                .url_redirect(url_redirect)
                .build();
    }
    public void delete(Long id) {
        bookmarkRepository.deleteById(id);
    }
}
