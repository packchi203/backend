package com.example.forums_backend.service;

import com.example.forums_backend.dto.PostResDto;
import com.example.forums_backend.entity.*;
import com.example.forums_backend.dto.PostRequestDto;
import com.example.forums_backend.entity.my_enum.SortPost;
import com.example.forums_backend.entity.my_enum.StatusEnum;
import com.example.forums_backend.entity.my_enum.VoteType;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.PostRepository;
import com.example.forums_backend.repository.BookmarkRepository;
import com.example.forums_backend.repository.PostViewRepository;
import com.example.forums_backend.repository.VoteRepository;
import com.example.forums_backend.utils.SlugGenerating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    TagService tagService;
    @Autowired
    AccountService accountService;
    CommentService commentService;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    PostViewRepository postViewRepository;

    @Autowired
    public PostService(@Lazy CommentService commentService) {
        this.commentService = commentService;
    }

    public List<PostResDto> findPostsPopular() {
        Account currentUser = accountService.getUserInfoData();
        List<Post> postList = postRepository.findAllPopular();
        return postList.stream().limit(5).map(it -> fromEntityPostDto(it, currentUser)).collect(Collectors.toList());
    }

    public Page<PostResDto> findAllPaginate(SortPost sortPost, Pageable pageable, String tags) {
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
        List<Tag> tagFollowings = tagService.myTagFollowing();
        List<Post> postList = null;
        if (sortPost.equals(SortPost.hot)) {
            if (tags.isEmpty()) {
                postList = postRepository.findAllPopular();
            } else {
                postList = postRepository.findAllPopular().stream().filter(it->it.getTags().containsAll(tagService.convertTagsFromString(tagsArray))).collect(Collectors.toList());
            }
        } else if (sortPost.equals(SortPost.relevant) && !tagFollowings.isEmpty() && currentUser != null) {
            if (tags.isEmpty()) {
                postList = postRepository.findByTagsIn(tagFollowings, Sort.by(Sort.Direction.DESC, "createdAt"));
            } else {
                postList = postRepository.findByTagsIn(tagService.convertTagsFromString(tagsArray), Sort.by(Sort.Direction.DESC, "createdAt"));
            }
        } else {
            if (tags.isEmpty()) {
                postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
            } else {
                postList = postRepository.findByTagsIn(tagService.convertTagsFromString(tagsArray), Sort.by(Sort.Direction.DESC, "createdAt"));
            }

        }
        List<PostResDto> dtoList = postList.stream()
                .distinct()
                .map(it -> fromEntityPostDto(it, currentUser))
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

    public List<PostResDto> findAllNotSort() {
        Account currentUser = accountService.getUserInfoData();
        List<Post> postList = postRepository.findAll();
        return postList.stream()
                .distinct()
                .map(it -> fromEntityPostDto(it, currentUser))
                .collect(Collectors.toList());
    }

    public List<PostResDto> findAllPostByTagFollowing(Account account, SortPost sortPost) {
        List<Tag> tagFollowings = tagService.myTagFollowing();
        if (tagFollowings.isEmpty()) {
            final List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
            return postList.stream()
                    .distinct()
                    .map(it -> fromEntityPostDto(it, account))
                    .collect(Collectors.toList());
        }
        final List<Post> postList = postRepository.findByTagsIn(tagFollowings, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postList.stream()
                .distinct()
                .map(it -> fromEntityPostDto(it, account))
                .collect(Collectors.toList());
    }


    public List<PostResDto> myPosts() {
        Account currentUser = accountService.getUserInfoData();
        List<Post> postList = postRepository.findByAuthor_id(currentUser.getId(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return postList.stream().map(it -> fromEntityPostDto(it, currentUser)).collect(Collectors.toList());
    }

    public List<PostResDto> userPosts(String username) {
        Account currentUser = accountService.getUserInfoData();
        Account account = accountService.findByUsername(username);
        List<Post> postList = postRepository.findByAuthor_id(account.getId(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return postList.stream().map(it -> fromEntityPostDto(it, currentUser)).collect(Collectors.toList());
    }

    public PostResDto savePost(PostRequestDto postRequestDto) {
        Account currentUser = accountService.getUserInfoData();
        Account author = accountService.getUserInfoData();
        Post postSave = new Post();
        String slugGenerate = SlugGenerating.toSlug(postRequestDto.getTitle()).concat("-" + System.currentTimeMillis());
        postSave.setTitle(postRequestDto.getTitle());
        postSave.setSlug(slugGenerate);
        postSave.setContent(postRequestDto.getContent());
        postSave.setTags(postRequestDto.getTags());
        postSave.setAuthor(author);
        postSave.setStatus(StatusEnum.ACTIVE);
        author.setReputation(author.getReputation() + 15);
        postRepository.save(postSave);
        return fromEntityPostDto(postSave, currentUser);
    }

    public PostResDto updateMyPost(Long id, PostRequestDto postRequestDto) throws AppException {
        Account account = accountService.getUserInfoData();
        Optional<Post> postOptional = postRepository.findByIdAndAuthor_Id(id, account.getId());
        if (!postOptional.isPresent()) {
            throw new AppException("POST NOT FOUND!");
        }
        Post post = postOptional.get();
        String slugGenerate = SlugGenerating.toSlug(postRequestDto.getTitle()).concat("-" + System.currentTimeMillis());
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setTags(postRequestDto.getTags());
        post.setSlug(slugGenerate);
        postRepository.save(post);
        return fromEntityPostDto(post, account);
    }

    public boolean deleteMyPost(Long id) throws AppException {
        Account account = accountService.getUserInfoData();
        Optional<Post> postOptional = postRepository.findByIdAndAuthor_Id(id, account.getId());
        if (!postOptional.isPresent()) {
            throw new AppException("POST NOT FOUND!");
        }
        Post post = postOptional.get();
        postRepository.deleteById(post.getId());
        return true;
    }

    public PostResDto detailsPost(String slug) throws AppException {
        try {
            Account currentUser = accountService.getUserInfoData();
            Optional<Post> optionalPost = postRepository.findFirstBySlug(slug);
            if (!optionalPost.isPresent()) {
                throw new AppException("POST NOT FOUND!");
            }
            Post resultPost = optionalPost.get();
            countViewerPost(resultPost, currentUser);
            return fromEntityPostDto(resultPost, currentUser);
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        return null;
    }
    public PostResDto detailsPostStatic(String slug) throws AppException {
        try {
            Account currentUser = accountService.getUserInfoData();
            Optional<Post> optionalPost = postRepository.findFirstBySlug(slug);
            if (!optionalPost.isPresent()) {
                throw new AppException("POST NOT FOUND!");
            }
            Post resultPost = optionalPost.get();
            return fromEntityPostDto(resultPost, currentUser);
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        return null;
    }


    public void deletePost(Long id) {
        postRepository.deleteById(id);

    }

    public Post findByID(Long id) throws AppException {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (!optionalPost.isPresent()) {
            throw new AppException("POST NOT FOUND!");
        }
        return optionalPost.get();
    }

    public PostResDto fromEntityPostDto(Post post, Account currentUser) {
        boolean isMyPost = false;
        Voting voting = null;
        Bookmark bookmark = null;
        PostResDto postResDto = new PostResDto();
        if (currentUser != null) {
            voting = voteRepository.findFirstByPost_IdAndAccount_Id(post.getId(), currentUser.getId()).orElse(null);
            bookmark = bookmarkRepository.findFirstByPost_IdAndAccount_Id(post.getId(), currentUser.getId()).orElse(null);
            isMyPost = Objects.equals(post.getAuthor().getId(), currentUser.getId());
        }
        postResDto.setId(post.getId());
        postResDto.setTitle(post.getTitle());
        postResDto.setSlug(post.getSlug());
        postResDto.setContent(post.getContent());
        postResDto.setAccount(post.getAuthor());
        postResDto.setVoteCount(post.getVoteCount());
        postResDto.setTags(post.getTags());
        postResDto.setCommentCount(post.getComment().size());
        postResDto.setBookmarkCount(post.getBookmarks().size());
        postResDto.setViewCount(post.getPostViews().size());
        postResDto.setVote(voting != null);
        postResDto.setVoteType(voting == null ? VoteType.UNDEFINED : voting.getType());
        postResDto.setBookmark(bookmark != null);
        postResDto.setCreatedAt(post.getCreatedAt());
        postResDto.setMyPost(isMyPost);
        return postResDto;
    }

    public void countViewerPost(Post post, Account account) {
        Optional<PostView> postView = postViewRepository.findFirstByPost_IdAndAccount_Id(post.getId(), account.getId());
        if (!postView.isPresent()) {
            PostView postViewSave = new PostView();
            postViewSave.setPost(post);
            postViewSave.setAccount(account);
            postViewRepository.save(postViewSave);
        }
    }
}
