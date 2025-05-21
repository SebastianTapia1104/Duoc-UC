Algoritmo SistemaReservaTeatro
    // Definir variables
    Dimension fechasEventos[4];
    Dimension asientosSabado1[3,3];
	Dimension asientosSabado2[3,3];
	Dimension asientosDomingo1[3,3];
	Dimension asientosDomingo2[3,3];
    Definir fechaSeleccionada Como Entero;  // Número de fecha seleccionada
    Definir filaSabado1, filaSabado2, filaDomingo1, filaDomingo2 Como Entero;
	Definir columnaSabado1, columnaSabado2, columnaDomingo1, columnaDomingo2 Como Entero;
    Definir reservaExitosa Como Logico;
	Definir i Como Entero;
	Definir asientosSabado1, asientosSabado2, asientosDomingo1, asientosDomingo2 Como Entero;
	Definir fechasEventos Como Caracter;
	Definir fila, columna Como Entero;
    // Inicializar fechas de los eventos
	
    fechasEventos[0] <- "Sábado mañana";
	fechasEventos[1] <- "Sábado tarde";
    fechasEventos[2] <- "Domingo mañana";
    fechasEventos[3] <- "Domingo tarde";
	
    // Inicializar asientos (0 = disponible, 1 = ocupado)
    Para filaSabado1 <- 0 Hasta 2 Hacer
        Para columnaSabado1 <- 0 Hasta 2 Hacer
            asientosSabado1[filaSabado1,columnaSabado1] <- 0;  // Todos los asientos están inicialmente disponibles
        FinPara;
    FinPara;
	
	Para filaSabado2 <- 0 Hasta 2 Hacer
        Para columnaSabado2 <- 0 Hasta 2 Hacer
            asientosSabado2[filaSabado2,columnaSabado2] <- 0;  // Todos los asientos están inicialmente disponibles
        FinPara;
    FinPara;
	
	Para filaDomingo1 <- 0 Hasta 2 Hacer
        Para columnaDomingo1 <- 0 Hasta 2 Hacer
            asientosDomingo1[filaDomingo1,columnaDomingo1] <- 0;  // Todos los asientos están inicialmente disponibles
        FinPara;
    FinPara;
	
	Para filaDomingo2 <- 0 Hasta 2 Hacer
        Para columnaDomingo2 <- 0 Hasta 2 Hacer
            asientosDomingo2[filaDomingo2,columnaDomingo2] <- 0;  // Todos los asientos están inicialmente disponibles
        FinPara;
    FinPara;

    // Mostrar las fechas de los eventos
    Escribir "Selecciona una fecha de evento:";
    Para i <- 0 Hasta 3 Hacer
        Escribir i, ". ", fechasEventos[i];
    FinPara;
	
    // Solicitar al usuario que elija una fecha
    Escribir "Ingresa el número de la fecha seleccionada:";
    Leer fechaSeleccionada;
	Mientras fechaSeleccionada > 3 o fechaSeleccionada < 0 Hacer
		Escribir "Fecha erronea, escoge una fecha válida";
		Leer fechaSeleccionada;
	FinMientras
	Escribir "Escogiste: ", fechasEventos[fechaSeleccionada];
	
	
	//Funcionalidad para diferentes días
	Si fechaSeleccionada = 0 Entonces
		// Mostrar el estado de los asientos
		Escribir "Estado de los asientos (0 = disponible, 1 = ocupado):";
		Para filaSabado1 <- 0 Hasta 2 Hacer
			Para columnaSabado1 <- 0 Hasta 2 Hacer
				Escribir asientosSabado1[filaSabado1,columnaSabado1], " ";
			FinPara;
			Escribir "";
		FinPara;
		// Solicitar al usuario que seleccione un asiento
		Escribir "Selecciona una asiento (fila):";
		Leer fila;
		Mientras fila > 3 o fila < 0 Hacer
			Escribir "Fila erronea, escoge una fila válida";
			Leer fila;
		FinMientras
		Escribir "Selecciona una asiento (columna):";
		Leer columna;
		Mientras columna > 3 o columna < 0 Hacer
			Escribir "columna erronea, escoge una columna válida";
			Leer columna;
		FinMientras
		// Verificar si el asiento está disponible
		Si asientosSabado1[fila,columna] = 0 Entonces
			asientosSabado1[fila,columna] <- 1;  // Marcar como ocupado
			reservaExitosa <- Verdadero;
			Escribir "¡Reserva exitosa!";
		Sino
			reservaExitosa <- Falso;
			Escribir "Lo siento, el asiento seleccionado ya está ocupado.";
		FinSi;
	FinSi
	
	Si fechaSeleccionada = 1 Entonces
		// Mostrar el estado de los asientos
		Escribir "Estado de los asientos (0 = disponible, 1 = ocupado):";
		Para filaSabado2 <- 0 Hasta 2 Hacer
			Para columnaSabado2 <- 0 Hasta 2 Hacer
				Escribir asientosSabado2[filaSabado2,columnaSabado2], " ";
			FinPara;
			Escribir "";
		FinPara;
		// Solicitar al usuario que seleccione un asiento
		Escribir "Selecciona una asiento (fila):";
		Leer fila;
		Mientras fila > 3 o fila < 0 Hacer
			Escribir "Fila erronea, escoge una fila válida";
			Leer fila;
		FinMientras
		Escribir "Selecciona una asiento (columna):";
		Leer columna;
		Mientras columna > 3 o columna < 0 Hacer
			Escribir "columna erronea, escoge una columna válida";
			Leer columna;
		FinMientras
		// Verificar si el asiento está disponible
		Si asientosSabado2[fila,columna] = 0 Entonces
			asientosSabado2[fila,columna] <- 1;  // Marcar como ocupado
			reservaExitosa <- Verdadero;
			Escribir "¡Reserva exitosa!";
		Sino
			reservaExitosa <- Falso;
			Escribir "Lo siento, el asiento seleccionado ya está ocupado.";
		FinSi;
	FinSi
	
	Si fechaSeleccionada = 2 Entonces
		// Mostrar el estado de los asientos
		Escribir "Estado de los asientos (0 = disponible, 1 = ocupado):";
		Para filaDomingo1 <- 0 Hasta 2 Hacer
			Para columnaDomingo1 <- 0 Hasta 2 Hacer
				Escribir asientosDomingo1[filaDomingo1,columnaDomingo1], " ";
			FinPara;
			Escribir "";
		FinPara;
		// Solicitar al usuario que seleccione un asiento
		Escribir "Selecciona una asiento (fila):";
		Leer fila;
		Mientras fila > 3 o fila < 0 Hacer
			Escribir "Fila erronea, escoge una fila válida";
			Leer fila;
		FinMientras
		Escribir "Selecciona una asiento (columna):";
		Leer columna;
		Mientras columna > 3 o columna < 0 Hacer
			Escribir "columna erronea, escoge una columna válida";
			Leer columna;
		FinMientras
		// Verificar si el asiento está disponible
		Si asientosDomingo1[fila,columna] = 0 Entonces
			asientosDomingo1[fila,columna] <- 1;  // Marcar como ocupado
			reservaExitosa <- Verdadero;
			Escribir "¡Reserva exitosa!";
		Sino
			reservaExitosa <- Falso;
			Escribir "Lo siento, el asiento seleccionado ya está ocupado.";
		FinSi;
	FinSi
	
	Si fechaSeleccionada = 3 Entonces
		// Mostrar el estado de los asientos
		Escribir "Estado de los asientos (0 = disponible, 1 = ocupado):";
		Para filaDomingo2 <- 0 Hasta 2 Hacer
			Para columnaDomingo2 <- 0 Hasta 2 Hacer
				Escribir asientosDomingo2[filaDomingo2,columnaDomingo2], " ";
			FinPara;
			Escribir "";
		FinPara;
		// Solicitar al usuario que seleccione un asiento
		Escribir "Selecciona una asiento (fila):";
		Leer fila;
		Mientras fila > 3 o fila < 0 Hacer
			Escribir "Fila erronea, escoge una fila válida";
			Leer fila;
		FinMientras
		Escribir "Selecciona una asiento (columna):";
		Leer columna;
		Mientras columna > 3 o columna < 0 Hacer
			Escribir "columna erronea, escoge una columna válida";
			Leer columna;
		FinMientras
		// Verificar si el asiento está disponible
		Si asientosDomingo2[fila,columna] = 0 Entonces
			asientosDomingo2[fila,columna] <- 1;  // Marcar como ocupado
			reservaExitosa <- Verdadero;
			Escribir "¡Reserva exitosa!";
		Sino
			reservaExitosa <- Falso;
			Escribir "Lo siento, el asiento seleccionado ya está ocupado.";
		FinSi;
		
	FinSi
	
	//A Creo que pude añadir un while/Si para que se pidieran asientos hasta que el cliente quisiera, pero no me quedó tiempo, esta semana estuve muy ocupado con unos proyectos de un bootcamp y el trabajo.
	//Además de añadir de antemano un par de asientos ocupados
	//Tengo el coonocimiento y la capacidad para hacerlo, pero no el tiempo
	
FinAlgoritmo