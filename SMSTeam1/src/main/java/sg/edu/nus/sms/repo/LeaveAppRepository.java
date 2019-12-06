package sg.edu.nus.sms.repo;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.sms.model.LeaveApp;

public interface LeaveAppRepository extends JpaRepository <LeaveApp,Integer>{

	LeaveApp findByStartDateAndEndDate(Date startDate, Date endDate);

	Collection<? extends LeaveApp> findByStatus(String string);

	

}
