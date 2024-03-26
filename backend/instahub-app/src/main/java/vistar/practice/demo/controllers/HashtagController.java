package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vistar.practice.demo.dto.HashtagDto;
import vistar.practice.demo.services.HashtagService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    HashtagService hashtagService;
    @GetMapping
    public List<HashtagDto> findAll() {
        return hashtagService.findAll();
    }

    @GetMapping("/{id}")
    public HashtagDto findById(@PathVariable Long id){
        return hashtagService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HashtagDto create (@Validated @RequestBody HashtagDto hashtagDto) { //validate
        return hashtagService.create(hashtagDto);
    }

    @PutMapping
    public HashtagDto update (@Validated @RequestBody HashtagDto hashtagDto) {
        return hashtagService.update(hashtagDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if(!hashtagService.delete(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
