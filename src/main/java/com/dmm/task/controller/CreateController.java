package com.dmm.task.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TaskRepository;
import com.dmm.task.form.TaskForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class CreateController {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@GetMapping("/main/create/{dateStr}")
	public String create(@PathVariable String dateStr, Model model) {
		TaskForm taskForm = new TaskForm();
		taskForm.setDate(dateStr);
		model.addAttribute("taskForm", taskForm);
		
		return "create";
	}
	
	
	@PostMapping("/main/create")
	public String regster(@Validated TaskForm taskForm, BindingResult bindingResult, @AuthenticationPrincipal AccountUserDetails user, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("taskForm", taskForm);
			return "create";
		}
		
		
		Tasks task = new Tasks();
		task.setTitle(taskForm.getTitle());
		task.setName(user.getName());
		task.setText(taskForm.getText());
		task.setDate(LocalDate.parse(taskForm.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		task.setDone(false);
		
		taskRepository.save(task);
		
		return "redirect:/main";
	}
	
	
}

