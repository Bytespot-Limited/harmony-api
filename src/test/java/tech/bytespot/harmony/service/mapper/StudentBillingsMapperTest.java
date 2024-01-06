package tech.bytespot.harmony.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class StudentBillingsMapperTest {

    private StudentBillingsMapper studentBillingsMapper;

    @BeforeEach
    public void setUp() {
        studentBillingsMapper = new StudentBillingsMapperImpl();
    }
}
