package com.example.riraproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RiraProjectApplicationTest {

    @Test
    void applicationShouldStart() {
        Assertions.assertDoesNotThrow(() -> RiraProjectApplication.main(new String[]{}));
    }
}
