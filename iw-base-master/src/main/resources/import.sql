--Usuarios
INSERT INTO USER VALUES('milagros@gmail.com','5896574','Av. Lima 14, 5ºB',TRUE,TRUE,'Milagros Peña','milagros','658978562','USER')
INSERT INTO PLAYER VALUES(NULL,'milagros@gmail.com')
INSERT INTO USER VALUES('pedro@ucm.es','5698745','Av. Mallorca 9, 2-izq',FALSE,TRUE,'Pedro Morell','pedro','658974563','USER')
INSERT INTO NORMAL VALUES('pedro@ucm.es')
INSERT INTO USER VALUES('laura@gmail.com',NULL,'C/La Toledana 159',FALSE,FALSE,'Laura Casas','laura','233654789','USER')
INSERT INTO NORMAL VALUES('laura@gmail.com')
INSERT INTO USER VALUES('admin@gmail.com','0098-55596','C/San Petardo 13, 2A',FALSE,TRUE,'Administrador','admin','916547893','ADMIN,USER')
INSERT INTO ADMIN VALUES('05950045D',1,'Supervisor','normal','admin@gmail.com')
INSERT INTO USER VALUES('teresa@ucm.es',NULL,'Av. Coronada 26, 3C',FALSE,FALSE,'Teresa Garc\u00eda','teresa','654987159','ADMIN,USER')
INSERT INTO ADMIN VALUES('01235874K',0,'Gerente matinal','Paraninfo Sur','teresa@ucm.es')

--Pistas
INSERT INTO COURT VALUES(1,'Pista de voleibol exterior con medidas de pista reglamentaria y red adaptada para categoría adulta femenina.','Av. Juan Herrera 1, Madrid','En caso de necesitar luces por poca visibilidad, el coste aumenta 5\u20ac/hora.\u000d\u000aVestuarios (por persona): 1,75 \u20ac.\u000d\u000aTodas estas tarifas son solo vigentes para la disputa de entrenamientos y partidos, no para la\u000d\u000aimpartici\u00f3n de cursos, jornadas, clinics, etc\u2026\u000d\u000aLas personas con discapacidad tendr\u00e1n una reducci\u00f3n del 50% en el precio de las tarifas. Si en el\u000d\u000acertificado de discapacidad se especifica que necesitan acompa\u00f1ante, \u00e9ste acceder\u00e1 gratis a la\u000d\u000ainstalaci\u00f3n. DESCUENTO A 12\u20ac/hora para vinculados a UCM.','Pista voleibol femenina - PARANINFO SUR','91258365',15.0E0)
INSERT INTO COURT VALUES(2,'Pista de cemento exterior adaptada a la categoria masculina.','Av. Juan Herrera 1, Madrid','Descuento para alumnos de la Universidad Complutense de Madrid.','Pista Volleyball Maculina - PARANINFO SUR','998812345',15.0E0)


--Reservas
INSERT INTO RESERVATION VALUES(1,'2018-06-10 00:00:00.000000',TRUE,1,'milagros@gmail.com')
INSERT INTO HORAS VALUES(1,'9:00')
INSERT INTO HORAS VALUES(1,'10:00')
INSERT INTO RESERVATION VALUES(2,'2018-06-10 00:00:00.000000',TRUE,1,'laura@gmail.com')
INSERT INTO HORAS VALUES(2,'11:00')
INSERT INTO HORAS VALUES(2,'13:00')