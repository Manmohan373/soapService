package com.alfaris.ipsh.soap.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

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
import com.alfaris.ipsh.soap.UpdateEmployeeRequest;
import com.alfaris.ipsh.soap.UpdateEmployeeResponse;
import com.alfaris.ipsh.soap.exception.RecordCreateException;
import com.alfaris.ipsh.soap.exception.RecordNotFoundException;
import com.alfaris.ipsh.soap.service.EmployeeService;

import lombok.RequiredArgsConstructor;
  
@Endpoint
@RequiredArgsConstructor
public class EndPoint {
	//http://techpriemers.com/soapservice
	private final EmployeeService service;
	
	private static final  String NAMESPACE = "http://www.example.org/employee";
	
	@PayloadRoot(namespace = NAMESPACE, localPart = "addEmployeeRequest")
	@ResponsePayload
	public AddEmployeeResponse addEmp(@RequestPayload AddEmployeeRequest request) throws RecordCreateException {
		return service.addEmployee(request);
}
	
	@PayloadRoot(namespace = NAMESPACE, localPart = "getEmployeeRequest")
	@ResponsePayload
	public GetEmployeeResponse getById(@RequestPayload GetEmployeeRequest request) throws RecordNotFoundException {
		return service.getById(request);
}
	
	@PayloadRoot(namespace = NAMESPACE, localPart = "getAllEmployeesRequest")
	@ResponsePayload
	public GetAllEmployeesResponse getAll(@RequestPayload GetAllEmployeesRequest request) throws RecordNotFoundException {
		return service.getAll(request);
}
	
	@PayloadRoot(namespace = NAMESPACE, localPart = "updateEmployeeRequest")
	@ResponsePayload
	public  UpdateEmployeeResponse update(@RequestPayload UpdateEmployeeRequest request) throws RecordNotFoundException {
		return service.update(request);
}
	
	@PayloadRoot(namespace = NAMESPACE, localPart = "deleteEmployeeRequest")
	@ResponsePayload
	public  DeleteEmployeeResponse delete(@RequestPayload DeleteEmployeeRequest request) throws RecordNotFoundException {
		return service.delete(request);
}
	
	@PayloadRoot(namespace = NAMESPACE, localPart = "searchEmployeeRequest")
	@ResponsePayload
	public  SearchEmployeeResponse search(@RequestPayload SearchEmployeeRequest request) throws RecordNotFoundException {
		System.out.println("Manmohan");
				System.out.println("laxmidhara");
								System.out.println("sambidhan");

		return service.search(request);
}

	
	
}
