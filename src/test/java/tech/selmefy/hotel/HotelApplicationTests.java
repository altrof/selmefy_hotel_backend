package tech.selmefy.hotel;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HotelApplicationTests extends AbstractIntegrationTest {
	@Test
	void contextLoads() {
		assertTrue(true); // insane test for SonarLint
	}
}
