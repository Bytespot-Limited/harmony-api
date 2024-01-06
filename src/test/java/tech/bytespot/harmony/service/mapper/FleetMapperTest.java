package tech.bytespot.harmony.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class FleetMapperTest {

    private FleetMapper fleetMapper;

    @BeforeEach
    public void setUp() {
        fleetMapper = new FleetMapperImpl();
    }
}
