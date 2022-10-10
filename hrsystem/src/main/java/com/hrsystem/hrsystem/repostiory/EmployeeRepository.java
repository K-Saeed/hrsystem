package com.hrsystem.hrsystem.repostiory;

import com.hrsystem.hrsystem.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e WHERE e.manager.id is null")
    List<Employee> getAllEmployees();

    @Query("SELECT e FROM Employee e WHERE e.manager.id =: managerId")
    List<Employee> getEmployees(@Param("managerId")Integer managerId);

    @Query("SELECT e FROM Employee e WHERE e.team.id =:teamId ")
    List<Employee> getEmployees(@Param("teamId")Integer teamId);
}
