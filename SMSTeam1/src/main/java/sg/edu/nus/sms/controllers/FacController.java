package sg.edu.nus.sms.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;
import sg.edu.nus.sms.model.User;
import sg.edu.nus.sms.model.UserSession;
import sg.edu.nus.sms.repo.CourseRepository;
import sg.edu.nus.sms.repo.FacultyRepository;
import sg.edu.nus.sms.repo.LeaveAppRepository;
import sg.edu.nus.sms.repo.StudentCourseRepository;
import sg.edu.nus.sms.repo.StudentsRepository;

@Controller
@SessionAttributes("usersession")
@RequestMapping("/faculty")
public class FacController {
	
	@Autowired
	private LeaveAppRepository learepo;
	
	@Autowired
	private FacultyRepository facrepo;
	
	@Autowired
	private CourseRepository courepo;
	
	@Autowired
	private StudentCourseRepository stucourepo;
	
	@Autowired
	private StudentsRepository sturepo;
	
	
	static List gralist= Arrays.asList("A","B","C","D","F");
	
	////////////////////////Courses
	
	@GetMapping("/assignedcourses")
	public String assignedCourses(Model model,@SessionAttribute UserSession usersession) {
		
		if(!usersession.getUserType().equals("FAC")) return "forward:/home/logout";
		
		Faculty fac=facrepo.findById(usersession.getId()).get();
		ArrayList<Course> mycourses=courepo.findAllByCurrentFaculty(fac);
		model.addAttribute("mycourses",mycourses);
		
		model.addAttribute("mydepart",fac.getDepartment());
		model.addAttribute("mycourse",courepo.findAllByCurrentFaculty(fac).size());
		model.addAttribute("myleaves",learepo.findAllByFaculty(fac).size());
		
		
		return "assignedcourses";
	}
	
	///////////////////////////////////Leave Application
	@GetMapping("/addleaveapp")
	public String addLeaveAppForm(Model model,@SessionAttribute UserSession usersession) {
		LeaveApp leaapp=new LeaveApp();
		Faculty f1=facrepo.findById(usersession.getId()).get();
		
		leaapp.setFaculty(f1);
		
		model.addAttribute("leaveapp",leaapp);
		model.addAttribute("facutly",f1);		
		
		
		model.addAttribute("mydepart",f1.getDepartment());
		model.addAttribute("mycourse",courepo.findAllByCurrentFaculty(f1).size());
		model.addAttribute("myleaves",learepo.findAllByFaculty(f1).size());
		
		return "leaveappform";
	}
	
	@GetMapping("/deleteleaveapp/{id}")
	public String deleteLeaveApp(Model model, @PathVariable("id") Integer id) {
		LeaveApp leaapp=learepo.findById(id).get();
		learepo.delete(leaapp);
		return "forward:/faculty/myleaveapps";
	}
	
	
	@GetMapping("/myleaveapps")
	public String myLeaveapps(Model model,@SessionAttribute UserSession usersession) {

		if(!usersession.getUserType().equals("FAC")) return "forward:/home/logout";
		Faculty fac=facrepo.findById(usersession.getId()).get();
		
		List<LeaveApp> mylealist=new ArrayList<LeaveApp>();
		mylealist=learepo.findAllByFaculty(fac);
		model.addAttribute("mleaveapps",mylealist);
		
		model.addAttribute("mydepart",fac.getDepartment());
		model.addAttribute("mycourse",courepo.findAllByCurrentFaculty(fac).size());
		model.addAttribute("myleaves",learepo.findAllByFaculty(fac).size());
		
		
		
		return "myleaveapps";
	}
	
	@RequestMapping(value="/saveleaveapp",path="/saveleaveapp", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveLeaveApp(@Valid @ModelAttribute LeaveApp lea, BindingResult bindingResult, @SessionAttribute UserSession usersession) {
		
		
		
		if(bindingResult.hasErrors())
		{
			return "forward:/faculty/addleaveapp";
		}
		
		if(!usersession.getUserType().equals("FAC")) return "forward:/home/logout";
		
		Faculty f1=facrepo.findById(usersession.getId()).get();
		lea.setStatus("Pending");
		lea.setFaculty(f1);
		
		LeaveApp l1= learepo.findByStartDateAndEndDate(lea.getStartDate(),lea.getEndDate());
			
		if(l1!=null) lea.setId(l1.getId());
		
		learepo.save(lea);
		
		return "forward:/faculty/myleaveapps";
	}
	
	////////////////////////////Student grade
	@GetMapping("/coursestulist/{id}")
	public String courseStuList(@PathVariable("id") Integer id, Model model,@SessionAttribute UserSession usersession) {
		if(!usersession.getUserType().equals("FAC")) return "forward:/home/logout";
		
		Course cou=courepo.findById(id).get();
		List<StudentCourse> stucoulist=stucourepo.findAllByCourse(cou);
		List<StudentCourse> valistucoulist=new ArrayList<StudentCourse>();
		List<StudentCourse> managedstucoulist=new ArrayList<StudentCourse>();
		
		for(StudentCourse stucou:stucoulist)
		{
			if(stucou.getStatus().equals("Approved")) valistucoulist.add(stucou);
			else if(stucou.getStatus().equals("Graded")) managedstucoulist.add(stucou);
		
			
		}
		model.addAttribute("coursename",cou.getCourseName());
		model.addAttribute("valistucoulist",valistucoulist);
		model.addAttribute("managedstucoulist",managedstucoulist);
		
		Faculty fac=facrepo.findById(usersession.getId()).get();
		model.addAttribute("mydepart",fac.getDepartment());
		model.addAttribute("mycourse",courepo.findAllByCurrentFaculty(fac).size());
		model.addAttribute("myleaves",learepo.findAllByFaculty(fac).size());
		
		model.addAttribute("valistucount", valistucoulist.size());
		model.addAttribute("managedstucount", managedstucoulist.size());
		
		
		
		
		return "coursestulist";
	}
	
	@GetMapping("/markgrade/{id}")
	public String markGrade(@PathVariable("id") Integer id,Model model) {
		
		StudentCourse stucou=stucourepo.findById(id).get();
		
		Course cou=courepo.findById(stucou.getCourse().getId()).get();
		model.addAttribute("course", cou);
		
		
		model.addAttribute("stucou",stucou);
		model.addAttribute("gralist",gralist);
		
		return "markgradeform";
	}
	

	@RequestMapping(value="/savegrade",path="/savegrade", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String savegrade(@ModelAttribute StudentCourse stucou) {
		
		Students stu=sturepo.findById(stucou.getStudent().getId()).get();
		Course cou=courepo.findById(stucou.getCourse().getId()).get();
		StudentCourse temp1= stucourepo.findByCourseAndStudent(cou,stu);
		
		//if(temp1!=null) stucou.setId(temp1.getId());
		
		temp1.setGrade(stucou.getGrade());
		temp1.setStatus("Graded");
		stucourepo.save(temp1);
		
		return "forward:/faculty/coursestulist/"+stucou.getCourse().getId();
	}

	

}
