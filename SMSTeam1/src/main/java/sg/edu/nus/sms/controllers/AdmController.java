package sg.edu.nus.sms.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.catalina.filters.RequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;
import sg.edu.nus.sms.model.StudentCourse;
import sg.edu.nus.sms.model.Students;
import sg.edu.nus.sms.model.UserSession;
import sg.edu.nus.sms.repo.CourseRepository;
import sg.edu.nus.sms.repo.FacultyRepository;
import sg.edu.nus.sms.repo.LeaveAppRepository;
import sg.edu.nus.sms.repo.StudentCourseRepository;
import sg.edu.nus.sms.repo.StudentsRepository;

@Controller
@SessionAttributes("usersession")
@RequestMapping("/admin")
public class AdmController {
	
	
	@Autowired
	private StudentsRepository sturepo;
	
	@Autowired
	private FacultyRepository facrepo;
	
	@Autowired
	private CourseRepository courepo;
	
	@Autowired
	private LeaveAppRepository learepo;
	
	@Autowired
	private StudentCourseRepository stucourepo;
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		
	}
	
	static List<String> dlist= Arrays.asList("Physics","Chemistry","Magic","Literature");
	
	
	//////////////////////////////////////////Student
	
	@GetMapping("/studentlist")
	public String liststudents(Model model, @SessionAttribute UserSession usersession) {
		
		
		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";

		ArrayList<Students> stulist=new ArrayList<Students>();
		stulist.addAll(sturepo.findAll());
		model.addAttribute("students",stulist);
		
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		
		return "studentlist";
	}
	
	@GetMapping("/addstudent")
	public String addStudentForm(Model model) {
		Students stu=new Students();
		
		model.addAttribute("student",stu);
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "studentform";
	}
	
	@RequestMapping(value="/savestudent",path="/savestudent", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveStudent(@Valid @ModelAttribute Students stu, BindingResult bindingResult,Model model) {
		
		if(bindingResult.hasErrors())
		{
			
			return "forward:/admin/addstudent";
		}
		
		Students s1= sturepo.findByStudentID(stu.getStudentID());
		
		if(s1!=null) stu.setId(s1.getId());
		
		sturepo.save(stu);
		
		////////////////Initiate course application list for each student
		List<Course> coulist=courepo.findAll();
		
		for(Course cou:coulist) {
			StudentCourse stucou=new StudentCourse();
			stucou.setStudent(stu);
			stucou.setCourse(cou);
			stucourepo.save(stucou);
		}
		
		return "forward:/admin/studentlist";
	}

	@GetMapping("/editstudent/{id}")
	public String editStudentForm(Model model, @PathVariable("id") Integer id) {
		Students stu=sturepo.findById(id).get();
		model.addAttribute("student",stu);
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "studentform";
	}

	
	@GetMapping("/deletestudent/{id}")
	public String deleteStudent(Model model, @PathVariable("id") Integer id) {
		Students stu=sturepo.findById(id).get();
		
		//remove correlated stu_course record before remove course record.
		List<StudentCourse> stucoulist=stucourepo.findAllByStudent(stu);
		stucourepo.deleteAll(stucoulist);
		
		
		sturepo.delete(stu);
		
		
		
		return "forward:/admin/studentlist";
	}
	
	/////////////////////////////////////////////////////faculty
	
	@GetMapping("/facultylist")
	public String listfaculty(Model model,@SessionAttribute UserSession usersession) {
		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
		
		List<Faculty> faclist=new ArrayList<Faculty>();
		faclist=facrepo.findAll();
		model.addAttribute("faculties",faclist);

		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		return "facultylist";
	}
	
	
	@GetMapping("/addfaculty")
	public String addFacultyForm(Model model) {
		Faculty fac=new Faculty();
		model.addAttribute("faculty",fac);
		model.addAttribute("departmentlist",dlist);
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		return "facultyform";
	}
	
	@GetMapping("/editfaculty/{id}")
	public String editFacultyForm(Model model, @PathVariable("id") Integer id) {
		Faculty fac=facrepo.findById(id).get();
		
		model.addAttribute("departmentlist",dlist);
		model.addAttribute("faculty",fac);
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "facultyform";
	}
	
	@GetMapping("/deletefaculty/{id}")
	public String deleteFaculty(Model model, @PathVariable("id") Integer id) {
		Faculty fac=facrepo.findById(id).get();
		facrepo.delete(fac);
		return "forward:/admin/facultylist";
	}
	
	
	@RequestMapping(value="/savefaculty",path="/savefaculty", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveFaculty(@Valid @ModelAttribute Faculty fac, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "forward:/admin/addfaculty";
		}
		
        Faculty f1= facrepo.findByFacultyID(fac.getFacultyID());
		
		if(f1!=null) fac.setId(f1.getId());
		facrepo.save(fac);
		
		return "forward:/admin/facultylist";
	}
	
	///////////////////////////////////////////Course
	
	@GetMapping("/courselist")
	public String listcourse(Model model,@SessionAttribute UserSession usersession) {

		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
		
		List<Course> coulist=new ArrayList<Course>();
		coulist=courepo.findAll();
		model.addAttribute("courses",coulist);
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		
		
		
		return "courselist";
	}
	
	
	@GetMapping("/addcourse")
	public String addCourseForm(Model model) {
		Course cou=new Course();
		Faculty abs=new Faculty();
		cou.setCurrentFaculty(abs);
		
		model.addAttribute("departmentlist",dlist);
		model.addAttribute("course",cou);
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
				
		return "courseform";
	}
	
	@GetMapping("/assignfaculty/{id}")
	public String assignfaculty(Model model,@PathVariable("id") Integer id) {
		Course cou=courepo.findById(id).get();
		model.addAttribute("course", cou);
		ArrayList<Faculty> facsdepart=facrepo.findByDepartment(cou.getDepartment());
		model.addAttribute("facsofdepartment", facsdepart);
		
		model.addAttribute("departmentlist",dlist);
		
		return "assignfacultyform";
	}
	
	
	@GetMapping("/editcourse/{id}")
	public String editCourseForm(Model model, @PathVariable("id") Integer id) {
		Course cou=courepo.findById(id).get();
		
		model.addAttribute("departmentlist",dlist);
		model.addAttribute("course",cou);
		
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		return "courseform";
	}
	
	@GetMapping("/deletecourse/{id}")
	public String deleteCourse(Model model, @PathVariable("id") Integer id) {
		
		Course cou=courepo.findById(id).get();
		
		//remove correlated stu_course record before remove course record.
		List<StudentCourse> stucoulist=stucourepo.findAllByCourse(cou);
		stucourepo.deleteAll(stucoulist);
		
		courepo.delete(cou);
		
		
		
		return "forward:/admin/courselist";
	}
	
	
	@RequestMapping(value="/savecourse",path="/savecourse", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveCourse(@Valid @ModelAttribute Course cou, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "forward:/admin/addcourse";
		}
		
		Course c1= courepo.findByCourseCode(cou.getCourseCode());
		
		if(c1!=null) cou.setId(c1.getId());
		
		
		courepo.save(cou);
		
		return "forward:/admin/courselist";
	}
	
	
	@RequestMapping(value="/saveassign",path="/saveassign", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveAssign(@ModelAttribute Course cou) {
		
		Course c1= courepo.findByCourseCode(cou.getCourseCode());
		
		if(c1!=null) cou.setId(c1.getId());
		
		
		courepo.save(cou);
		
		return "forward:/admin/courselist";
	}
	
	/////////////////////////////////////////Leave application
	
	@GetMapping("/applicationlist")
	public String listleaveapp(Model model, @SessionAttribute UserSession usersession) {

		if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
		
		
		List<LeaveApp> lealist=new ArrayList<LeaveApp>();
		List<LeaveApp> alllealist=new ArrayList<LeaveApp>();
		lealist=learepo.findAllByStatus("Pending");
		alllealist=learepo.findAll();
		model.addAttribute("leaveapps",lealist);
		model.addAttribute("allleaveapps",alllealist);
		
		
		int leacount=learepo.findAllByStatus("Pending").size();
		int couappcount=stucourepo.findAllByStatus("Pending").size();
		model.addAttribute("stucount",sturepo.count());
		model.addAttribute("faccount",facrepo.count());
		model.addAttribute("coucount",courepo.count());
		model.addAttribute("leacount",leacount);
		model.addAttribute("couappcount",couappcount);
		
		
		
		
		return "applicationlist";
	}
	
	

	
	@GetMapping("/approveleaveapp/{id}")
	public String approveLeaveAppForm(Model model, @PathVariable("id") Integer id) {
		LeaveApp leaapp=learepo.findById(id).get();
		leaapp.setStatus("Approved");
		learepo.save(leaapp);
		model.addAttribute("leaveapp",leaapp);
				
		return "forward:/admin/applicationlist";
	}
	
	@GetMapping("/rejectleaveapp/{id}")
	public String rejectLeaveAppForm(Model model, @PathVariable("id") Integer id) {
		LeaveApp leaapp=learepo.findById(id).get();
		leaapp.setStatus("Rejected");
		learepo.save(leaapp);
		model.addAttribute("leaveapp",leaapp);
				
		return "forward:/admin/applicationlist";
	}

	
	
	@RequestMapping(value="/saveleaveapp",path="/saveleaveapp", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveLeaveApp(@Valid @ModelAttribute LeaveApp lea, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "leaveappform";
		}
		
		Faculty f1=facrepo.findByFirstName("Jon");
		lea.setFaculty(f1);
		
		learepo.save(lea);
		
		return "forward:/admin/applicationlist";
	}
	
	//////////////////////////////////Course Application
	@GetMapping("/courseapplist")
	public String courseAppList(Model model,@SessionAttribute UserSession usersession) {
		
	if(!usersession.getUserType().equals("ADM")) return "forward:/home/logout";
	
	List<StudentCourse> stucoulist=stucourepo.findAll();
	List<StudentCourse> pendingstucoulist=new ArrayList<StudentCourse>();
	List<StudentCourse> managedstucoulist=new ArrayList<StudentCourse>();
	
	for (StudentCourse stucou:stucoulist)
	{
		if(stucou.getStatus().equals("Pending")) 
			{
			pendingstucoulist.add(stucou);
			}
		else if (stucou.getStatus().equals("Approved")||stucou.getStatus().equals("Rejected"))
			{
			managedstucoulist.add(stucou);
			}
		
	}
	
	model.addAttribute("pendingstucoulist",pendingstucoulist);
	model.addAttribute("managedstucoulist",managedstucoulist);
	
	
	int leacount=learepo.findAllByStatus("Pending").size();
	int couappcount=stucourepo.findAllByStatus("Pending").size();
	model.addAttribute("stucount",sturepo.count());
	model.addAttribute("faccount",facrepo.count());
	model.addAttribute("coucount",courepo.count());
	model.addAttribute("leacount",leacount);
	model.addAttribute("couappcount",couappcount);
	
	
	
	return "courseapplist";
	}
	
	@GetMapping("/approvecourseapp/{id}")
	public String approveCourseApp(@PathVariable("id") Integer id) {
		StudentCourse stucou=stucourepo.findById(id).get();
		stucou.setStatus("Approved");
		stucourepo.save(stucou);
				
		return "forward:/admin/courseapplist";
	}
	
	@GetMapping("/rejectcourseapp/{id}")
	public String rejectCourseApp(@PathVariable("id") Integer id) {
		StudentCourse stucou=stucourepo.findById(id).get();
		stucou.setStatus("Rejected");
		stucourepo.save(stucou);
		return "forward:/admin/courseapplist";
	}
}
