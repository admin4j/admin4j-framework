package com.admin4j.framework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author andanyang
 * @since 2023/11/1 11:58
 */
@SpringBootTest(classes = AppTest.class)
@AutoConfigureMockMvc
public class EnumConverterTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"3", "2", "1"})
    void genderIdCode(String userStatus) throws Exception {
        final String result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/UserStatus")
                                .param("userStatus", userStatus)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(result, userStatus);
    }
}
