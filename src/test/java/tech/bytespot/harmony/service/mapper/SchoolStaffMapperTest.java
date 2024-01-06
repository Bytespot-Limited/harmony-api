package tech.bytespot.harmony.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SchoolStaffMapperTest {

    private SchoolStaffMapper schoolStaffMapper;

    @BeforeEach
    public void setUp() {
        schoolStaffMapper = new SchoolStaffMapperImpl();
    }
}
