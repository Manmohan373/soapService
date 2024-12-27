package com.alfaris.ipsh.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.alfaris.ipsh.soap.entity.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>,JpaSpecificationExecutor<Employee> {

}
