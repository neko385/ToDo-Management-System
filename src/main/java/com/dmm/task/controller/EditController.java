package com.dmm.task.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TaskRepository;
import com.dmm.task.form.TaskForm;

@Controller
public class EditController {
	@Autowired
	private TaskRepository taskRepository;
	
	
	@GetMapping("main/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Tasks task = taskRepository.getById(id);
		model.addAttribute("task", task);
		
		return "edit";
	}
	
	@PostMapping("main/edit/{id}")
	public String regster(@Validated TaskForm taskForm, @PathVariable Integer id) {
		Tasks oldtask = taskRepository.getById(id);
		
		Tasks task = new Tasks();
		task.setId(id);
		task.setTitle(taskForm.getTitle());
		task.setName(oldtask.getName());
		task.setText(taskForm.getText());
		task.setDate(LocalDate.parse(taskForm.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		
		if (taskForm.getDone() != null) {
			task.setDone(taskForm.getDone());
		} else {
			task.setDone(false);
		}
		
		taskRepository.save(task);
		
		return "redirect:/main";
	}
	
	
	@PostMapping("main/delete/{id}")
	public String delete(@PathVariable Integer id) {
		taskRepository.deleteById(id);
		
		return "redirect:/main";
	}
}
