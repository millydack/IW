<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<%@ include file="../jspf/header.jspf"%>

<div class="container">
		<h3>Tus reservas</h3>
		<ul class="list-group pistas">
			<c:forEach items="${list}" var="r">
	         	<li class="list-group-item">
	         		<img class="img-pista" src="${s}/img/rugby.jpg"/><span class="titulo-pista">${r.nameCourt}</span>
	         		<a href="#" class="link-masinfo-pista"><span class="glyphicon glyphicon-pencil"></span> Editar</a>
	         		<a href="#" class="link-masinfo-pista cancelar"><span class="glyphicon glyphicon-remove"></span> Cancelar</a>
	         		<hr class="division-reserva"/>
					<div class="info-reserva">
						<span class="fecha-reserva">${r.date}</span><br/>
						<h4>Horas</h4>
						<ul>
							<c:forEach items="${r.horas}" var="h">
								<li>
									<span class="hora-reseva">${h}</span>
								</li>
							</c:forEach>
						</ul>
					</div>
				</li>
	      	</c:forEach>
		</ul>
</div>

<%@ include file="../jspf/footer.jspf"%>