package com.alfaris.ipsh.soap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alfaris.ipsh.soap.AddEmployeeRequest;
import com.alfaris.ipsh.soap.AddEmployeeResponse;
import com.alfaris.ipsh.soap.DeleteEmployeeRequest;
import com.alfaris.ipsh.soap.DeleteEmployeeResponse;
import com.alfaris.ipsh.soap.GetAllEmployeesRequest;
import com.alfaris.ipsh.soap.GetAllEmployeesResponse;
import com.alfaris.ipsh.soap.GetEmployeeRequest;
import com.alfaris.ipsh.soap.GetEmployeeResponse;
import com.alfaris.ipsh.soap.SearchEmployeeRequest;
import com.alfaris.ipsh.soap.SearchEmployeeResponse;
import com.alfaris.ipsh.soap.SearchEmployeeResponse.AaData;
import com.alfaris.ipsh.soap.ServiceResponse;
import com.alfaris.ipsh.soap.UpdateEmployeeRequest;
import com.alfaris.ipsh.soap.UpdateEmployeeResponse;
import com.alfaris.ipsh.soap.entity.Employee;
import com.alfaris.ipsh.soap.exception.RecordCreateException;
import com.alfaris.ipsh.soap.exception.RecordNotFoundException;
import com.alfaris.ipsh.soap.repository.EmployeeRepository;
import com.alfaris.ipsh.soap.repository.specification.EmployeeSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeRepository employeeRepo;

	public AddEmployeeResponse addEmployee(AddEmployeeRequest request) throws RecordCreateException {
		AddEmployeeResponse response = new AddEmployeeResponse();
		try {
			Employee employee = new Employee();
			Optional<Employee> optional = employeeRepo.findById(request.getEmployee().getEmpId());
			if (optional.isPresent()) {
				throw new RecordCreateException("record already exist!!!");
			}
			BeanUtils.copyProperties(request.getEmployee(), employee);
			System.out.println(employee);
			if (request.getStatus() != "Active") {
				employee.setStatus("Active");
			}
			Employee result = employeeRepo.save(employee);
			response.setServiceResponse(createServiceResponse("SUCCESS", "data added successfully", result.toString()));
			return response;
		} catch (RecordCreateException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error:" + e.getMessage(), e);
		}
		return response;
	}

	private ServiceResponse createServiceResponse(String code, String message, String request) {
		ServiceResponse serviceResponse = new ServiceResponse();
		serviceResponse.setCode(code);
		serviceResponse.setMessage(message);
		serviceResponse.setDetails(request);
		return serviceResponse;
	}

	public GetEmployeeResponse getById(GetEmployeeRequest request) throws RecordNotFoundException {
		GetEmployeeResponse response = new GetEmployeeResponse();
		try {
			Optional<Employee> result = employeeRepo.findById(request.getEmpId());
			if (result.isPresent()) {
				Employee entity = result.get();
				response.setServiceResponse(
						createServiceResponse("SUCCESS", "data found successfully", entity.toString()));
				return response;
			} else {
				throw new RecordNotFoundException("record not found!!!");
			}
		} catch (RecordNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error : " + e.getMessage(), e);
		}
		return response;
	}

	public GetAllEmployeesResponse getAll(GetAllEmployeesRequest request) {
		GetAllEmployeesResponse response = new GetAllEmployeesResponse();
		List<Employee> employees = employeeRepo.findAll();
		for (Employee emp : employees) {
			System.out.println(emp);
			GetAllEmployeesResponse.EmployeeList list = mapToList(emp);
			response.getEmployeeList().add(list);
		}
		return response;
	}

	private GetAllEmployeesResponse.EmployeeList mapToList(Employee emp) {
		GetAllEmployeesResponse.EmployeeList list = new GetAllEmployeesResponse.EmployeeList();
		BeanUtils.copyProperties(emp, list);
		return list;
	}

//	public GetAllEmployeesResponse getAll(GetAllEmployeesRequest request) {
//	GetAllEmployeesResponse response = new GetAllEmployeesResponse();
//	List<Employee> list=employeeRepo.findAll();
//	response.setServiceResponse(createServiceResponse("SUCCESS", "Employee found successfully", list.toString()));
//	return response;
//}

	public UpdateEmployeeResponse update(UpdateEmployeeRequest request) throws RecordNotFoundException {
		UpdateEmployeeResponse response = new UpdateEmployeeResponse();
		try {
			Optional<Employee> result = employeeRepo.findById(request.getEmployee().getEmpId());
			if (!result.isPresent()) {
				throw new RecordNotFoundException("Record Not Found!!!");
			}
			Employee existing = result.get();
			existing.setName(request.getEmployee().getName());
			existing.setAge(request.getEmployee().getAge());
			existing.setGender(request.getEmployee().getGender());
			existing.setEmail(request.getEmployee().getEmail());
			existing.setDepartment(request.getEmployee().getDepartment());
			existing.setSalary(request.getEmployee().getSalary());
			employeeRepo.save(existing);
			response.setServiceResponse(
					createServiceResponse("SUCCESS", "data updated successfully", result.toString()));
			return response;
		} catch (RecordNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error: " + e.getMessage(), e);
			return response;
		}
	}

	public DeleteEmployeeResponse delete(DeleteEmployeeRequest request) {
		DeleteEmployeeResponse response = new DeleteEmployeeResponse();
		try {
			Optional<Employee> result = employeeRepo.findById(request.getEmpId());
			if (!result.isPresent()) {
				throw new RecordNotFoundException("Record Not Found!!!");
			}
			Employee existing = result.get();
			if (existing.getStatus().contentEquals("deleted")) {
				response.setServiceResponse(createServiceResponse("FAILED", "data already deleted", result.toString()));
				return response;
			}
			existing.setStatus("deleted"); 
			employeeRepo.save(existing);
			response.setServiceResponse(
					createServiceResponse("SUCCESS", "data deleted successfully", result.toString()));
			return response;
		} catch (Exception e) {
			log.error("Error:" + e.getMessage(), e);
		}
		return response;
	}

	public SearchEmployeeResponse search(SearchEmployeeRequest request) {

		SearchEmployeeResponse response = new SearchEmployeeResponse();
		try {
			Pageable pageable = PageRequest.of(request.getIDisplayStart() / request.getIDisplayLength(),
					request.getIDisplayLength());
			Page<Employee> headerList = employeeRepo
					.findAll(EmployeeSpecification.getStreamBySearchSpec(request.getSearchParam()), pageable);
			AaData data = new AaData();
			for (Employee entity : headerList) {
				SearchEmployeeResponse.AaData.EmployeeList list = new SearchEmployeeResponse.AaData.EmployeeList();
				BeanUtils.copyProperties(entity, list);
				data.getEmployeeList().add(list);
			}
			response.setAaData(data);
	        response.setITotalDisplayRecords(headerList.getTotalElements());
	        response.setITotalRecords(headerList.getTotalElements());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setAaData(new AaData());
	        response.setITotalDisplayRecords(0);
	        response.setITotalRecords(0);
		}
		return response;
	}



}
