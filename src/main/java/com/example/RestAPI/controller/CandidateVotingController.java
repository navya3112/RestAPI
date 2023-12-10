package com.example.RestAPI.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.RestAPI.entity.Candidate;

@RestController
public class CandidateVotingController {
	private final Map<String, Candidate> candidates = new ConcurrentHashMap<>();

    @GetMapping("/entercandidate")
    public ResponseEntity<String> enterCandidate(@RequestParam String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Candidate name cannot be empty");
        }

        if (candidates.containsKey(name)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Candidate already exists");
        }

        candidates.put(name, new Candidate(name, 0));
        return ResponseEntity.status(HttpStatus.CREATED).body("Candidate entered successfully");
    }

    @GetMapping("/castvote")
    public ResponseEntity<Object> castVote(@RequestParam String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Candidate name cannot be empty");
        }
        
        if (candidates.containsKey(name)) {
            Candidate candidate = candidates.get(name);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            return ResponseEntity.ok(candidate.getVoteCount());
        } else {
            return ((BodyBuilder) ResponseEntity.notFound()).body("Candidate not found");
        }
    }

    @GetMapping("/countvote") 
    public ResponseEntity<Object> countVote(@RequestParam String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Candidate name cannot be empty");
        }

        if (candidates.containsKey(name)) {
            return ResponseEntity.ok(candidates.get(name).getVoteCount());
        } else {
            return ((BodyBuilder) ResponseEntity.notFound()).body("Candidate not found");
        }
    }

    @GetMapping("/listvote")
    public ResponseEntity<Map<String, Integer>> listVotes() {
        Map<String, Integer> result = new HashMap<>();
        candidates.forEach((name, candidate) -> result.put(name, candidate.getVoteCount()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getwinner")
    public ResponseEntity<String> getWinner() {
        if (candidates.isEmpty()) {
            return ((BodyBuilder) ResponseEntity.notFound()).body("No candidates available");
        }

        Optional<Map.Entry<String, Candidate>> winner = candidates.entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue().getVoteCount()));

        return winner.map(entry -> ResponseEntity.ok(entry.getKey())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
