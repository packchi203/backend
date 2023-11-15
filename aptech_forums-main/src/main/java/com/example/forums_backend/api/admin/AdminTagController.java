package com.example.forums_backend.api.admin;

import com.example.forums_backend.dto.TagFollowReqDto;
import com.example.forums_backend.entity.Post;
import com.example.forums_backend.entity.Tag;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.TagRepository;
import com.example.forums_backend.service.TagService;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.forums_backend.config.constant.route.AdminRoute.*;

@RestController
@RequestMapping(PREFIX_ADMIN_ROUTE)
@RequiredArgsConstructor
public class AdminTagController {
    @Autowired
    TagService tagService;
    @RequestMapping(value = TAG_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(tagService.findAll());
    }
    @RequestMapping(value = TAG_PATH, method = RequestMethod.POST)
    public ResponseEntity<?> createTag(@RequestBody Tag tag){
        return ResponseEntity.ok(tagService.save(tag));
    }
    @RequestMapping(value = TAG_PATH_WITH_ID, produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity<Tag> update(@RequestBody Tag tag, @PathVariable Long id) {
        return ResponseEntity.ok(tagService.update(tag, id));
    }
    @RequestMapping(value = TAG_PATH_WITH_ID, method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id){
        tagService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
//    @RequestMapping(value = TAG_PATH, method = RequestMethod.GET)
//    public Page<Tag> findPage(@RequestParam int page, @RequestParam int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return repository.findAll(pageRequest);
//    }
}
