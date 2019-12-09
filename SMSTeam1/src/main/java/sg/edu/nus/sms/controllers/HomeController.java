package sg.edu.nus.sms.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import sg.edu.nus.sms.model.Course;
import sg.edu.nus.sms.model.User;
import sg.edu.nus.sms.model.UserSession;
import sg.edu.nus.sms.repo.FacultyRepository;
import sg.edu.nus.sms.repo.StudentsRepository;
import sg.edu.nus.sms.repo.UserRepository;

@Controller
@SessionAttributes("usersession")
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private FacultyRepository facrepo;
	
	@Autowired
	private StudentsRepository sturepo;
	
	
	@GetMapping("/index")
	public String index() {
		
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}
	
	
	@GetMapping("/logout")
	public String logout(UserSession usersession, SessionStatus status)
	{
		status.setComplete();;
		return "forward:/home/login";
	}

	@RequestMapping(value="/authenticate", path="/authenticate",method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String getAuthentication(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpSession session)
	{
		
		
		
		if(bindingResult.hasErrors()) {
			return "login";
		}
		
		
		User u1= userrepo.findByUserName(user.getUserName());
		if(u1!=null) user.setId(u1.getId());
		
		
		String username=user.getUserName();
		String usertype;
		if (facrepo.findByUserName(username)!=null) usertype="FAC";
		else if (sturepo.findByUserName(username)!=null) usertype="STU";
		else usertype="ADM";
		
		if(usertype=="STU")
		{
			UserSession usersession=new UserSession(userrepo.findByUserName(username).getId(),"STU");
			session.setAttribute("usersession",usersession);
			return "forward:/student/mygrades";
		}
		else if(usertype=="ADM") 
		{
			UserSession usersession=new UserSession(userrepo.findByUserName(username).getId(),"ADM");
			session.setAttribute("usersession",usersession);
			
			return "forward:/admin/studentlist";
		}
		
		else if(usertype=="FAC")
		{
			UserSession usersession=new UserSession(userrepo.findByUserName(username).getId(),"FAC");
			session.setAttribute("usersession",usersession);
			
			return "forward:/faculty/assignedcourses";
		}
		
		else
			return "login";
	}
}
