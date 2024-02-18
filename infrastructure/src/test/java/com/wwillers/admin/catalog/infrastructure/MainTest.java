package com.wwillers.admin.catalog.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void main() {
        assertNotNull(new Main());
        Main.main(new String[] {});
    }
}