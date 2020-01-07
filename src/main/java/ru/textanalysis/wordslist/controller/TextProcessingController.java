package ru.textanalysis.wordslist.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.textanalysis.wordslist.payload.AllWordsListResponse;
import ru.textanalysis.wordslist.payload.WordsListResponse;
import ru.textanalysis.wordslist.service.TextProcessingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/process")
@AllArgsConstructor
public class TextProcessingController {

    private final TextProcessingService textProcessingService;

    @GetMapping("/status")
    @ResponseBody
    public String getStatus() {
        return "OK!";
    }

    @PostMapping("/allwords")
    public AllWordsListResponse getAllWords(@RequestBody String text) {
        List<String> words = textProcessingService.getAllWordList(text);
        return new AllWordsListResponse(words, (long) words.size());
    }

    @PostMapping("/words")
    public WordsListResponse getWords(@RequestBody String text) {
        Map<String, Long> words = textProcessingService.getWordList(text);
        return new WordsListResponse(words, (long) words.size());
    }
}
