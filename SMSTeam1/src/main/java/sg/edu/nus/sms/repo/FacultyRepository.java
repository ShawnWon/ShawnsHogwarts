package sg.edu.nus.sms.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.sms.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

	public Faculty findByFacultyID(int facultyID);

	public Faculty findByFirstName(String string);

	public ArrayList<Faculty> findByDepartment(String department);

	public Object findByUserName(String username);

}
