package netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootApplicationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private static final GenericContainer<?> fisrtApp = new GenericContainer<>("devapp:latest").withExposedPorts(8080);
    private static final GenericContainer<?> secondApp = new GenericContainer<>("prodapp:latest").withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        fisrtApp.start();
        secondApp.start();
    }

    @Test
    void devProfileTest() {
        String expected = "Current profile is dev";
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + fisrtApp.getMappedPort(8080), String.class);
        Assertions.assertEquals(expected, entity.getBody());
    }

    @Test
    void prodProfileTest() {
        String expected = "Current profile is production";
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + secondApp.getMappedPort(8081), String.class);
        Assertions.assertEquals(expected, entity.getBody());
    }
}
