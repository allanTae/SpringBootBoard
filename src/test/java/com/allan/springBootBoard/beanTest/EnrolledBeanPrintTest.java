package com.allan.springBootBoard.beanTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EnrolledBeanPrintTest {

    @Autowired
    DefaultListableBeanFactory df;

    @Test
    public void 등록_빈_출력() throws Exception {
        //given

        //when
        System.out.println("==================빈 등록 정보 출력=======================");
        //then
        for (String name: df.getBeanDefinitionNames()) {
            System.out.println(name + " \t" + df.getBean(name).getClass().getName());
        }
    }
}
