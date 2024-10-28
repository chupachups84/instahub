package vistar.practice.demo.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.hashtag.HashtagCreateEditDto;
import vistar.practice.demo.dtos.hashtag.HashtagReadDto;
import vistar.practice.demo.services.HashtagService;

import java.util.List;

@RestController
@RequestMapping("api/v1/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;
    @GetMapping
    public List<HashtagReadDto> findAll() {
        return hashtagService.findAll();
    }

    @GetMapping("/{id}")
    public HashtagReadDto findById(@PathVariable Long id){
        return hashtagService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hashtag (id: " + id + ") does not exist"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HashtagReadDto create (@RequestBody HashtagCreateEditDto hashtagCreateEditDto) {
        return hashtagService.create(hashtagCreateEditDto);
    }

    @PutMapping("{id}")
    public HashtagReadDto update (@PathVariable Long id, @RequestBody HashtagCreateEditDto hashtagCreateEditDto) {
        return hashtagService.update(id ,hashtagCreateEditDto)
                .orElseThrow(() -> new EntityNotFoundException("Hashtag (id: " + id + ") does not exist"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if(!hashtagService.delete(id))
            throw new EntityNotFoundException("Hashtag (id: " + id + ") does not exist");
    }
}
