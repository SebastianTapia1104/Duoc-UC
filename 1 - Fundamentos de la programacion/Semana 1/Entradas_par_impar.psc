Proceso Entradas_par_impar
	
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