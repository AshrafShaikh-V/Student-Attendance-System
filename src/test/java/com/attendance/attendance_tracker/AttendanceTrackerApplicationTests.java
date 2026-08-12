package com.attendance.attendance_tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.attendance.attendance_tracker.entity.Student;
import com.attendance.attendance_tracker.entity.Subject;
import com.attendance.attendance_tracker.repository.AttendanceRepository;
import com.attendance.attendance_tracker.repository.StudentRepository;
import com.attendance.attendance_tracker.repository.SubjectRepository;

@SpringBootTest
class AttendanceTrackerApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@BeforeEach
	void setUp() {
		attendanceRepository.deleteAll();
		subjectRepository.deleteAll();
		studentRepository.deleteAll();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

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
	void attendanceSearchFiltersResultsCorrectly() throws Exception {
		Student student = studentRepository.save(Student.builder()
				.rollNumber("ATT-002")
				.firstName("Mina")
				.lastName("Khan")
				.email("mina@example.com")
				.department("Science")
				.year(1)
				.division("B")
				.build());
		Subject subject = subjectRepository.save(Subject.builder()
				.subjectCode("PHY-101")
				.subjectName("Physics")
				.credits(4)
				.build());

		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(attendanceRequest(student.getId(), subject.getId(), "PRESENT")))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/attendance")
				.param("studentId", student.getId().toString())
				.param("subjectId", subject.getId().toString())
				.param("attendanceDate", "2026-08-11")
				.param("status", "PRESENT"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].studentName").value("Mina Khan"))
				.andExpect(jsonPath("$[0].subjectName").value("Physics"));
	}

	@Test
	void duplicateAttendanceReturnsConflict() throws Exception {
		Student student = studentRepository.save(Student.builder()
				.rollNumber("ATT-003")
				.firstName("Noor")
				.lastName("Patel")
				.email("noor@example.com")
				.department("Math")
				.year(3)
				.division("C")
				.build());
		Subject subject = subjectRepository.save(Subject.builder()
				.subjectCode("MTH-101")
				.subjectName("Mathematics")
				.credits(3)
				.build());

		String request = attendanceRequest(student.getId(), subject.getId(), "ABSENT");
		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isConflict());
	}

	@Test
	void duplicateAttendanceUpdateReturnsConflict() throws Exception {
		Student student = studentRepository.save(Student.builder()
				.rollNumber("ATT-004")
				.firstName("Riya")
				.lastName("Singh")
				.email("riya@example.com")
				.department("Arts")
				.year(2)
				.division("A")
				.build());
		Subject subject = subjectRepository.save(Subject.builder()
				.subjectCode("ENG-101")
				.subjectName("English")
				.credits(3)
				.build());

		String firstAttendance = attendanceRequest(student.getId(), subject.getId(), "PRESENT");
		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(firstAttendance))
				.andExpect(status().isCreated());

		String secondAttendance = "{\"studentId\":" + student.getId()
				+ ",\"subjectId\":" + subject.getId()
				+ ",\"attendanceDate\":\"2026-08-12\""
				+ ",\"status\":\"ABSENT\"}";
		mockMvc.perform(post("/attendance")
				.contentType(MediaType.APPLICATION_JSON)
				.content(secondAttendance))
				.andExpect(status().isCreated());

		Long secondId = attendanceRepository.findAll().stream()
				.filter(a -> !a.getAttendanceDate().equals(java.time.LocalDate.parse("2026-08-11")))
				.findFirst()
				.orElseThrow()
				.getId();

		mockMvc.perform(put("/attendance/{id}", secondId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(attendanceRequest(student.getId(), subject.getId(), "PRESENT")))
				.andExpect(status().isConflict());
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
