package eredua.domeinua;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Erabiltzailea {
	@Id
	private String izena;
	private String pasahitza;
	private String mota;
	@OneToMany(targetEntity = LoginGertaera.class, mappedBy = "erabiltzailea", cascade = { CascadeType.REMOVE,
			CascadeType.PERSIST }, fetch = FetchType.EAGER)
	private Set<LoginGertaera> gertaerak;

	

	public Set<LoginGertaera> getGertaerak() {
		return gertaerak;
	}

	public void setGertaerak(Set<LoginGertaera> gertaerak) {
		this.gertaerak = gertaerak;
	}

	public Erabiltzailea() {
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {
		this.mota = mota;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	public String toString() { // Erabiltzailea
		return izena + "/" + pasahitza + "/" + mota;
	}
}
