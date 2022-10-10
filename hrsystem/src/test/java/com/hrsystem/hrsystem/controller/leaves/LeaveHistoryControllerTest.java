package com.hrsystem.hrsystem.controller.leaves;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hrsystem.hrsystem.entity.command.LeavesCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class LeaveHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper ;
    @Test
    @DatabaseSetup("/DBUnit.dataset/EmployeeRecordLeaves/dataset.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "/DBUnit.dataset/EmployeeRecordLeaves/expected.xml")
    public void recordEmployeeLeaves () throws Exception {
        LocalDate date =LocalDate.now();
        LeavesCommand leavesCommand = new LeavesCommand (30,date);
        mockMvc.perform(MockMvcRequestBuilders.put("/leaves/record/20")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leavesCommand)))
                .andExpect(jsonPath("$.exceedLeaves").value(30))
                .andExpect(jsonPath("$.backToWorkDate").value("2022-11-15"));
    }
    @Test
    @DatabaseSetup("/DBUnit.dataset/EmployeeRecordLeaves/dataset2.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "/DBUnit.dataset/EmployeeRecordLeaves/expected2.xml")
    public void recordEmployeeLeaves2 () throws Exception {
        LocalDate date =LocalDate.now();
        LeavesCommand leavesCommand = new LeavesCommand (5,date);
        mockMvc.perform(MockMvcRequestBuilders.put("/leaves/record/20")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leavesCommand)))
                .andExpect(jsonPath("$.leaves").value(5));
    }
    @Test
    @DatabaseSetup("/DBUnit.dataset/EmployeeRecordLeaves/dataset3.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "/DBUnit.dataset/EmployeeRecordLeaves/expected3.xml")
    public void recordEmployeeLeaves3 () throws Exception {
        LocalDate date =LocalDate.now();
        LeavesCommand leavesCommand = new LeavesCommand (5,date);
        mockMvc.perform(MockMvcRequestBuilders.put("/leaves/record/20")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leavesCommand)))
                .andExpect(jsonPath("$.leaves").value(5));
    }

    @Test
    @DatabaseSetup("/DBUnit.dataset/EmployeeRecordLeaves/dataset4.xml")
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "/DBUnit.dataset/EmployeeRecordLeaves/expected4.xml")
    public void recordEmployeeLeaves4 () throws Exception {
        LocalDate date =LocalDate.now();
        LeavesCommand leavesCommand = new LeavesCommand (14,date);
        mockMvc.perform(MockMvcRequestBuilders.put("/leaves/record/20")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leavesCommand)))
                .andExpect(jsonPath("$.leaves").value(14));
    }

}