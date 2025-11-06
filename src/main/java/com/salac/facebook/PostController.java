package com.salac.facebook;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
//@CrossOrigin(origins = "http://localhost:5173") // Allow React frontend
public class PostController {

    private final PostRepository repository;

    public PostController(PostRepository repository) {
        this.repository = repository;
    }

    // Create new post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest request) {
        Post post = new Post();
        post.setAuthor(request.getAuthor());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        Post saved = repository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get all posts
    @GetMapping
    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    // Get a single post
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    // Update post
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        post.setAuthor(request.getAuthor());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        // modifiedDate will be set by @PreUpdate in the entity
        return repository.save(post);
    }

    // Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
