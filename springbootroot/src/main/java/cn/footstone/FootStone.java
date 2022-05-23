package cn.footstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @description: 启动类
 * @author: liuyan
 * @create: 2022−05-18 10:51 AM
 */
@ServletComponentScan
@SpringBootApplication
public class FootStone {


    public static void main(String[] args) {
        SpringApplication.run(FootStone.class, args);
    }


}
