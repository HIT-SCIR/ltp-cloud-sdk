package edu.hit.ir.ltpService;

//import java.io.Serializable;

/**
 * @author http://developer.51cto.com/art/200905/124743.htm
 *
 */
public final class SRL {

	public final String type;
	public final int beg;
	public final int end;
	
	public SRL(String type, int beg, int end) {
		this.type = type;
		this.beg = beg;
		this.end = end;
	}
/*
	public static SRL of(String type, int beg, int end) {
		return new SRL(type, beg, end);
	}
//*/
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final SRL other = (SRL) obj;
		if (this.type != other.type &&
				(this.type == null || !this.type.equals(other.type))) {
			return false;
		}

		if (this.beg != other.beg ) {
			return false;
		}

		if (this.end != other.end ) {
			return false;
		}
		return true;
	}
/*
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);
		hash = 37 * hash + (this.beg != null ? this.beg.hashCode() : 0);
		hash = 37 * hash + (this.end != null ? this.end.hashCode() : 0);
		return hash;
	}
//*/
	@Override
	public String toString() {
		return String.format("SRL[%s,%d,%d]", type, beg, end);
	}

}
