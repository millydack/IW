package es.ucm.fdi.iw.controller;

import java.security.Principal;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Normal;
import es.ucm.fdi.iw.model.Player;
import es.ucm.fdi.iw.model.User;

@Controller	
public class RootController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EntityManager entityManager;
	
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("s", "/static");
    }

	@GetMapping({"/", "/index"})
	public String root(Model model, Principal principal) {
		//log.info(principal.getName() + " de tipo " + principal.getClass());		
		// org.springframework.security.core.userdetails.User
		return "login";
	}
	
	@GetMapping("/home")
	public String home(HttpSession session) {
		if (session.getAttribute("user") != null) {
			return "home";
		} else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/login-admin")
	public String loginAdmin() {
		return "login-admin";
	}

	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	
	@GetMapping("/logout")
	public String logout(
			
			HttpSession session) {
			
		
		session.invalidate();
		
		
		return "redirect:login";
		
		
	}
	
	/**
	 * Registra un nuevo usuario con los datos proporcionados por el usuario,
	 * además, gestiona el control de los campos del formulario.
	 * @param Nombre
	 * @param Email
	 * @param Direccion
	 * @param Telefono
	 * @param Password
	 * @param CodUcm
	 * @param checkboxPlayer
	 * @param checkboxValue
	 * @param isAdmin
	 * @param m
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	@Transactional
	public String addUser(
			@RequestParam String Nombre, 
			@RequestParam String Email,
			@RequestParam String Direccion,
			@RequestParam String Telefono,
			@RequestParam String Password,
			@RequestParam String CodUcm,
			@RequestParam (required=false,name="Player") String checkboxPlayer,
			@RequestParam ("UCM") String checkboxValue,			
			@RequestParam(required=false) String isAdmin, Model m, HttpSession session) {
		
		Boolean errores = false;
		if (Email == "") {
			m.addAttribute("errorEmail", "Debe insertar un email");
			errores = true;
		}
		if (Direccion == "") {
			m.addAttribute("errorDireccion", "Debe insertar una direccion");
			errores = true;
		}
		if (Telefono == "") {
			m.addAttribute("errorTelefono", "Debe insertar un telefono");
			errores = true;
		}
		if (Nombre == "") {
			m.addAttribute("errorNombre", "Debe insertar un nombre");
			errores = true;
		}
		if (Password == "") {
			m.addAttribute("errorPassword", "Debe insertar una contraseña");
			errores = true;
		}
		if (errores) {
			return "register";
		}
		
		User u = null;
		if(checkboxPlayer != null && checkboxPlayer.equals("option1")) {
			u = new Player();
		}else {
			u = new Normal();
		}
		u.setLogin(Email);
		u.setPassword(passwordEncoder.encode(Password));
		u.setDir(Direccion);
		u.setName(Nombre);
		u.setPhone(Telefono);
		
		if(checkboxValue.equals("option1")) {
			u.setUCM(true);
			u.setCodUCM(CodUcm);
			if (CodUcm == "") {
				m.addAttribute("errorCodUcm", "Debe insertar un código UCM");
				errores = true;
			}
			
			if(checkboxPlayer.equals("option1")) {
				u.setPlayer(true);
			}else {
				u.setPlayer(false);
			}
			
		}else {
			u.setUCM(false);
		}
		
		u.setRoles("on".equals(isAdmin) ? "ADMIN,USER" : "USER");
				
		entityManager.persist(u);
		
		session.setAttribute("user", u.getLogin());
		session.setAttribute("role", "user");
		
		return "home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@Transactional
	public String login(@RequestParam String Email,
						@RequestParam String Password, Model m, HttpSession session) {
		
		Boolean errores = false;
		if (Email == "") {
			m.addAttribute("errorEmail", "Debe insertar un email");
			errores = true;
		}
		if (Password == "") {
			m.addAttribute("errorPassword", "Debe insertar una contraseña");
			errores = true;
		}
		if (errores) {
			return "login";
		}
		try {
			User u = entityManager.find(User.class, Email);
			session.setAttribute("user", u.getLogin());	
			if(passwordEncoder.matches(Password, u.getPassword())) {
				session.setAttribute("role", "user");	
				return "home";
			}else {
				m.addAttribute("error", "El usuario introducido no es correcto");
				return "login";
			}
		} catch (Exception e) {
			m.addAttribute("error", "El usuario introducido no es correcto");
			return "redirect:/login";
		}
			
	}
	
	@RequestMapping(value = "/login-admin", method = RequestMethod.POST)
	@Transactional
	public String loginAdmin(@RequestParam String Email,
						@RequestParam String Password, Model m, HttpSession session) {
		User u = entityManager.find(User.class, Email);
	
		session.setAttribute("user", u.getLogin());	
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(passwordEncoder.matches(Password, u.getPassword()) && u.getRoles().contains("ADMIN")) {
			session.setAttribute("role", "admin");
			return "redirect:/home";
		}else {
			return "login-admin";
		}
			
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	@Transactional
	public String removeUser(Model m, HttpSession session) {		
		User u = entityManager.find(User.class, session.getAttribute("user") );
		entityManager.remove(u);
		
		return "redirect:/login";
	}
}
