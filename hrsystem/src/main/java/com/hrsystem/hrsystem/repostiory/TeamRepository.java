package com.hrsystem.hrsystem.repostiory;

import com.hrsystem.hrsystem.entity.Employee;
import com.hrsystem.hrsystem.entity.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer> {

}
