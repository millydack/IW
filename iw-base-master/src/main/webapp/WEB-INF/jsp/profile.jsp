<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<%@ include file="../jspf/header.jspf"%>

<div class="container">
    	<div class="perfil">
    		<div class="cabecera-perfil"><span class="nombre-perfil">${user.name}</span></div>
    		<div class="celda-perfil">
    			<h4>Info</h4>
    			<p>
		  		<span>${user.login}</span><br/>
		  		<span>${user.phone}</span><br/>
		  		<span>${user.dir}</span><br/>
		  		</p>
	  		</div>
		  	<div class="div-modificar"><a href="/user/editar" class="btn btn-modificar">Modificar perfil</a></div>
		</div>
</div>

<%@ include file="../jspf/footer.jspf"%>