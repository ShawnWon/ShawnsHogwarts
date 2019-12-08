package sg.edu.nus.sms.repo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;

public interface LeaveAppRepository extends JpaRepository <LeaveApp,Integer>{

	LeaveApp findByStartDateAndEndDate(Date startDate, Date endDate);

	List<LeaveApp> findAllByFaculty(Faculty fac);

	List<LeaveApp> findAllByStatus(String string);

	

	

	

}
