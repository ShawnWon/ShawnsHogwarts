package sg.edu.nus.sms.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import sg.edu.nus.sms.model.User;
import sg.edu.nus.sms.model.UserSession;

@Controller
@SessionAttributes("usersession")
@RequestMapping("/home")
public class HomeController {
	
	@GetMapping("/index")
	public String index() {
		
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(value="/authenticate", path="/authenticate",method= {RequestMethod.GET, RequestMethod.POST}, produces="text/html")
	public String getAuthentication(@ModelAttribute("user") User user, HttpSession session, BindingResult bindingResult)
	{
		if(bindingResult.hasErrors()) {
			return "login";
		}
		
		System.out.println(user.getUserName());
		
		if(user.getUserName().equalsIgnoreCase("Student"))
		{
			UserSession usersession=new UserSession(user.getId(),"STU");
			session.setAttribute("usersession",usersession);
			return "forward:/student/stugrades";
		}
		else if(user.getUserName().equalsIgnoreCase("Admin")) 
		{
			UserSession usersession=new UserSession(user.getId(),"ADM");
			session.setAttribute("usersession",usersession);
			
			return "forward:/admin/studentlist";
		}
		
		else if(user.getUserName().equalsIgnoreCase("Faculty"))
		{
			UserSession usersession=new UserSession(user.getId(),"FAC");
			session.setAttribute("usersession",usersession);
			
			return "forward:/faculty/assignedcourses";
		}
		
		else
			return "login";
	}
}
