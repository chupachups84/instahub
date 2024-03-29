package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vistar.practice.demo.dto.HashtagCreateEditDto;
import vistar.practice.demo.dto.HashtagReadDto;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HashtagReadDto create (@Validated @RequestBody HashtagCreateEditDto hashtagCreateEditDto) {
        return hashtagService.create(hashtagCreateEditDto);
    }

    @PutMapping("{id}")
    public HashtagReadDto update (@PathVariable Long id, @Validated @RequestBody HashtagCreateEditDto hashtagCreateEditDto) {
        return hashtagService.update(id ,hashtagCreateEditDto)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if(!hashtagService.delete(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}