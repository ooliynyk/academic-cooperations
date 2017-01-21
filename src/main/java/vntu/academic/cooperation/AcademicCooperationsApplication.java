package vntu.academic.cooperation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AcademicCooperationsApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AcademicCooperationsApplication.class)
			.profiles("dev")
			.run(args);
	}

}
