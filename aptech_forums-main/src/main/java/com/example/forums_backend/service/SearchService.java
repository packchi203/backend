package com.example.forums_backend.service;

import com.example.forums_backend.dto.*;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Post;
import com.example.forums_backend.entity.Tag;
import com.example.forums_backend.entity.my_enum.SortPost;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.repository.PostRepository;
import com.example.forums_backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostService postService;
    @Autowired
    AccountService accountService;
    @Autowired
    TagService tagService;
    private Object Collection;

    public TagFollowResDto findTagDetails(String slug) throws AppException {
        Optional<Tag> tagOptional = tagRepository.findFirstBySlug(slug);
        Account currentUser = accountService.getUserInfoData();
        if (!tagOptional.isPresent()) {
            throw new AppException("TAG NOT FOUND");
        }
        Tag tag = tagOptional.get();
        return tagService.fromEntityTagDto(tag, currentUser);
    }

    public Page<PostResDto> filterPostByTag(String slug, SortPost sortPost, Pageable pageable, String tags) throws AppException {
        Optional<Tag> tagOptional = tagRepository.findFirstBySlug(slug);
        if (!tagOptional.isPresent()) {
            throw new AppException("TAG NOT FOUND");
        }
        String[] tagsArray;
        if (!tags.isEmpty()) {
            tagsArray = tags.split("\\,");
        } else {
            tagsArray = null;
        }
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        Account currentUser = accountService.getUserInfoData();
        Tag tag = tagOptional.get();
        List<Post> postList = null;
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        if (sortPost.equals(SortPost.hot)) {
            postList = postRepository.findAllPopular().stream().filter(it -> it.getTags().containsAll(tagList)).collect(Collectors.toList());
        } else {
            postList = postRepository.findByTagsIn(tagList, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        List<PostResDto> dtoList = postList.stream()
                .distinct()
                .map(it -> postService.fromEntityPostDto(it, currentUser))
                .collect(Collectors.toList());

        List<PostResDto> postListData;
        if (dtoList.size() < startItem) {
            postListData = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, dtoList.size());
            postListData = dtoList.subList(startItem, toIndex);
        }
        return new PageImpl<PostResDto>(postListData,
                PageRequest.of(currentPage, pageSize), dtoList.size());
    }

    public List<?> searchByKeyword(String keyword, String type, int limit) {
        if (Objects.equals(type, "post")) {
            return searchPost(keyword, limit);
        } else if (Objects.equals(type, "account")) {
            return searchAccount(keyword, limit);
        } else if (Objects.equals(type, "tag")) {
            return searchTag(keyword, limit);
        }
        return null;
    }

    public List<PostResDto> searchPost(String keyword, int limit) {
        Account currentUser = accountService.getUserInfoData();
        List<Post> postListSearch = postRepository.searchAllPopular(keyword);
        return postListSearch.stream().distinct().limit(limit).map(it -> postService.fromEntityPostDto(it, currentUser)).collect(Collectors.toList());
    }

    public List<UserAllInfoDto> searchAccount(String keyword, int limit) {
        List<Account> accountList = new ArrayList<>();
        if (keyword.startsWith("@")) {
            String key = keyword.replace("@", "").trim();
            accountList = accountRepository.searchAccountByUserName(key);
        } else {
            accountList = accountRepository.findByNameContaining(keyword);
        }
        return accountList.stream().distinct().limit(limit).map(this::fromAccountToDto).collect(Collectors.toList());
    }

    public List<TagFollowResDto> searchTag(String keyword, int limit) {
        Account account = accountService.getUserInfoData();
        List<Tag> tagList = tagRepository.searchTag(keyword);
        return tagList.stream().distinct().limit(limit).map(it -> tagService.fromEntityTagDto(it, account)).collect(Collectors.toList());
    }


    public PostSearchDto fromTagFollowingToTag(Post post) {
        PostSearchDto postSearchDto = new PostSearchDto();
        postSearchDto.setTitle(post.getTitle());
        postSearchDto.setSlug(post.getSlug());
        postSearchDto.setComment_count(post.getComment().size());
        postSearchDto.setVote_count(post.getVoteCount());
        postSearchDto.setBookmark_count(post.getBookmarks().size());
        postSearchDto.setCreatedAt(post.getCreatedAt());
        return postSearchDto;
    }

    public UserAllInfoDto fromAccountToDto(Account account) {

        return UserAllInfoDto.builder()
                .name(account.getName())
                .avatar(account.getImageUrl())
                .username(account.getUsername())
                .reputation(account.getReputation())
                .post_count(account.getPosts().size())
                .comment_count(account.getComments().size())
                .tag_flowing_count(account.getTagFollowings().size())
                .badge_count(account.getUserBadge().size())
                .build();
    }
}

