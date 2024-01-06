package tech.bytespot.harmony.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class StudentTripsMapperTest {

    private StudentTripsMapper studentTripsMapper;

    @BeforeEach
    public void setUp() {
        studentTripsMapper = new StudentTripsMapperImpl();
    }
}
