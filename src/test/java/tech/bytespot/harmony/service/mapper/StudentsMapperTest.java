package tech.bytespot.harmony.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class StudentsMapperTest {

    private StudentsMapper studentsMapper;

    @BeforeEach
    public void setUp() {
        studentsMapper = new StudentsMapperImpl();
    }
}
