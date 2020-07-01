package ru.textanalysis.wordslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.jmorfsdk.loader.JMorfSdkFactory;
import ru.textanalysis.tawt.ms.external.sp.BearingPhraseExt;
import ru.textanalysis.tawt.ms.internal.ref.RefOmoForm;
import ru.textanalysis.tawt.ms.internal.sp.BearingPhraseSP;
import ru.textanalysis.tawt.ms.storage.OmoFormList;
import ru.textanalysis.tawt.sp.api.SyntaxParser;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@EnableSwagger2
@SpringBootApplication
public class WordsListApplication {

    @Bean
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    public static void main(String[] args) {
//        SyntaxParser sp = new SyntaxParser();
//        sp.init();
//        List<BearingPhraseSP> phrase
//                = sp.getTreeSentence("Стало ясно, что будет с российской валютой.");
//        phrase.forEach(System.out::println);
//
//        List<BearingPhraseExt> phrase1
//                = sp.getTreeSentenceWithoutAmbiguity("Стало ясно, что будет с российской валютой.");
//        phrase1 .forEach(System.out::println);

        JMorfSdk jMorfSdk = JMorfSdkFactory.loadFullLibrary();
        OmoFormList omoForms = jMorfSdk.getAllCharacteristicsOfForm("союз");

        SpringApplication.run(WordsListApplication.class, args);
    }

}
