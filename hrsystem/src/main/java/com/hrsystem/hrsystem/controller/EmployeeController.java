package com.hrsystem.hrsystem.controller;

import com.hrsystem.hrsystem.Service.EmployeeService;
import com.hrsystem.hrsystem.entity.EmployeeFindAllDto;
import com.hrsystem.hrsystem.entity.command.EmployeeCommand;
import com.hrsystem.hrsystem.entity.command.EmployeeUpdateCommand;
import com.hrsystem.hrsystem.entity.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping (value = "/add")
    public @ResponseBody EmployeeDto addNewEmployee (@RequestBody EmployeeCommand employeeCommand) throws Exception {
        return employeeService.createNewEmployee(employeeCommand);
    }
    @GetMapping  (value ="/{employeeId}")
    public @ResponseBody EmployeeDto findEmployeeByID (@PathVariable Integer employeeId) throws Exception {
        return employeeService.getEmployeeByID(employeeId);
    }
    @PutMapping (value = "/update/{employeeId}")
    public @ResponseBody EmployeeUpdateDto updateEmployee (@PathVariable  Integer employeeId , @RequestBody EmployeeUpdateCommand employeeUpdateCommand) throws Exception {
        return employeeService.updateEmployee(employeeId,employeeUpdateCommand);
    }

    @GetMapping (value = "/manager/{employeeId}")
    public  @ResponseBody List <EmployeeFindAllDto>  getEmployeesByManagerid (@PathVariable Integer employeeId) throws Exception {
        return employeeService.getAllEmployeesRelatedToOneManager(employeeId);
    }

    @GetMapping (value = "/team/{teamId}")
    public  @ResponseBody List<EmployeeFindAllDto> getEmployeesByTeamid(@PathVariable Integer teamId) throws Exception {
        return employeeService.getEmployeesByTeamid(teamId);
    }

    @GetMapping (value = "/salary/{employeeId}")
    public EmployeeSalaryDto getEmployeeNetSalary(@PathVariable Integer employeeId) throws Exception {
        return employeeService.getEmployeeNetSalary(employeeId);
    }

    @DeleteMapping (value =  "/employee/{employeeId}")
    public Object EmployeeDelterByID(@PathVariable Integer employeeId) throws Exception {
        return employeeService.deleteEmployeeByID(employeeId);
    }
    @GetMapping (value ="/manager/hierarchical/{managerId}")
    public  @ResponseBody List <EmployeeFindAllDto>  allEmployeesHierarchical (@PathVariable Integer managerId) throws Exception {
        return employeeService.getAllEmployeesHierarchical(managerId);
    }


}
