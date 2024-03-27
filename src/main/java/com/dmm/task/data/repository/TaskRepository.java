package com.dmm.task.data.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmm.task.data.entity.Tasks;

public interface TaskRepository extends JpaRepository<Tasks, Integer> {
	@Query("select a from Tasks a where a.date between :from and :to")
	List<Tasks> findByDateBetweenAdmin(@Param("from") LocalDate from, @Param("to") LocalDate to);
	
	@Query("select a from Tasks a where a.date between :from and :to and name = :name")
	List<Tasks> findByDateBetweenUser(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("name") String name);
	
	

}
