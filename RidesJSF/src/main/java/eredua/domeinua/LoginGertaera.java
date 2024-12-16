package eredua.domeinua;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class LoginGertaera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String deskribapena;
	
	@ManyToOne(targetEntity=Driver.class,cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.JOIN)
	private Driver erabiltzailea;
	private boolean login;

	public LoginGertaera() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeskribapena() {
		return deskribapena;
	}

	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}

	public Driver getErabiltzailea() {
		return erabiltzailea;
	}

	public void setErabiltzailea(Driver erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
		if (login)
			this.deskribapena = erabiltzailea.getName() + "k ongi egin du logina";
		else
			this.deskribapena = erabiltzailea.getName() + " login egiten saiatu da";
	}

	public String toString() { // LoginGertaera
		return id + "/" + deskribapena + "/" + erabiltzailea + "/" + login;
	}
}