package com.example.forums_backend.service;

import com.example.forums_backend.dto.CommentResDto;
import com.example.forums_backend.dto.PostResDto;
import com.example.forums_backend.dto.VoteResDto;
import com.example.forums_backend.entity.*;
import com.example.forums_backend.dto.VoteDto;
import com.example.forums_backend.entity.my_enum.NotificationType;
import com.example.forums_backend.entity.my_enum.Subject;
import com.example.forums_backend.entity.my_enum.VoteType;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.repository.CommentRepository;
import com.example.forums_backend.repository.PostRepository;
import com.example.forums_backend.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class VoteService {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    AccountRepository accountRepository;

    public VoteResDto vote(VoteDto voteDto) throws AppException {
        Account author = accountService.getUserInfoData();
        VoteResDto voteResDto = new VoteResDto();
        if (voteDto.getSubject() == Subject.POST) {
            PostResDto post = postVote(voteDto.getSubject_id(), voteDto.getType(), author);
            voteResDto.setVoteType(post.getVoteType());
            voteResDto.setVote_count(post.getVoteCount());
        } else if (voteDto.getSubject() == Subject.COMMENT) {
            CommentResDto comment = commentVote(voteDto.getSubject_id(), voteDto.getType(), author);
            voteResDto.setVoteType(comment.getVoteType());
            voteResDto.setVote_count(comment.getVoteCount());
        }
        return voteResDto;
    }

    public PostResDto postVote(Long postId, VoteType type, Account account) throws AppException {
        Post post = postService.findByID(postId);
        Account findAuthor = accountService.findByUsername(post.getAuthor().getUsername());
        Optional<Voting> votingOptional = voteRepository.findFirstByPost_IdAndAccount_Id(post.getId(), account.getId());
        Notification notification = new Notification();
        notification.setReceiver(post.getAuthor());
        notification.setInteractive_user(account);
        notification.setRedirect_url("/bai-dang/".concat(post.getSlug()));
        if (!votingOptional.isPresent()) {
            Voting voteSave = new Voting();
            voteSave.setPost(post);
            voteSave.setAccount(account);
            if (type.equals(VoteType.UPVOTE)) {
                notification.setType(NotificationType.UPVOTE);
                voteSave.setType(VoteType.UPVOTE);
                post.setVoteCount(post.getVoteCount() + 1);
                if(!account.equals(post.getAuthor())){
                    findAuthor.setReputation(findAuthor.getReputation() + 3);
                    notificationService.saveNotification(notification);
                }
            } else if (type.equals(VoteType.DOWN_VOTE)) {
                if(!account.equals(post.getAuthor()) && findAuthor.getReputation() >= 3) {
                    findAuthor.setReputation(findAuthor.getReputation() - 3);
                }
                voteSave.setType(VoteType.DOWN_VOTE);
                post.setVoteCount(post.getVoteCount() - 1);
            }
            accountRepository.save(findAuthor);
            voteRepository.save(voteSave);
            postRepository.save(post);
        } else {
            Voting voteExist = votingOptional.get();
            if (type.equals(VoteType.UPVOTE)) {
                if (voteExist.getType().equals(VoteType.UPVOTE)) {
                    if(!account.equals(post.getAuthor()) && findAuthor.getReputation() >= 3 && account.getReputation() >=3) {
                        account.setReputation(account.getReputation() - 3);
                        findAuthor.setReputation(findAuthor.getReputation() + 3);
                    }
                    delete(voteExist.getId());
                    post.setVoteCount(post.getVoteCount() - 1);
                } else if (voteExist.getType().equals(VoteType.DOWN_VOTE)) {
                    if(!account.equals(post.getAuthor())) {
                        findAuthor.setReputation(findAuthor.getReputation() - 3);
                    }
                    voteExist.setType(VoteType.UPVOTE);
                    post.setVoteCount(post.getVoteCount() + 2);
                    voteRepository.save(voteExist);
                }
            } else if (type.equals(VoteType.DOWN_VOTE)) {
                if (voteExist.getType().equals(VoteType.DOWN_VOTE)) {
                    if(!account.equals(post.getAuthor()) && findAuthor.getReputation() >= 3 && account.getReputation() >=3) {
                        account.setReputation(account.getReputation() - 3);
                        findAuthor.setReputation(findAuthor.getReputation() + 3);
                    }
                    delete(voteExist.getId());
                    post.setVoteCount(post.getVoteCount() + 1);
                } else if (voteExist.getType().equals(VoteType.UPVOTE)) {
                    voteExist.setType(VoteType.DOWN_VOTE);
                    if(!account.equals(post.getAuthor()) && findAuthor.getReputation() >= 3) {
                        findAuthor.setReputation(findAuthor.getReputation() - 3);
                    }
                    post.setVoteCount(post.getVoteCount() - 2);
                    voteRepository.save(voteExist);
                }
            }
            accountRepository.save(findAuthor);
            accountRepository.save(account);
            postRepository.save(post);
        }
        return postService.detailsPost(post.getSlug());
    }

    public CommentResDto commentVote(Long commentId, VoteType type, Account account) throws AppException {
        Comment comment = commentService.findById(commentId);
        Account findAuthor = accountService.findByUsername(comment.getAccount().getUsername());
        Optional<Voting> votingOptional = voteRepository.findFirstByComment_IdAndAccount_Id(comment.getId(), account.getId());
        Notification notification = new Notification();
        notification.setReceiver(comment.getAccount());
        notification.setInteractive_user(account);
        notification.setRedirect_url("/bai-dang/".concat(comment.getPost().getSlug()+"?to_comment=comment-"+comment.getId()));
        if (!votingOptional.isPresent()) {
            Voting voteSave = new Voting();
            voteSave.setComment(comment);
            voteSave.setAccount(account);
            if (type.equals(VoteType.UPVOTE)) {
                notification.setType(NotificationType.UPVOTE_COMMENT);
                voteSave.setType(VoteType.UPVOTE);
                comment.setVoteCount(comment.getVoteCount() + 1);
                if(!account.equals(comment.getAccount())){
                    findAuthor.setReputation(findAuthor.getReputation() + 3);
                    notificationService.saveNotification(notification);
                }
            } else if (type.equals(VoteType.DOWN_VOTE)) {
                if(!account.equals(comment.getAccount()) && findAuthor.getReputation() >= 3){
                    findAuthor.setReputation(findAuthor.getReputation() - 3);
                }
                voteSave.setType(VoteType.DOWN_VOTE);
                comment.setVoteCount(comment.getVoteCount() - 1);
            }
            accountRepository.save(findAuthor);
            voteRepository.save(voteSave);
            commentRepository.save(comment);
        } else {
            Voting voteExist = votingOptional.get();
            if (type.equals(VoteType.UPVOTE)) {
                if (voteExist.getType() == VoteType.UPVOTE) {
                    if(!account.equals(comment.getAccount()) && findAuthor.getReputation() >= 3 && account.getReputation() >= 3){
                        account.setReputation(account.getReputation() - 3);
                        findAuthor.setReputation(findAuthor.getReputation() - 3);
                    }
                    delete(voteExist.getId());
                    comment.setVoteCount(comment.getVoteCount() - 1);
                } else if (voteExist.getType().equals(VoteType.DOWN_VOTE)) {
                    if(!account.equals(comment.getAccount())){
                        findAuthor.setReputation(findAuthor.getReputation() + 3);
                    }
                    voteExist.setType(VoteType.UPVOTE);
                    notification.setType(NotificationType.UPVOTE_COMMENT);
                    comment.setVoteCount(comment.getVoteCount() + 2);
                    voteRepository.save(voteExist);
                }
            } else if (type.equals(VoteType.DOWN_VOTE)) {
                if (voteExist.getType().equals(VoteType.DOWN_VOTE)) {
                    if(!account.equals(comment.getAccount()) && findAuthor.getReputation() >= 3 && account.getReputation() >= 3){
                        account.setReputation(account.getReputation() - 3);
                        findAuthor.setReputation(findAuthor.getReputation() + 3);
                    }
                    delete(voteExist.getId());
                    comment.setVoteCount(comment.getVoteCount() + 1);
                } else if (voteExist.getType().equals(VoteType.UPVOTE)) {
                    if(!account.equals(comment.getAccount()) && findAuthor.getReputation() >= 3){
                        findAuthor.setReputation(findAuthor.getReputation() - 3);
                    }
                    voteExist.setType(VoteType.DOWN_VOTE);
                    comment.setVoteCount(comment.getVoteCount() - 2);
                    voteRepository.save(voteExist);
                }
            }
            accountRepository.save(account);
            accountRepository.save(findAuthor);
            commentRepository.save(comment);
        }
        return commentService.fromEntityCommentDto(comment, account);
    }

    public void delete(Long id) {
        voteRepository.deleteById(id);
    }
}
