package org.sopt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication 안에 @ComponentScan이 포함됨
// 이 어노테이션이 붙은 클래스(Main)의 하위 패키지를 모두 스캔해서 Bean으로 등록
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}