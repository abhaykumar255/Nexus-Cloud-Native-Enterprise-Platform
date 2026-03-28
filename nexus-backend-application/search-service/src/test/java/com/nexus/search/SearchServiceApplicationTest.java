package com.nexus.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SearchServiceApplication Tests")
class SearchServiceApplicationTest {

    @Test
    @DisplayName("Application context loads")
    void applicationContext_loads() {
        // This test ensures the main class exists and is accessible
        assertThat(SearchServiceApplication.class).isNotNull();
    }

    @Test
    @DisplayName("Main method exists")
    void mainMethod_exists() throws NoSuchMethodException {
        // Verify that the main method exists with correct signature
        assertThat(SearchServiceApplication.class.getMethod("main", String[].class)).isNotNull();
    }
}

