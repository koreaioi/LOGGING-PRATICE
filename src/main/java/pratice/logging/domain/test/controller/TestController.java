package pratice.logging.domain.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class TestController {

    @RequestMapping(value = "/api/test", method = RequestMethod.GET)
    public ResponseEntity<String> loggingTest() {

        log.info("create test log");
        String uuid = UUID.randomUUID().toString();
        log.info("created uuid {}", uuid);

        return ResponseEntity.ok("we created test log");
    }

}
