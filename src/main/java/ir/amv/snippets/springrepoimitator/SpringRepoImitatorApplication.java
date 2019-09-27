package ir.amv.snippets.springrepoimitator;

import ir.amv.snippets.springrepoimitator.mine.MyRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories
@Import(MyRegistrar.class)
public class SpringRepoImitatorApplication {

	@Bean
	public IMyClass myClass() {
		return new IMyClass() {
		};
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringRepoImitatorApplication.class, args);
		IMyClass myClass = applicationContext.getBean(IMyClass.class);
		System.out.println("myClass = " + myClass);
	}

}
