package cn.edu.hgu.exam.examuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "cn.edu.hgu.exam.examuser.mapper")
public class ExamUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamUserApplication.class, args);
	}

}
