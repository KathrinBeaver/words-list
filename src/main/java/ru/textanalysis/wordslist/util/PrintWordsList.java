package ru.textanalysis.wordslist.util;

import lombok.extern.java.Log;
import ru.textanalysis.wordslist.model.MorphSentence;
import ru.textanalysis.wordslist.model.SimpleWord;
import ru.textanalysis.wordslist.model.Word;
import ru.textanalysis.wordslist.model.WordMainForm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Log
public class PrintWordsList {

    public void printWordsList(List<Word> words, String fileName, String typeOfSpeech) {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(typeOfSpeech + words.size() + ": \n");

            for (Word word : words) {
                writer.write(word.toString() + "\n");
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    public void printWordsMap(Map<WordMainForm, List<SimpleWord>> words, String fileName, String typeOfSpeech) {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(typeOfSpeech + words.size() + ": \n");

            for (WordMainForm wordMainForm : words.keySet()) {
                writer.write(wordMainForm.toString() + ": \n");

                for(SimpleWord simpleWord : words.get(wordMainForm)) {
                    writer.write(simpleWord.toString() + ": \n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

//    public void printMorphWordsMap(Map<Long, List<Word>> words, String fileName, String typeOfSpeech) {
//        try (Writer writer = new BufferedWriter(
//                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
//            writer.write(typeOfSpeech + words.size() + ": \n");
//
//            for (Long morphKey : words.keySet()) {
//                writer.write("Морфологические характеристики " + morphKey + ": \n");
//                for(Word word : words.get(morphKey)) {
//                    writer.write(word.toString() + ": \n");
//                }
//                writer.write("\n");
//            }
//        } catch (IOException e) {
//            log.severe(e.getMessage());
//        }
//    }

    public void printMorphWordsMap(Map<Long, Map<WordMainForm, List<SimpleWord>>> words, String fileName, String typeOfSpeech) {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(typeOfSpeech + words.size() + ": \n");
            for (Long morphKey : words.keySet()) {
                writer.write("Морфологические характеристики - " + morphKey + ": \n");
                Map<WordMainForm, List<SimpleWord>> similarWords = words.get(morphKey);
                for (WordMainForm wordMainForm : similarWords.keySet()) {
                    writer.write(wordMainForm.toString() + ": \n");

                    for(SimpleWord simpleWord : similarWords.get(wordMainForm)) {
                        writer.write(simpleWord.toString() + ": \n");
                    }
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    public void printMorphSentWordsMap(Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> words, String fileName, String typeOfSpeech) {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(typeOfSpeech + words.size() + ": \n");
            for (MorphSentence morphKey : words.keySet()) {
                writer.write("Предложение - " + morphKey.getSentenceNumber() +
                        " : морфологические характеристики - " + morphKey.getMorphCharateristics() + ": \n");
                Map<WordMainForm, List<SimpleWord>> similarWords = words.get(morphKey);
                for (WordMainForm wordMainForm : similarWords.keySet()) {
                    writer.write(wordMainForm.toString() + ": \n");

                    for(SimpleWord simpleWord : similarWords.get(wordMainForm)) {
                        writer.write(simpleWord.toString() + ": \n");
                    }
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

}
