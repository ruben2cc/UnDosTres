package unoDostres.ejercicio1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;


public class Ejercicio1 {
	
	private ArrayList<TreeSet<Numero>> conjuntos;
	private ArrayList<TreeSet<Numero>> combinaciones;
	private ArrayList<TreeSet<Numero>> candidatos;
	private ArrayList<TreeSet<Numero>> soluciones;
	private TreeSet<Numero> datos;
	private final int N;
	
	public Ejercicio1(ArrayList<int[]> entrada) {
		int[] A = entrada.get(0);
		int[] B = entrada.get(1);
		N = A[0];
		datos = new TreeSet<Numero>();
		conjuntos = new ArrayList<TreeSet<Numero>>();
		combinaciones = new ArrayList<TreeSet<Numero>>();
		candidatos = new ArrayList<TreeSet<Numero>>();
		soluciones = new ArrayList<TreeSet<Numero>>();
		for(int n: B) {
			datos.add(new Numero(n, 0));
		}
	}
	
	public static void main(String[] args) {
		// Languaje: Java
		
		// Data
		ArrayList<int[]> entrada = new ArrayList<int[]>();
		int[] A = {12,3};
		int[] B = {2,3,4};
		entrada.add(A);
		entrada.add(B);
 		
		// Object which solves the problem
		Ejercicio1 ejercicio1 = new Ejercicio1(entrada);
		
		// Example call
		ejercicio1.casosExtremos(ejercicio1.N, ejercicio1.datos);
				
	}
	
	public void hacerConjuntos() {

		boolean yaHayDivisor = false;
		
		TreeSet<Numero> treeSet = new TreeSet<Numero>();
		Iterator<Numero> iterator = datos.iterator();
		treeSet.add(new Numero(iterator.next()));
		conjuntos.add(treeSet);
		while(iterator.hasNext()) {
			Numero next = iterator.next();
			Iterator<TreeSet<Numero>> it = conjuntos.iterator();
			yaHayDivisor = false;
			while(it.hasNext()) {
				TreeSet<Numero> c = it.next();
				Numero first = c.first();
				if(next.divisor % first.divisor == 0) {
					c.add(new Numero(next));
					yaHayDivisor = true;
				}			
			}
			if(!yaHayDivisor) {
				Numero numero = new Numero(next);
				TreeSet<Numero> nuevoConjunto = new TreeSet<Numero>();
				nuevoConjunto.add(numero);
				conjuntos.add(nuevoConjunto);
			}
		}
	}
	
	public void obtenerSiguienteCombinacion() {
		TreeSet<Numero> treeSet;
		Iterator<TreeSet<Numero>> iterator = conjuntos.iterator();		
		TreeSet<Numero> next = iterator.next();
		Iterator<Numero> it = next.iterator();
		
		while(it.hasNext()) {
			treeSet=  new TreeSet<Numero>();
			Numero n = it.next();
			treeSet.add(n);
			combinaciones.add(treeSet);
		}

		productoCartesiano();
	}
	
	public void productoCartesiano() {
		Iterator<TreeSet<Numero>> iterator = conjuntos.iterator();
		TreeSet<Numero> conjunto = iterator.next();
		while(iterator.hasNext()) {
			conjunto = iterator.next();
			productoCartesianoBinario(conjunto);
		}
	}

	private void productoCartesianoBinario(TreeSet<Numero> conjunto) {
		Iterator<TreeSet<Numero>> iterator = combinaciones.iterator();
		ArrayList<TreeSet<Numero>> tempSoluciones= new ArrayList<TreeSet<Numero>>();
		TreeSet<Numero> tempSolucion = null;
		while(iterator.hasNext()) {
			TreeSet<Numero> s = iterator.next();
			Iterator<Numero> it = conjunto.iterator();
			while(it.hasNext()) {
				tempSolucion = new TreeSet<Numero>();
				TreeSet<Numero> clone = (TreeSet<Numero>)s.clone();
				Numero next = it.next();
				clone.add(next);
				tempSoluciones.add(clone);
			}
		}
		combinaciones = tempSoluciones;
	}
	
	public void encontrarCandidatos() {
		Iterator<TreeSet<Numero>> combinacionesIt = combinaciones.iterator();
		while(combinacionesIt.hasNext()) {
			TreeSet<Numero> combinacion = combinacionesIt.next();
			Iterator<Numero> divisorIt = combinacion.descendingIterator();
			
			int n = N;
			TreeSet<Numero> tempSolucion = new TreeSet<Numero>();
			while(divisorIt.hasNext()) {
				Numero numero = divisorIt.next();
				while(n % numero.divisor == 0) {
					n/=numero.divisor;					
					numero.conteo++;					
				}
				Numero tempNumero = new Numero(numero);
				numero.conteo=0;
				tempSolucion.add(tempNumero);
			}
			candidatos.add(tempSolucion);
		}
	}
	
	public void encontrarSoluciones() {
		Iterator<TreeSet<Numero>> candidatosIt = candidatos.iterator();
		while(candidatosIt.hasNext()) {
			TreeSet<Numero> candidato = candidatosIt.next();
			if(esSolucion(candidato)) {
				soluciones.add(candidato);
			}
		}
	}

	private boolean esSolucion(TreeSet<Numero> candidato) {
		Iterator<Numero> candidatoIt = candidato.iterator();
		int M = 1;
		while(candidatoIt.hasNext()) {
			Numero numero = candidatoIt.next();
			if(numero.conteo!=0) {
				M*=Math.pow(numero.divisor, numero.conteo);
			}
		}
		if(M==N) {
			return true;
		} else {
			return false;
		}
	}
	
	public void obtenerSolucion() {
		if(soluciones.size()>0) {
			int menorConteo = buscarElMenorConteo();
			TreeSet<Numero> solucion = encontrarSolucion(menorConteo);
			TreeSet<Numero> copiaSolucion = minimizarSolucion(solucion);
			String solucionString = formatoDeSolucion(copiaSolucion);
			System.out.println(solucionString);
		} else {
			System.out.println(-1);
		}
	}

	private TreeSet<Numero> minimizarSolucion(TreeSet<Numero> solucion) {
		TreeSet<Numero> copiaSolucion = copiaDeSolucion(solucion);
		Iterator<Numero> solucionIt = solucion.iterator();
		while(solucionIt.hasNext()) {
			Numero numero = solucionIt.next();
			if(numero.conteo>1) {
				TreeSet<Numero> divisoresComunes = encontrarConjuntoDeDivisoresComunes(numero);
				encontrarMultiplo(numero, divisoresComunes, copiaSolucion);
			}
		}
		return copiaSolucion;
	}

	private void encontrarMultiplo(Numero numero, TreeSet<Numero> divisoresComunes, TreeSet<Numero> copiaSolucion) {
		Iterator<Numero> divisoresComunesIt = divisoresComunes.descendingIterator();
		while(divisoresComunesIt.hasNext()) {
			Numero next = divisoresComunesIt.next();
			if(next.equals(numero)) break;
			double esPotenciaDeNumero = esPotenciaDeNumero(numero, next);
			if(esPotenciaDeNumero!=-1 && esPotenciaDeNumero <= numero.conteo) {
				minimizar(numero, next, copiaSolucion, esPotenciaDeNumero);
			}
		}		
	}
	
	private void minimizar(Numero numero, Numero next, TreeSet<Numero> copiaSolucion, double esPotenciaDeNumero) {
		Iterator<Numero> copiaSolucionIt = copiaSolucion.iterator();
		Numero n=null;
		while(copiaSolucionIt.hasNext()) {
			n = copiaSolucionIt.next();
			if(n.equals(numero))
				break;
		}
		copiaSolucion.add(next);
		
		while(n.conteo>= esPotenciaDeNumero) {
			n.conteo-=esPotenciaDeNumero;
			numero.conteo-=esPotenciaDeNumero;
			next.conteo++;
		}
	}

	private double esPotenciaDeNumero(Numero numero, Numero next) {
		
		double ln = logaritmoBaseB(next, numero.divisor);
		if(Math.floor(ln)==ln) {
			return ln;
		} else {		
			return -1;
		}
	}
	
	private double logaritmoBaseB(Numero numero, int b) {
		return Math.log(numero.divisor) / Math.log(b);
	}

	private TreeSet<Numero> encontrarConjuntoDeDivisoresComunes(Numero numero) {
		Iterator<TreeSet<Numero>> conjuntosIt = conjuntos.iterator();
		while(conjuntosIt.hasNext()) {
			TreeSet<Numero> conjunto = conjuntosIt.next();
			if(numero.divisor % conjunto.first().divisor == 0){
				return conjunto;
			} 	
		}
		return null;
	}

	private TreeSet<Numero> copiaDeSolucion(TreeSet<Numero> solucion) {
		TreeSet<Numero> solucionCopia = new TreeSet<Numero>();
		Iterator<Numero> solucionIt = solucion.iterator();
		while(solucionIt.hasNext()) {
			Numero numero = solucionIt.next();
			Numero copiaNumero = new Numero(numero);
			solucionCopia.add(copiaNumero);
		}
		return solucionCopia;
	}

	private String formatoDeSolucion(TreeSet<Numero> solucion) {
		Iterator<Numero> solucionIt = solucion.iterator();
		String numeros = "1 ";
		while(solucionIt.hasNext()) {
			Numero next = solucionIt.next();
			for(int i = 0; i < next.conteo ;i++) {
				numeros+=next.divisor + " ";
			}
		}
		return numeros;
	}

	private TreeSet<Numero> encontrarSolucion(int menorConteo) {
		Iterator<TreeSet<Numero>> solucionesIt = soluciones.iterator();
		while(solucionesIt.hasNext()) {
			TreeSet<Numero> solucion = solucionesIt.next();
			Iterator<Numero> solucionIt = solucion.iterator();
			int tempConteo=0;
			while(solucionIt.hasNext()) {
				Numero numero = solucionIt.next();
				tempConteo+=numero.conteo;
			}
			if(tempConteo==menorConteo)
				return solucion;
		}
		return null;
	}

	private int buscarElMenorConteo() {
		int menorConteo=0;
		Iterator<TreeSet<Numero>> solucionesIt = soluciones.iterator();
		while(solucionesIt.hasNext()) {
			TreeSet<Numero> solucion = solucionesIt.next();
			Iterator<Numero> solucionIt = solucion.iterator();
			int tempConteo=0;
			while(solucionIt.hasNext()) {
				Numero numero = solucionIt.next();
				tempConteo+=numero.conteo;
			}
			if(menorConteo==0)
				menorConteo=tempConteo;
			else {
				if(tempConteo<menorConteo)
					menorConteo=tempConteo;
			}
		}
		return menorConteo;
	}
	
	public void casosExtremos(int N, TreeSet<Numero> datos) {
		if(N == 0) {
			if(datosContieneCero(datos)) {
				System.out.println("1 0");
			} else {
				System.out.println(-1);
			}
				
		} else if(N>0) {
			if(N>1)
				if(datosContieneCero(datos)) {
					datos.remove(new Numero(0,0));
					sonDatosCorrectos(datos);
				} else {
					sonDatosCorrectos(datos);
				}
			else
				System.out.println(1);
		} else 
			System.out.println(-1);	
	}

	private void sonDatosCorrectos(TreeSet<Numero> datos) {
		descartarNegativos(datos);
		if(datos.size()>0) {		
			hacerConjuntos();
			obtenerSiguienteCombinacion();
			encontrarCandidatos();
			encontrarSoluciones();
			obtenerSolucion();			
		} else {
			System.out.println(-1);
		}
	}

	private void descartarNegativos(TreeSet<Numero> datos) {
		
		Iterator<Numero> datosIt = datos.iterator();
		while(datosIt.hasNext()) {
			Numero numero = datosIt.next();
			if(numero.divisor<0 || numero.divisor == 1)
				datosIt.remove();
		}
	}

	private boolean datosContieneCero(TreeSet<Numero> datos) {
		if(datos.contains(new Numero(0,0))) 
			return true;
		else
			return false;
	}
	
	
}
