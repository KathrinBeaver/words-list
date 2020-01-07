package ru.textanalysis.wordslist.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class WordsListResponse {
    private Map<String, Long> words;
    private Long count;
}
