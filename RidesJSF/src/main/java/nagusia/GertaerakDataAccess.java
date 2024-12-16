package nagusia;

import eredua.HibernateUtil;
import eredua.domeinua.Driver;
import eredua.domeinua.LoginGertaera;
import eredua.domeinua.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import configuration.UtilDate;

import java.util.*;

import javax.persistence.TypedQuery;

public class GertaerakDataAccess {
	public GertaerakDataAccess() {

	}

	public Driver createAndStoreDriver(String izena, String emaila) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Driver e = new Driver();
		e.setName(izena);
		e.setEmail(emaila);
		session.persist(e);
		session.getTransaction().commit();
		return e;
	}

	public LoginGertaera createAndStoreLoginGertaera(String erabiltzailea, boolean login) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		LoginGertaera e = new LoginGertaera();
		try {
			e.setErabiltzailea((Driver) session.get(Driver.class, erabiltzailea));
			e.setLogin(login);

			session.persist(e);
			session.getTransaction().commit();
		} catch (org.hibernate.PropertyValueException ex) {
			System.out.println("Errorea: data falta da ");
			e = null;
			session.getTransaction().rollback();
			e = null;
		}
		return e;
	}

	public List<LoginGertaera> getLoginGertaerak() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from LoginGertaera").list();
		// System.out.println("getLoginGertaerak() : " + result);
		session.getTransaction().commit();
		return result;
	}

	public List<LoginGertaera> getLoginGertaerakv1(String erabiltzaileaIzena) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery(
				"select lg from LoginGertaera lg inner join lg.erabiltzailea e where e.izena= :erabiltzaileaIzena");
		q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
		List result = q.list();
		session.getTransaction().commit();
		return result;
	}

	public List<LoginGertaera> getLoginGertaerakv2(String erabiltzaileaIzena) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("from LoginGertaera lg where lg.erabiltzailea.izena= :erabiltzaileaIzena");
		q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
		List result = q.list();
		session.getTransaction().commit();
		return result;
	}

	public List<LoginGertaera> getLoginGertaerakv3(String erabiltzaileaIzena) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(LoginGertaera.class).createCriteria("erabiltzailea")
				.add(Restrictions.eq("izena", erabiltzaileaIzena));
		List<LoginGertaera> result = c.list();
		session.getTransaction().commit();
		return result;
	}

	public List<Driver> getDriverrak() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from Erabiltzailea").list();
		session.getTransaction().commit();
		return result;
	}

	public Driver getDriver(String erabiltzaileaIzena) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query q = session.createQuery("select lg.erabiltzailea from LoginGertaera lg where izena= :erabiltzaileaIzena");
		q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
		List result = q.list();
		session.getTransaction().commit();
		return (Driver) result.get(0);
	}

	// mirar bien para q me haga el login las funciones de arriba
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		Query query = session.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.origin = :from AND r.destination = :to AND r.date BETWEEN :startDate AND :endDate");
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("startDate", firstDayMonthDate);
		query.setParameter("endDate", lastDayMonthDate);

		List dates = query.list();

		session.getTransaction().commit();
		return dates;
	}

	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides => from= " + from + " to= " + to + " date= " + date);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session
				.createQuery("SELECT r FROM Ride r WHERE r.origin = :from AND r.destination = :to AND r.date = :date"

				);
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("date", date);

		List rides = query.list();

		session.getTransaction().commit();
		return rides;
	}
	public List<Ride> getRides2(String to) {
		//System.out.println(">> DataAccess: getRides => from= " + from + " to= " + to + " date= " + date);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session
				.createQuery("SELECT r FROM Ride r WHERE   r.origin = :to "

				);
		query.setParameter("to", to);
		

		List rides = query.list();

		session.getTransaction().commit();
		return rides;
	}

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide => from= " + from + " to= " + to + " driver= " + driverEmail
				+ " date= " + date);

		Transaction tx = null; // Manejo explícito de la transacción
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();// Bloque try-with-resources para
																				// garantizar cierre de la sesión
			tx = session.beginTransaction();

			// Validar que la fecha sea futura
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}

			// Buscar al conductor (Driver)
			Driver driver = (Driver) session.get(Driver.class, driverEmail);
			if (driver == null) {
				throw new NullPointerException("Driver not found with email: " + driverEmail);
			}

			// Verificar si ya existe un ride con los mismos detalles
			if (driver.doesRideExists(from, to, date)) {
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}

			// Crear el nuevo ride y asociarlo al conductor
			Ride ride = driver.addRide(from, to, date, nPlaces, price);

			// Persistir los cambios en la base de datos
			session.saveOrUpdate(driver); // Actualiza el conductor y sus rides asociados automáticamente

			tx.commit(); // Confirmar la transacción
			return ride;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback(); // Revertir la transacción si ocurre un error
			}
			System.err.println("Error during ride creation: " + e.getMessage());
			throw e; // Re-lanzar la excepción para manejarla en niveles superiores
		}
	}

	public List<String> getArrivalCities(String from) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session
				.createQuery("SELECT DISTINCT r.destination FROM Ride r WHERE r.origin = :from ORDER BY r.destination");
		query.setParameter("from", from);

		List arrivingCities = query.list();

		session.getTransaction().commit();
		return arrivingCities;
	}

	public List<String> getDepartCities() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session.createQuery("SELECT DISTINCT r.origin FROM Ride r ORDER BY r.origin");

		List<String> cities = query.list();

		session.getTransaction().commit();
		return cities;

	}
	
	

	public boolean login(String emaila, String pass) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session
				.createQuery("SELECT d.emaila , d.pasahitza FROM Driver d WHERE d.emaila=:email and d.pasahitza:pass");
		query.setParameter("email", emaila);
		query.setParameter("pass", pass);
		List<String> result = query.list();

		session.getTransaction().commit();
		return (!result.isEmpty());
	}

	public List<String> getHiriak() {
	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    session.beginTransaction();

	    // Obtener ciudades de origen
	    Query queryOrigin = session.createQuery("SELECT DISTINCT r.origin FROM Ride r");
	    List<String> originCities = queryOrigin.list();

	    // Obtener ciudades de destino
	    Query queryDestination = session.createQuery("SELECT DISTINCT r.destination FROM Ride r");
	    List<String> destinationCities = queryDestination.list();

	    // Combinar y eliminar duplicados
	    Set<String> uniqueCities = new HashSet<>();
	    uniqueCities.addAll(originCities);
	    uniqueCities.addAll(destinationCities);

	    session.getTransaction().commit();
	    List<String> emaitza =new ArrayList<>(uniqueCities);
	    emaitza.remove(emaitza.size()-1);
	    return emaitza;
	}


	public static void main(String[] args) {
		Transaction tx = null;
		Session session = session = HibernateUtil.getSessionFactory().openSession();
		try {

			tx = session.beginTransaction();

			Calendar today = Calendar.getInstance();
			int month = today.get(Calendar.MONTH) + 1; // Hibernate utiliza meses del 1 al 12
			int year = today.get(Calendar.YEAR);

			if (month == 12) {
				month = 1;
				year += 1;
			}

			// Crear conductores
			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
			Driver driver3 = new Driver("driver3@gmail.com", "Test driver");

			// Crear viajes (rides)
			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
			driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);
			driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

			driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

			// Persistir los conductores (y sus rides asociadas)
			session.persist(driver1);
			session.persist(driver2);
			session.persist(driver3);

			// session.getTransaction().commit();
			System.out.println("Base de datos inicializada");
			tx.commit(); // Confirmar la transacción
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback(); // Revertir la transacción si ocurre un error
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // Asegúrate de cerrar la sesión al final
			}
		}

	}
}
