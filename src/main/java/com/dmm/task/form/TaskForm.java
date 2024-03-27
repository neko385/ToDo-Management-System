package com.dmm.task.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class TaskForm {
	@NotEmpty
	private String title;
	@NotEmpty
	private String date;
	@NotEmpty
	private String text;
	private Boolean done;
	
}
