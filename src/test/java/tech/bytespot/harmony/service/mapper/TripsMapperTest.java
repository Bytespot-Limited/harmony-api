package tech.bytespot.harmony.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TripsMapperTest {

    private TripsMapper tripsMapper;

    @BeforeEach
    public void setUp() {
        tripsMapper = new TripsMapperImpl();
    }
}
