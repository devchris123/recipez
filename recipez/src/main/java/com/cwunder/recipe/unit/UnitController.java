package com.cwunder.recipe.unit;

import java.util.*;
import java.util.stream.Collectors;

// Spring
import org.springframework.web.bind.annotation.*;

import com.cwunder.recipe._shared.NotFoundException;

import org.springframework.hateoas.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

// Jakarta
import jakarta.validation.Valid;

@Controller
@RequestMapping("/units")
public class UnitController {

    private final UnitRepository repo;
    private final UnitModelAssembler assembler;

    UnitController(UnitRepository repo, UnitModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE,
            MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE })
    public @ResponseBody CollectionModel<EntityModel<Unit>> listUnits() {
        List<EntityModel<Unit>> recs = repo.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(recs, linkTo(methodOn(UnitController.class).listUnits()).withSelfRel());
    }

    @PostMapping()
    public @ResponseBody ResponseEntity<?> createUnit(@Valid @RequestBody Unit newUnit) {

        EntityModel<Unit> rec = assembler.toModel(repo.save(newUnit));
        return ResponseEntity.created(rec.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(rec);
    }

    @GetMapping("/{id}")
    public @ResponseBody EntityModel<Unit> getUnit(@PathVariable String id) {
        Unit rec = repo.findByPublicId(id).orElseThrow(this::generateNotFoundException);
        return assembler.toModel(rec);
    }

    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateUnit(@Valid @RequestBody Unit newUnit, @PathVariable String id) {
        Unit rec = repo.findByPublicId(id)
                .map(
                        unit -> {
                            unit.setUnit(newUnit.getUnit());
                            return repo.save(unit);
                        })
                .orElseThrow(this::generateNotFoundException);
        return ResponseEntity.ok().body(assembler.toModel(rec));
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteUnit(@PathVariable String id) {
        repo.deleteByPublicId(id);
        return ResponseEntity.noContent().build();
    }

    private NotFoundException generateNotFoundException() {
        return new NotFoundException("Unit");
    }
}
