package com.hrsystem.hrsystem.controller.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hrsystem.hrsystem.controller.EmployeeController;
import com.hrsystem.hrsystem.entity.Department;
import com.hrsystem.hrsystem.entity.Team;
import com.hrsystem.hrsystem.entity.command.EmployeeCommand;
import com.hrsystem.hrsystem.entity.command.EmployeeUpdateCommand;
import com.hrsystem.hrsystem.repostiory.EmployeeRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
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
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith ({SpringExtension.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class EmployeeControllerTest {


    @Autowired
    private EmployeeController employeeController;
    @Autowired
    ObjectMapper objectMapper ;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @DatabaseSetup("/DBUnit.dataset/employeesaver/datasetOfSaveEmployee.xml")
    @ExpectedDatabase(assertionMode= DatabaseAssertionMode.NON_STRICT,value = "/DBUnit.dataset/employeesaver/expected.xml")
    public void testEmployeeAdder() throws Exception {
        Department department = new Department( 1, "ed" );
        Team team = new Team( 1, "team" );
        LocalDate date =  LocalDate.of (1999,8,01);
        EmployeeCommand employeeCommand = new EmployeeCommand( "khaled", "saeed", 3000, 1, 455665, 1, 20, new ArrayList<>(),
                date, date, date , 15 );
        mockMvc.perform( MockMvcRequestBuilders.post( "/employee/add" ).accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( objectMapper.writeValueAsString( employeeCommand ) ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.fname" ).value( "khaled" ) )
                .andExpect( jsonPath( "$.lname" ).value( "saeed" ) )
                .andExpect( jsonPath( "$.grossSallary" ).value( 3000 ) )
                .andExpect( jsonPath( "$.departmentId" ).value( 1 ) )
                .andExpect( jsonPath( "$.teamId" ).value( 1 ) )
                .andExpect( jsonPath( "$.managerId" ).value( 20 ) )
                .andExpect( jsonPath( "$.birthDate" ).value( "1999-08-01" ) )
                .andExpect( jsonPath( "$.graduationDate" ).value( "1999-08-01" ) )
                .andExpect( jsonPath( "$.startWorkDate" ).value( "1999-08-01" ) );
    }

    @Test
    @DatabaseSetup("/DBUnit.dataset/employeefinder/datasetOfFindEmployee.xml")
    void testEmployeeFinder () throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/employee/1").accept( MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("khaled"))
                .andExpect(jsonPath("$.lname").value("saeed"))
                .andExpect(jsonPath("$.grossSallary").value(10000))
                .andExpect(jsonPath("$.departmentId").value(1))
                .andExpect(jsonPath("$.managerId").value(1))
                .andExpect(jsonPath("$.managerId").value(1))
                .andExpect(jsonPath( "$.birthDate").value("1996-08-01"))
                .andExpect(jsonPath( "$.graduationDate").value("2020-09-01"))
                .andExpect(jsonPath( "$.startWorkDate").value("2022-09-01"));
    }

    @Test
    @DatabaseSetup("/DBUnit.dataset/employeeupdater/dataemployeeupdater.xml")
    @ExpectedDatabase(assertionMode= DatabaseAssertionMode.NON_STRICT,value = "/DBUnit.dataset/employeeupdater/expectedsalary.xml")
    public void testEmployeeUpdate() throws Exception {
        EmployeeUpdateCommand employeeUpdateCommand = new EmployeeUpdateCommand(15000.0);
        mockMvc.perform( MockMvcRequestBuilders.put( "/employee/update/1" ).accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content(objectMapper.writeValueAsString(employeeUpdateCommand)))
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.grossSalary").value(15000.0));
    }

    @Test
    @DatabaseSetup ("/DBUnit.dataset/employeesRelatedToManager/dataset.xml")
    @ExpectedDatabase (assertionMode = DatabaseAssertionMode.NON_STRICT , value = "/DBUnit.dataset/employeesRelatedToManager/expected.xml")
    public void testEmployeesRelatedToManager() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get( "/employee/manager/10" ).accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON ))
                .andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class))).
                andExpect(jsonPath("$.*", Matchers.hasSize(1)));
    }
    @Test
    @Transactional
    @DatabaseSetup ("/DBUnit.dataset/employeeRelatedToTeam/dataset.xml")
    @ExpectedDatabase (assertionMode = DatabaseAssertionMode.NON_STRICT , value = "/DBUnit.dataset/employeeRelatedToTeam/expected.xml")
    public void testEmployeesRelatedToTeam() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get( "/employee/team/10").accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON )).
                andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class))).
                andExpect(jsonPath("$.*", Matchers.hasSize(3))).
                andExpect(jsonPath("$[1].fname", Matchers.equalToIgnoringCase("khaled")));
    }
    @Test
    @DatabaseSetup ("/DBUnit.dataset/employeeDeleter/dataset.xml")
    @ExpectedDatabase (assertionMode= DatabaseAssertionMode.NON_STRICT,value = "/DBUnit.dataset/employeeDeleter/expected.xml")
    public void testEmployeesDeleter() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.delete( "/employee/employee/2").accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON )).
                andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup ("/DBUnit.dataset/employeeDeleter/dataset2.xml")
    @ExpectedDatabase (assertionMode= DatabaseAssertionMode.NON_STRICT,value = "/DBUnit.dataset/employeeDeleter/expected.xml")
    public void testEmployeesDeleter2() throws Exception {
        Assert.assertThrows(org.springframework.web.util.NestedServletException.class,
                () -> mockMvc.perform( MockMvcRequestBuilders.delete("/employee/employee/20")));
    }

    @Test
    @DatabaseSetup("/DBUnit.dataset/employeeSalary/dataset.xml")
    public void testEmployeeSalaryGetter () throws  Exception {
        mockMvc.perform( MockMvcRequestBuilders.get( "/employee/salary/1").accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON )).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.grossSalary").value(10000))
                .andExpect(jsonPath("$.netSalary").value(8000));
    }
    @Test
    @DatabaseSetup ("/DBUnit.dataset/employeesRelatedToManager/dataset.xml")
    @ExpectedDatabase (assertionMode = DatabaseAssertionMode.NON_STRICT ,
            value = "/DBUnit.dataset/employeesRelatedToManager/expected.xml")
    public void testAllEmployeesHierarchical () throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get( "/employee/manager/hierarchical/10" )
                        .accept( MediaType.APPLICATION_JSON )
                        .contentType( MediaType.APPLICATION_JSON ))
                        .andDo(print())
                .andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class))).
                andExpect(jsonPath("$.*", Matchers.hasSize(2)));
    }

}
