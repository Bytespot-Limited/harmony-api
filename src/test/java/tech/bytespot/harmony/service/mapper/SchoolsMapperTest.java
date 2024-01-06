package tech.bytespot.harmony.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SchoolsMapperTest {

    private SchoolsMapper schoolsMapper;

    @BeforeEach
    public void setUp() {
        schoolsMapper = new SchoolsMapperImpl();
    }
}
