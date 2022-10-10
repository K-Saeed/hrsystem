package com.hrsystem.hrsystem.Service;

import com.hrsystem.hrsystem.Service.mapper.EmployeeMapper;
import com.hrsystem.hrsystem.entity.Employee;
import com.hrsystem.hrsystem.entity.LeavesHistory;
import com.hrsystem.hrsystem.entity.Salary;
import com.hrsystem.hrsystem.entity.command.LeavesCommand;
import com.hrsystem.hrsystem.entity.dto.LeavesEmployeeDto;
import com.hrsystem.hrsystem.repostiory.EmployeeRepository;
import com.hrsystem.hrsystem.repostiory.LeavesHistoryRepository;
import com.hrsystem.hrsystem.repostiory.SalaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

@Service
@AllArgsConstructor
@EnableScheduling
public class LeavesService {

    private EmployeeRepository employeeRepository;
    private LeavesHistoryRepository leavesHistoryRepository;
    private SalaryRepository salaryRepository ;
    private EmployeeMapper employeeMapper ;

    public LeavesEmployeeDto recordEmployeeLeaves(Integer employeeid, LeavesCommand leavesCommand) throws Exception {
        boolean check = this.checkMonth(leavesCommand.getDate());
        if (check == true) {
            Employee employee = employeeRepository.findById(employeeid).orElseThrow();
            Year year = Year.now();
            Integer calcLeaves = 0;
            if (leavesHistoryRepository.getAllLeavesOfEmployee(employeeid, year.getValue()) != null) {
                calcLeaves = leavesHistoryRepository.getAllLeavesOfEmployee(employeeid, year.getValue());
            }
            Integer newLeaves = leavesCommand.getLeaves();
            if (calcLeaves < employee.getLeaves()) {
                if ((calcLeaves + newLeaves) < employee.getLeaves()) {
                    LeavesHistory leavesHistory = this.setLeavesHistory(newLeaves, employee, leavesCommand);
                    leavesHistoryRepository.save(leavesHistory);
                    return employeeMapper.convertLeavesToDto(leavesHistory, leavesCommand);
                } else {
                    Integer exceededLeaves = (calcLeaves + newLeaves) - employee.getLeaves();
                    Month month = Month.from(LocalDate.now());
                    if (salaryRepository.findByDate(month.getValue(), employeeid) == null) {
                        Salary salary = this.setSalary(employee, exceededLeaves);
                        salaryRepository.save(salary);
                        LeavesHistory leavesHistory = this.setLeavesHistory(newLeaves, employee, leavesCommand);
                        leavesHistoryRepository.save(leavesHistory);
                        return employeeMapper.convertLeavesToDto(leavesHistory, leavesCommand);
                    } else {
                        Salary salary = salaryRepository.findByDate(month.getValue(), employeeid);
                        if (salary.getExceededLeaves() == null) {
                            salary.setEmployee(employee);
                            salary.setsDate(LocalDate.now());
                            salary.setExceededLeaves(exceededLeaves);
                            salaryRepository.save(salary);
                            LeavesHistory leavesHistory = this.setLeavesHistory(newLeaves, employee, leavesCommand);
                            leavesHistoryRepository.save(leavesHistory);
                            return employeeMapper.convertLeavesToDto(leavesHistory, leavesCommand);
                        } else {
                            salary.setEmployee(employee);
                            salary.setsDate(LocalDate.now());
                            exceededLeaves = exceededLeaves + salary.getExceededLeaves();
                            salary.setExceededLeaves(exceededLeaves);
                            salaryRepository.save(salary);
                            LeavesHistory leavesHistory = this.setLeavesHistory(newLeaves, employee, leavesCommand);
                            leavesHistoryRepository.save(leavesHistory);
                            return employeeMapper.convertLeavesToDto(leavesHistory, leavesCommand);

                        }
                    }

                }
            } else {
                Month month = Month.from(LocalDate.now());
                if (salaryRepository.findByDate(month.getValue(), employeeid) == null) {
                    Salary salary = this.setSalary(employee, newLeaves);
                    salaryRepository.save(salary);
                    LeavesHistory leavesHistory = this.setLeavesHistory(newLeaves, employee, leavesCommand);
                    leavesHistoryRepository.save(leavesHistory);
                    return employeeMapper.convertLeavesToDto(leavesHistory, leavesCommand);
                } else {
                    Salary salary = salaryRepository.findByDate(month.getValue(), employeeid);
                    if (salary.getExceededLeaves() == null) {
                        salary.setEmployee(employee);
                        salary.setsDate(LocalDate.now());
                        salary.setExceededLeaves(newLeaves);
                        salaryRepository.save(salary);
                        LeavesHistory leavesHistory = this.setLeavesHistory(newLeaves, employee, leavesCommand);
                        leavesHistoryRepository.save(leavesHistory);
                        return employeeMapper.convertLeavesToDto(leavesHistory, leavesCommand);
                    } else {
                        salary.setEmployee(employee);
                        salary.setsDate(LocalDate.now());
                        Integer exceededLeaves = salary.getExceededLeaves();
                        exceededLeaves = exceededLeaves + newLeaves;
                        salary.setExceededLeaves(exceededLeaves);
                        salaryRepository.save(salary);
                        LeavesHistory leavesHistory = this.setLeavesHistory(exceededLeaves, employee, leavesCommand);
                        leavesHistoryRepository.save(leavesHistory);
                        return employeeMapper.convertLeavesToDto(leavesHistory, leavesCommand);

                    }
                }
            }
        }else throw new Exception("we can't add leaves to past month");

    }

    private boolean checkMonth(LocalDate date) {
        Month month = Month.from(LocalDate.now());
        Month month1 = Month.from(date);
        if (month==month1||month==month1.plus(1)){
            return true;
        }
        else return false;

    }

    private Salary setSalary(Employee employee, Integer exceededLeaves) {
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setsDate(LocalDate.now());
        salary.setExceededLeaves(exceededLeaves);
        return salary;
    }

    private LeavesHistory setLeavesHistory(Integer leaves, Employee employee, LeavesCommand leavesCommand) {
        LeavesHistory leavesHistory = new LeavesHistory();
        leavesHistory.setlDate(leavesCommand.getDate());
        leavesHistory.setEmployee(employee);
        leavesHistory.setLeaves(leaves);
        return leavesHistory;
    }


}
