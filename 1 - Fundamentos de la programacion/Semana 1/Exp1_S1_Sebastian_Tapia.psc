Proceso Exp1_S1_Sebastian_Tapia
	
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
	
	Escribir 'Bienvenido al Teatro Moro';
	
	Definir asientosVendidos, totalAsientos Como Entero;
	Definir ocupacion Como Real;
	
	Escribir 'Ingrese la cantidad de asientos vendidos:';
	Leer asientosVendidos;
	
	totalAsientos = 150;
	ocupacion = (asientosVendidos / totalAsientos) * 100;
	
	Escribir 'El procentaje de ocupacion es del ', ocupacion, '%';
	
	Escribir 'Bienvenido al Teatro Moro';
	
	Definir entradas Como Entero;
	Definir parimpar Como Real;
	
	Escribir 'Ingrese la cantidad de entradas que desea:';
	Leer entradas;
	
	parimpar = entradas % 2;
	
	Si (parimpar = 0) Entonces
		Escribir 'La cantidad de entradas es par';
	Sino
		Escribir 'La cantidad de entradas es impar';
	FinSi
	
FinProceso