package ru.textanalysis.wordslist.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class WordsListResponse {
    private Map<String, Long> words;
    private Long count;
}
