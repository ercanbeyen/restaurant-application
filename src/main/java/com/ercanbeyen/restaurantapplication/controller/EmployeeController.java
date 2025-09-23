package com.ercanbeyen.restaurantapplication.controller;

import com.ercanbeyen.restaurantapplication.dto.EmployeeDto;
import com.ercanbeyen.restaurantapplication.model.Employee;
import com.ercanbeyen.restaurantapplication.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class EmployeeController {
    public static final String REDIRECT_EMPLOYEE_MANAGEMENT = "redirect:/employee-management";
    private final EmployeeService employeeService;

    @GetMapping("/showCreateEmployeeForm")
    public String showCreateEmployeeForm(Employee employee) {
        return "create-employee";
    }

    @PostMapping("/createEmployee")
    public String createEmployee(@Valid @ModelAttribute("employee") EmployeeDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-employee";
        }

        employeeService.createEmployee(request);
        return REDIRECT_EMPLOYEE_MANAGEMENT;
    }

    @GetMapping("/showUpdateEmployeeForm/{id}")
    public String showUpdateEmployeeForm(@PathVariable("id") String id, Model model) {
        EmployeeDto response = employeeService.getEmployee(id);
        model.addAttribute("employee", response);
        return "update-employee";
    }

    @PostMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable("id") String id, @Valid @ModelAttribute("employee") EmployeeDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update-employee";
        }

        employeeService.updateEmployee(id, request);
        return REDIRECT_EMPLOYEE_MANAGEMENT;
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") String id) {
        employeeService.deleteEmployee(id);
        return REDIRECT_EMPLOYEE_MANAGEMENT;
    }

    @GetMapping("/employee-management")
    public String showEmployeeManagementPage(Model model) {
        List<EmployeeDto> response = employeeService.getEmployees();
        model.addAttribute("employees", response);
        return "employee-management";
    }
}
