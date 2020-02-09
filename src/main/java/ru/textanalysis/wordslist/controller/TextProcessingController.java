package ru.textanalysis.wordslist.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.textanalysis.wordslist.dto.filters.TextWithFilters;
import ru.textanalysis.wordslist.dto.payload.AllWordsListResponse;
import ru.textanalysis.wordslist.dto.payload.WordsListResponse;
import ru.textanalysis.wordslist.service.MorfologyService;
import ru.textanalysis.wordslist.service.TextProcessingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/process")
@AllArgsConstructor
public class TextProcessingController {

    private final TextProcessingService textProcessingService;
    private final MorfologyService morfologyService;

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

    @PostMapping("/allwords/filter")
    public AllWordsListResponse getAllWords(@RequestBody TextWithFilters textWithFilters) {
        List<String> words = textProcessingService.getAllWordList(textWithFilters.getText(),
                textWithFilters.getTypesOfSpeech(),
                textWithFilters.isSimpleMode());
        return new AllWordsListResponse(words, (long) words.size());
    }

    @PostMapping("/words")
    public WordsListResponse getWords(@RequestBody String text) {
        Map<String, Long> words = textProcessingService.getWordList(text);
        return new WordsListResponse(words, (long) words.size());
    }

    @PostMapping("/words/filter")
    public WordsListResponse getWords(@RequestBody TextWithFilters textWithFilters) {
        Map<String, Long> words = textProcessingService.getWordList(textWithFilters.getText(),
                                                                    textWithFilters.getTypesOfSpeech(),
                                                                    textWithFilters.isSimpleMode());
        return new WordsListResponse(words, (long) words.size());
    }

    @GetMapping("/typesOfSpeech")
    public Map<Byte, String> getTypesOfSpeech() {
        return morfologyService.getTypesOfSpeech();
    }
}
