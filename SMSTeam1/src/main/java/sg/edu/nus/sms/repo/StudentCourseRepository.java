package sg.edu.nus.sms.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Integer>{

	ArrayList<Students> findAllByCourse(Course cou);

	ArrayList<StudentCourse> findAllByStudent(Students stu);

}
