package unoDostres.ejercicio1;

public class Numero implements Comparable<Numero>{
	int divisor;
	int conteo;
	public Numero(int numero, int conteo) {
		this.divisor = numero;
		this.conteo = conteo;
	}
	public Numero(Numero numero) {
		this.divisor = numero.divisor;
		this.conteo = numero.conteo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + divisor;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Numero other = (Numero) obj;
		if (divisor != other.divisor)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Numero [divisor=" + divisor + ", conteo=" + conteo + "]";
	}
	@Override
	public int compareTo(Numero o) {
		
		if(this.divisor>o.divisor) {
			return 1;
		} else if(this.divisor<o.divisor) {
			return -1;
		} else {
			return 0;
		}
	}
}

