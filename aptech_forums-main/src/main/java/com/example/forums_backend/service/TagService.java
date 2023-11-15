package com.example.forums_backend.service;

import com.example.forums_backend.dto.PostResDto;
import com.example.forums_backend.dto.TagFollowReqDto;
import com.example.forums_backend.dto.TagFollowResDto;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Post;
import com.example.forums_backend.entity.Tag;
import com.example.forums_backend.entity.TagFollowing;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.TagFollowingRepository;
import com.example.forums_backend.repository.TagRepository;
import com.example.forums_backend.utils.SlugGenerating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.TagUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TagService {
    @Autowired
    AccountService accountService;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagFollowingRepository tagFollowingRepository;

    public List<TagFollowResDto> findAll() {
        Account account = accountService.getUserInfoData();
        List<Tag> tags = tagRepository.findTagsPopular();
        return tags.stream().map(it -> fromEntityTagDto(it, account)).collect(Collectors.toList());
    }
    public List<TagFollowResDto> tagsPopular(){
        Account account = accountService.getUserInfoData();
        List<Tag> tagsPopular = tagRepository.findTagsPopular();
        return tagsPopular.stream().limit(5).map(it -> fromEntityTagDto(it, account)).collect(Collectors.toList());
    }

    public Tag save(Tag tag) {
        Tag tagSave = new Tag();
        String slugGenerate = SlugGenerating.toSlug(tag.getName());
        tagSave.setName(tag.getName());
        tagSave.setIcon(tag.getIcon());
        tagSave.setColor_bg(tag.getColor_bg());
        tagSave.setImportant(tag.isImportant());
        tagSave.setDescription(tag.getDescription());
        tagSave.setSlug(slugGenerate);
        return tagRepository.save(tagSave);
    }

    public TagFollowResDto followTag(TagFollowReqDto tagFollowReqDto) throws AppException {
        Account account = accountService.getUserInfoData();
        Optional<Tag> tagOptional = tagRepository.findById(tagFollowReqDto.getTag().getId());
        if (!tagOptional.isPresent()) throw new AppException("TAG NOT FOUND!");
        Tag tag = tagOptional.get();
        Optional<TagFollowing> tagFollowingOptional = tagFollowingRepository.findFirstByTag_IdAndAccount_Id(tagFollowReqDto.getTag().getId(), account.getId());
        if (tagFollowingOptional.isPresent()) {
            TagFollowing tagFollowing = tagFollowingOptional.get();
            tagFollowingRepository.deleteById(tagFollowing.getId());
            tag.setFollow_count(tag.getFollow_count() - 1);
            return fromEntityTagDto(tag, account);
        }
        TagFollowing tagFollowing = new TagFollowing();
        tagFollowing.setAccount(account);
        tagFollowing.setTag(tagFollowReqDto.getTag());
        tag.setFollow_count(tag.getFollow_count() + 1);
        tagFollowingRepository.save(tagFollowing);
        return fromEntityTagDto(tag, account);
    }

    public List<Tag> myTagFollowing() {
        Account account = accountService.getUserInfoData();
        if(account != null) {
            List<TagFollowing> tagFollowing = tagFollowingRepository.findByAccount_Id(account.getId());
            return tagFollowing.stream().map(this::fromTagFollowingToTag).collect(Collectors.toList());
        }
        return new ArrayList<Tag>();
    }

    public TagFollowResDto fromEntityTagDto(Tag tag, Account currentUser) {
        TagFollowing tagFollowingOptional = null;
        if (currentUser != null) {
            tagFollowingOptional = tagFollowingRepository.findFirstByTag_IdAndAccount_Id(tag.getId(), currentUser.getId()).orElse(null);
        }
        TagFollowResDto tagFollowResDto = new TagFollowResDto();
        tagFollowResDto.setId(tag.getId());
        tagFollowResDto.setName(tag.getName());
        tagFollowResDto.setSlug(tag.getSlug());
        tagFollowResDto.setIcon(tag.getIcon());
        tagFollowResDto.setTag_follow_count(tag.getFollow_count());
        tagFollowResDto.setFollow(tagFollowingOptional != null);
        tagFollowResDto.setPosts_use(tag.getPosts().size());
        tagFollowResDto.setDesc(tag.getDescription());
        tagFollowResDto.setColor_bg(tag.getColor_bg());
        tagFollowResDto.setImportant(tagFollowResDto.isImportant());
        return tagFollowResDto;
    }

    public Tag fromTagFollowingToTag(TagFollowing tagFollowing){
        return tagFollowing.getTag();
    }

    public Tag update(Tag tag, Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        Tag tagModal = optionalTag.get();
        tagModal.setName(tag.getName());
        return tagRepository.save(tagModal);
    }

    public List<Tag> convertTagsFromString (String[] tagsLug){
        List<Tag> tags = new ArrayList<>();
        for (String item : tagsLug) {
            Optional<Tag> tagOptional = tagRepository.findFirstBySlug(item);
            tags.add(tagOptional.get());
        }
        return tags;
    }
    public void delete(Long id){
        tagRepository.deleteById(id);
    }
}
