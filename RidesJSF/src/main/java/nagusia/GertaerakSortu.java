package nagusia;

import eredua.HibernateUtil;
import eredua.domeinua.Driver;

import eredua.domeinua.LoginGertaera;

import org.hibernate.Query;
import org.hibernate.Session;
import java.util.*;

public class GertaerakSortu {
	public GertaerakSortu() {
	}

	private void createAndStoreLoginGertaera(String erabil, boolean login) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from Erabiltzailea where izena= :erabiltzailea");
		q.setParameter("erabiltzailea", erabil);
		List result = q.list();
		LoginGertaera e = new LoginGertaera();
		try {
			e.setErabiltzailea((Driver) result.get(0));
		} catch (Exception ex) {
			System.out.println("Errorea: erabiltzailea ez da existitzen" + ex.toString());
		}
		e.setLogin(login);
		
		session.persist(e);
		session.getTransaction().commit();
	}

	private List gertaerakZerrendatu() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from LoginGertaera").list();
		session.getTransaction().commit();
		return result;
	}

	


	public static void main(String[] args) {
		
	}
}
