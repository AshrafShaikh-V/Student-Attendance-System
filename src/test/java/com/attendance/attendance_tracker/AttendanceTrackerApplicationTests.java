package com.attendance.attendance_tracker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.attendance.attendance_tracker.entity.Student;
import com.attendance.attendance_tracker.entity.Subject;
import com.attendance.attendance_tracker.repository.AttendanceRepository;
import com.attendance.attendance_tracker.repository.StudentRepository;
import com.attendance.attendance_tracker.repository.SubjectRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AttendanceTrackerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Test
	void attendanceCrudLifecycleWorks() throws Exception {
		Student student = studentRepository.save(Student.builder()
				.rollNumber("ATT-001")
				.firstName("Ashraf")
				.lastName("Shaikh")
				.email("ashraf@example.com")
				.department("Computer")
				.year(2)
				.division("A")
				.build());
		Subject subject = subjectRepository.save(Subject.builder()
				.subjectCode("JAVA-101")
				.subjectName("Java")
				.credits(4)
				.build());
		String request = attendanceRequest(student.getId(), subject.getId(), "PRESENT");

		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.studentName").value("Ashraf Shaikh"))
				.andExpect(jsonPath("$.subjectName").value("Java"));

		Long attendanceId = attendanceRepository.findAll().getFirst().getId();
		mockMvc.perform(get("/attendance"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(attendanceId));
		mockMvc.perform(get("/attendance/{id}", attendanceId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("PRESENT"));

		mockMvc.perform(put("/attendance/{id}", attendanceId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(attendanceRequest(student.getId(), subject.getId(), "ABSENT")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("ABSENT"));

		mockMvc.perform(delete("/attendance/{id}", attendanceId))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/attendance/{id}", attendanceId))
				.andExpect(status().isNotFound());
	}

	@Test
	void attendanceValidationAndLookupsReturnExpectedErrors() throws Exception {
		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest());
		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(attendanceRequest(999L, 999L, "PRESENT")))
				.andExpect(status().isNotFound());
		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(attendanceRequest(999L, 999L, "INVALID")))
				.andExpect(status().isBadRequest());
		mockMvc.perform(get("/attendance/{id}", 999L))
				.andExpect(status().isNotFound());
	}

	private String attendanceRequest(Long studentId, Long subjectId, String status) {
		return "{\"studentId\":" + studentId
				+ ",\"subjectId\":" + subjectId
				+ ",\"attendanceDate\":\"2026-08-11\""
				+ ",\"status\":\"" + status + "\"}";
	}

}
