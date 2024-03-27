package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TaskRepository;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class MainController {
	@Autowired
	private TaskRepository taskRepository;
	
	private static LocalDate displayMonth;
	
	@GetMapping("/main")
	public String tasks(Model model, @AuthenticationPrincipal AccountUserDetails user, @RequestParam(name = "date", required = false) String date) {
		List<List<LocalDate>> matrixCal = new ArrayList<>(); 
		LocalDate currentDate = null; 
		 		
		if (date == null) {
			displayMonth = LocalDate.now();
		} else {
			displayMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		
		model.addAttribute("prev", displayMonth.plusMonths(-1));
		model.addAttribute("next", displayMonth.plusMonths(1));
		
		LocalDate firstDate =displayMonth.withDayOfMonth(1);
		
		model.addAttribute("month", displayMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月")));
		
		DayOfWeek youbi = firstDate.getDayOfWeek();
		LocalDate lastmonthday = firstDate.plusDays(-youbi.getValue());
		
		matrixCal.add(createOneWeekList(lastmonthday));
		
		currentDate = matrixCal.get(0).get(6);
		currentDate = currentDate.plusDays(1);
		
		while (currentDate.isBefore(displayMonth.with(TemporalAdjusters.lastDayOfMonth()))) {
			matrixCal.add(createOneWeekList(currentDate));
			currentDate = currentDate.plusDays(7);
		}
		
		model.addAttribute("matrix", matrixCal);
		
		List<Tasks> taskList = new ArrayList<>();
		
		boolean isAdmin = false;
		
		Collection<? extends GrantedAuthority> authList = user.getAuthorities();
		
		for (GrantedAuthority auth : authList) {
			if ("ROLE_ADMIN".equals(auth.getAuthority())) {
				isAdmin = true;
				break;
			}
		}
		
		if (isAdmin) {
			taskList = taskRepository.findByDateBetweenAdmin(
						displayMonth.with(TemporalAdjusters.firstDayOfMonth()),
						displayMonth.with(TemporalAdjusters.lastDayOfMonth()));
		} else {
			taskList = taskRepository.findByDateBetweenUser(
						displayMonth.with(TemporalAdjusters.firstDayOfMonth()),
						displayMonth.with(TemporalAdjusters.lastDayOfMonth()),
						user.getName());
		}
		
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		
		for (Tasks task : taskList) {
			tasks.add(task.getDate(), task);
		}
		
		model.addAttribute("tasks", tasks);
		
		return "main";
	}
	
	private List<LocalDate> createOneWeekList(LocalDate startDate) {
		List<LocalDate> oneWeekTaskList = new ArrayList<>();
		
		for (int plusDay = 0; plusDay < 7; plusDay++) {
			oneWeekTaskList.add(startDate.plusDays(plusDay));
		}
		
		return oneWeekTaskList;
	}
}