Proceso Porcentaje_de_ocupacion
	
	Escribir 'Bienvenido al Teatro Moro';
	
	Definir asientosVendidos, totalAsientos Como Entero;
	Definir ocupacion Como Real;
	
	Escribir 'Ingrese la cantidad de asientos vendidos:';
	Leer asientosVendidos;
	
	totalAsientos = 150;
	ocupacion = (asientosVendidos / totalAsientos) * 100;
	
	Escribir 'El procentaje de ocupacion es del ', ocupacion, '%';
	
FinProceso