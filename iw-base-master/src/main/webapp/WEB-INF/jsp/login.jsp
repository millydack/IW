<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%@ include file="../jspf/header.jspf"%>

<div class="starter-template">
	<h1>Acceso</h1>

    <form action="/login" method="post">               
        <fieldset>
            <legend>Please Login</legend>
            <label for="Email">Email</label>
            <input type="text" id="username" name="email"/>        
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/>
            
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            
            <div class="form-actions">
                <button type="submit" class="btn">Log in</button>
                <a href="http://localhost:8080/register.jsp" class="btn btn-primary">Registro</a>
            </div>
        </fieldset>
    </form>

	<%@ include file="../jspf/authinfo.jspf"%>		
</div>

<%@ include file="../jspf/footer.jspf"%>
