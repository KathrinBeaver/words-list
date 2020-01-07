package ru.textanalysis.wordslist.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllWordsListResponse {
    private List<String> words;
    private Long count;
}
