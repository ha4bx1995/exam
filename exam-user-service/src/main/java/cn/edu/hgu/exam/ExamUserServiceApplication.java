package cn.edu.hgu.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "cn.edu.hgu.exam.user.mapper")
public class ExamUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamUserServiceApplication.class, args);
	}

}
