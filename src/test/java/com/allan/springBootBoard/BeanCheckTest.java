package com.allan.springBootBoard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Set;

@SpringBootTest
public class BeanCheckTest {

    @Autowired
    DefaultListableBeanFactory df;

    @Test
    public void 빈등록_확인() throws Exception {
        for (String name: df.getBeanDefinitionNames()) {
            System.out.println();
            System.out.println();
            System.out.println("============ 빈 등록 정보 출력 ==========================");
            System.out.println(name + " \t " + df.getBean(name).getClass().getName() );
            if(name.equals("thymeleafViewResolver")){

                System.out.println("ThymeleafViewResolver find!!!");
                System.out.println("***** thymeleafViewResolver ********");
                ThymeleafViewResolver th = (ThymeleafViewResolver) df.getBean(name);
                SpringTemplateEngine engine = (SpringTemplateEngine) th.getTemplateEngine();
//                SpringResourceTemplateResolver resolver = (SpringResourceTemplateResolver) engine.getTemplateResolvers();
                Set<ITemplateResolver> templateResolvers = engine.getTemplateResolvers();
                for (ITemplateResolver templateResolver : templateResolvers) {
                    SpringResourceTemplateResolver resolver = (SpringResourceTemplateResolver) templateResolver;
                    System.out.println();
                    System.out.println("suffix: " + resolver.getSuffix());
                    System.out.println("preffix: " + resolver.getPrefix());
                }
            }else if(name.equals("defaultViewResolver")){
                System.out.println("Inter find!!!");
                System.out.println("***** thymeleafViewResolver ********");
                InternalResourceViewResolver th = (InternalResourceViewResolver) df.getBean(name);
            }
        }
    }
}
