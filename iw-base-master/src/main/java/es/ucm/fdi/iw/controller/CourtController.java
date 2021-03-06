package es.ucm.fdi.iw.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Court;
import es.ucm.fdi.iw.model.Reservation;


@Controller
@RequestMapping("court")
public class CourtController {
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("s", "/static");
    }
	
	/**
	 * Mapper para listar todas las pistas disponibles y mostrarlas a un administrador.
	 * @param m
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/pistas")
	public String pistas(Model m, HttpSession session) {
		if (session.getAttribute("role") == "admin") {
			List<Court> l = entityManager.createNamedQuery("allCourts").getResultList();
			
			m.addAttribute("list", l);
			return "pistas";
		} else {
			return "redirect:/error";
		}
	}
	
	/**
	 * Mapper para visualizar información de una determinada pista.
	 * @param id
	 * @param m
	 * @return
	 */
	@RequestMapping(value="/perfil-pista/{id}", method = RequestMethod.GET)
	public String perfilPista(@PathVariable("id") long id, Model m) {
		Court c = (Court) entityManager.find(Court.class, id);
		
		m.addAttribute("court", c);
		return "perfil-pista";
	}
	
	/**
	 * Mapper para listar todas las pistas disponibles y mostrarlas a un usuario.
	 * @param m
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/pistas-user")
	public String pistasUser(Model m, HttpSession session) {
		if (session.getAttribute("user") != null) {
			List<Court> l = entityManager.createNamedQuery("allCourts").getResultList();
			
			m.addAttribute("list", l);
			return "pistas-user";
		} else {
			return "redirect:/login";
		}
	}
	
	/**
	 * Mapper para mostrar formulario de crear una pista.
	 * @param session
	 * @return
	 */
	@GetMapping("/crear-pista")
	public String loginAdmin(HttpSession session) {
		if (session.getAttribute("role") == "admin") {
			return "crear-pista";
		}
		else {
			return "redirect:/error";
		}
	}
	
	/**
	 * Mapper para mostrar formulario y editar una determinada pista.
	 * @param id
	 * @param m
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/editar-pista/{id}", method = RequestMethod.GET)
	public String editarPista(@PathVariable("id") long id, Model m, HttpSession session) {
		if (session.getAttribute("role") == "admin") {
			Court c =  entityManager.find(Court.class, id);
			
			m.addAttribute("court", c);
			return "modificar-pista";
		}else {
			return "redirect:/error";
		}
		
	}
	
	/**
	 * Crea una nueva pista con la información proporcionada por un administrador.
	 * @param Nombre
	 * @param Precio
	 * @param Direccion
	 * @param Telefono
	 * @param Extras
	 * @param Descripcion
	 * @param photo
	 * @param m
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/newCourt", method = RequestMethod.POST)
	@Transactional
	public String newCourt(
			@RequestParam String Nombre, 
			@RequestParam Double Precio,
			@RequestParam String Direccion,
			@RequestParam String Telefono,
			@RequestParam String Extras,
			@RequestParam String Descripcion,
			@RequestParam("file") MultipartFile photo, Model m, HttpSession session) {
		
		
		Boolean errores = false;
		
		if (Nombre == "") {
			m.addAttribute("errorNombre", "Debe insertar un nombre");
			errores = true;
		}
		if (Precio.equals(null)) {
			m.addAttribute("errorPrecio", "Debe insertar un precio");
			errores = true;
		} 
		if (Precio.isNaN() || Precio <= 0) {
			m.addAttribute("errorPrecio", "El precio debe ser un número mayor que cero");
			errores = true;
		}
		if (Direccion == "") {
			m.addAttribute("errorDireccion", "Debe insertar una dirección");
			errores = true;
		}
		if (Telefono == "") {
			m.addAttribute("errorTelefono", "Debe insertar un teléfono");
			errores = true;
		}
		if (Extras == "") {
			m.addAttribute("errorExtras", "Debe insertar unos extras");
			errores = true;
		}
		if (Descripcion == "") {
			m.addAttribute("errorDescripcion", "Debe insertar una descripción");
			errores = true;
		}
		if (errores) {
			return "crear-pista";
		}
		
		
		
		Court c = new Court();
					
		Query q = entityManager.createNamedQuery("findCourtByName").setParameter("n", Nombre);
		
		if(q.getResultList().isEmpty()) {
			c.setDescription(Descripcion);
			c.setDir(Direccion);
			c.setExtras(Extras);
			c.setName(Nombre);
			c.setPhone(Telefono);
			c.setPrice(Precio);	
		}				
				
		entityManager.persist(c);
		
		try {
			byte[] bytes = photo.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(
                    		new FileOutputStream(localData.getFile("court", Long.toString(c.getId()))));
            stream.write(bytes);
            stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/court/pistas";
	}
	
	/**
	 * Elimina una pista permanentemente y las reservas asociadas a ella.
	 * @param id
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/deleteCourt/{id}", method=RequestMethod.POST)
	@Transactional
	public String deleteCourt(@PathVariable("id") long id,  HttpSession session) {
		
		if (session.getAttribute("role") == "admin") {
			Court c = entityManager.find(Court.class, id);
			List<Reservation> l = entityManager.createNamedQuery("reservationsCourt").setParameter("c", c).getResultList();
			
			for(Reservation r : l) {
				entityManager.remove(r);
			}
			
			entityManager.remove(c);
			
			return "redirect:/court/pistas";
		}else {
			return "redirect:/error";
		}
		
	}	
	
	/**
	 * Actualiza datos de una pista con la información proporcionada por administrador.
	 * @param idCourt
	 * @param Nombre
	 * @param Precio
	 * @param Direccion
	 * @param Telefono
	 * @param Extras
	 * @param Descripcion
	 * @param photo
	 * @param m
	 * @return
	 */
	@RequestMapping(value="/uploadCourt", method=RequestMethod.POST)
	@Transactional
	public String uploadCourt(
			@RequestParam long idCourt,
			@RequestParam String Nombre, 
			@RequestParam Double Precio,
			@RequestParam String Direccion,
			@RequestParam String Telefono,
			@RequestParam String Extras,
			@RequestParam String Descripcion,
			@RequestParam("file") MultipartFile photo, Model m) {
						
		Boolean errores = false;
		
		if (Nombre == "") {
			m.addAttribute("errorNombre", "Debe insertar un nombre");
			errores = true;
		}
		if (Precio.equals(null)) {
			m.addAttribute("errorPrecio", "Debe insertar un precio");
			errores = true;
		} 
		if (Precio.isNaN() || Precio <= 0) {
			m.addAttribute("errorPrecio", "El precio debe ser un número mayor que cero");
			errores = true;
		}
		if (Direccion == "") {
			m.addAttribute("errorDireccion", "Debe insertar una dirección");
			errores = true;
		}
		if (Telefono == "") {
			m.addAttribute("errorTelefono", "Debe insertar un teléfono");
			errores = true;
		}
		if (Extras == "") {
			m.addAttribute("errorExtras", "Debe insertar unos extras");
			errores = true;
		}
		if (Descripcion == "") {
			m.addAttribute("errorDescripcion", "Debe insertar una descripción");
			errores = true;
		}
		if (errores) {
			return "modificar-pista";
		}
		
		Court c = entityManager.find(Court.class, idCourt);
		
		c.setDescription(Descripcion);
		c.setName(Nombre);
		c.setPhone(Telefono);
		c.setExtras(Extras);
		c.setPrice(Precio);
		c.setDir(Direccion);
		
		try {
			byte[] bytes = photo.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(
                    		new FileOutputStream(localData.getFile("court", Long.toString(c.getId()))));
            stream.write(bytes);
            stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return "redirect:/court/pistas";
	}
	
	
	/**
	 * Returns a court's photo
	 * @param id of user to get photo from
	 * @return
	 */
	@RequestMapping(value="/photo/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void courtPhoto(@PathVariable("id") String id, 
			HttpServletResponse response) {
	    File f = localData.getFile("court", id);
	    InputStream in = null;
	    try {
		    if (f.exists()) {
		    	in = new BufferedInputStream(new FileInputStream(f));
		    } else {
		    	in = new BufferedInputStream(
		    			this.getClass().getClassLoader().getResourceAsStream("sin_imagen.jpg"));
		    }
	    	FileCopyUtils.copy(in, response.getOutputStream());
	    } catch (IOException ioe) {
	    	//log.info("Error retrieving file: " + f + " -- " + ioe.getMessage());
	    }
	}
	
}
