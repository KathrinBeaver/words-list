package ru.textanalysis.wordslist.util;

import lombok.extern.java.Log;
import ru.textanalysis.wordslist.model.MorphSentence;
import ru.textanalysis.wordslist.model.SimpleWord;
import ru.textanalysis.wordslist.model.Word;
import ru.textanalysis.wordslist.model.WordMainForm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    final String WEKA_HEADER = "@relation weka.datagenerators.clusterers.BIRCHCluster-S_1_-a_10_-k_4_-N_1..50_-R_0.1..1.41_-O_-P_0.0\n" +
            "\n" +
            "@attribute X0 string\n" +
            "@attribute X1 numeric\n" +
            "@attribute X2 numeric\n" +
            "@attribute X3 numeric\n" +
            "@attribute X4 numeric\n" +
            "@attribute X5 numeric\n" +
            "@attribute X6 numeric\n" +
            "@attribute X7 numeric\n" +
            "@attribute X8 numeric\n" +
            "@attribute X9 numeric\n" +
            "@attribute X10 numeric\n" +
            "@attribute X11 numeric\n" +
            "@attribute X12 numeric\n" +
            "@attribute X13 numeric\n" +
            "@attribute X14 numeric\n" +
            "@attribute X15 numeric\n" +
            "@attribute X16 numeric\n" +
            "\n" +
            "@data\n";
    public void printMorphSentWordsMap(List<Word> words, String fileName) {
        if (!Files.exists(Paths.get(fileName))) {
            try {
                Files.createFile(Paths.get(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(WEKA_HEADER);
            for(Word word: words) {
                writer.write(word.getWord() + ", " + word.getTypeOfSpeech());
                for (Long morphValue : word.getMorph().values()) {
                    writer.write(", " + morphValue);
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

}
