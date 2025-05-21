Proceso Entradas_Teatro_Moro
	
	Escribir 'Bienvenido al Teatro Moro';
	
	Definir cantEntradasGeneral, cantEntradasEstudiante, valorGeneral, valorEstudiante, total Como Entero;
	
	Escribir 'Ingrese la cantidad de entradas Generales que desea:';
	Leer cantEntradasGeneral;
	
	Escribir 'Ingrese la cantidad de entradas Estudiante que desea:';
	Leer cantEntradasEstudiante;
	
	valorGeneral = 1000;
	valorEstudiante = 500;
	
	total = (cantEntradasGeneral * valorGeneral) + (cantEntradasEstudiante * valorEstudiante);
	
	Escribir  'El total de su comra es de:', total;
	
FinProceso