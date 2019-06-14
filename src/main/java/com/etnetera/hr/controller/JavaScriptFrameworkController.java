package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */
@RestController
public class JavaScriptFrameworkController {

    private final JavaScriptFrameworkRepository repository;

    @Autowired
    public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/frameworks")
    public ResponseEntity<List<JavaScriptFramework>> frameworks() {
        return new ResponseEntity<>(IteratorUtils.toList(repository.findAll().iterator()), HttpStatus.OK);
    }

    @GetMapping(value = "/frameworks/{id}", produces = "application/json")
    public ResponseEntity<JavaScriptFramework> findFrameworkById(@PathVariable("id") Long id) {

        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return repository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/frameworks", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JavaScriptFramework> createFramework(@RequestBody JavaScriptFramework framework) {
        return new ResponseEntity<>(repository.save(framework), HttpStatus.OK);
    }

    @PutMapping(value = "/frameworks/{id}", produces = "application/json")
    public ResponseEntity<JavaScriptFramework> updateFramework(@PathVariable("id") Long id, @RequestBody JavaScriptFramework framework) {

        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return repository.findById(id).map(item -> {
            item.setName(framework.getName());
            item.setHypeLevel(framework.getHypeLevel());
            item.setDeprecationDate(framework.getDeprecationDate());
            item.setVersions(framework.getVersions());
            return new ResponseEntity<>(repository.save(item), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("/frameworks/{id}")
    public ResponseEntity<JavaScriptFramework> deleteFramework(@PathVariable("id") Long id) {

        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/frameworks/search", produces = "application/json")
    public ResponseEntity<List<JavaScriptFramework>> findByName(@PathParam("query") String query) {

        if (query == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return repository.findByNameIgnoreCaseContaining(query)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElse(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));
    }


}
