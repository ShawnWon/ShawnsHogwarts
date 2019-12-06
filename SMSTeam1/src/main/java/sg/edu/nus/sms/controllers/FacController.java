package sg.edu.nus.sms.controllers;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import sg.edu.nus.sms.model.Faculty;
import sg.edu.nus.sms.model.LeaveApp;
import sg.edu.nus.sms.repo.FacultyRepository;
import sg.edu.nus.sms.repo.LeaveAppRepository;

@Controller
@SessionAttributes("user")
@RequestMapping("/faculty")
public class FacController {
	
	@Autowired
	private LeaveAppRepository learepo;
	
	@Autowired
	private FacultyRepository facrepo;
	
	
	
	
	@GetMapping("/assignedcourses")
	public String assignedCourses() {
		
		return "assignedcourses";
	}
	
	@GetMapping("/addleaveapp")
	public String addLeaveAppForm(Model model) {
		LeaveApp leaapp=new LeaveApp();
		Faculty f1=facrepo.findByFirstName("Jon");
		
		leaapp.setFaculty(f1);
		
		model.addAttribute("leaveapp",leaapp);
		model.addAttribute("facutly",f1);		
		return "leaveappform";
	}
	
	@GetMapping("/deleteleaveapp/{id}")
	public String deleteLeaveApp(Model model, @PathVariable("id") Integer id) {
		LeaveApp leaapp=learepo.findById(id).get();
		learepo.delete(leaapp);
		return "forward:/faculty/myleaveapps";
	}
	
	
	@GetMapping("/myleaveapps")
	public String myLeaveapps(Model model) {

		ArrayList<LeaveApp> mylealist=new ArrayList<LeaveApp>();
		mylealist.addAll(learepo.findAll());
		model.addAttribute("mleaveapps",mylealist);
		return "myleaveapps";
	}
	
	@RequestMapping(value="/saveleaveapp",path="/saveleaveapp", method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String saveLeaveApp(@Valid @ModelAttribute LeaveApp lea, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "leaveappform";
		}
		
		Faculty f1=facrepo.findByFirstName("Jon");
		lea.setStatus("Pending");
		lea.setFaculty(f1);
		
		LeaveApp l1= learepo.findByStartDateAndEndDate(lea.getStartDate(),lea.getEndDate());
			
		if(l1!=null) lea.setId(l1.getId());
		
		learepo.save(lea);
		
		return "forward:/faculty/myleaveapps";
	}

}
