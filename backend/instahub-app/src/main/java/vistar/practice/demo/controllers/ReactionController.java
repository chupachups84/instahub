package vistar.practice.demo.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vistar.practice.demo.dtos.reaction.ReactionCreateEditDto;
import vistar.practice.demo.dtos.reaction.ReactionReadDto;
import vistar.practice.demo.services.ReactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reactions")
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping()
    public List<ReactionReadDto> findAll () {
        return reactionService.findAll();
    }

    @GetMapping("/{id}")
    public ReactionReadDto findById (@PathVariable Long id) {
        return reactionService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reaction (id: " + id + ") does not exist"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionReadDto create (@Validated @RequestBody ReactionCreateEditDto createEditDto) {
        return reactionService.create(createEditDto);
    }

    @PutMapping("/{id}")
    public ReactionReadDto update (@PathVariable Long id, @Validated @RequestBody ReactionCreateEditDto createEditDto) {
        return reactionService.update(id, createEditDto)
                .orElseThrow(() ->new EntityNotFoundException("Reaction (id: " + id + ") does not exist"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id) {
        if(!reactionService.delete(id))
            throw new EntityNotFoundException("Reaction (id: " + id + ") does not exist");
    }

}
