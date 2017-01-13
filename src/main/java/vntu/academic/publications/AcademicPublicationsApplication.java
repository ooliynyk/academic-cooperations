package vntu.academic.publications;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AcademicPublicationsApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AcademicPublicationsApplication.class)
			.profiles("dev")
			.run(args);
	}

}
