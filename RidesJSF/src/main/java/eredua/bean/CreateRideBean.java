package eredua.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import businessLogic.BLFacade;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import nagusia.GertaerakDataAccess;



public class CreateRideBean {
	private Date data;
	private String depart;
	private String array;
	private String price;
	private int seat;
	//private  BLFacade facadeBL=FacadeBean.getBusinessLogic();
	private GertaerakDataAccess da= new GertaerakDataAccess();

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}


	public void onDateSelect(AjaxBehaviorEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data aukeratua: " + data));
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getArray() {
		return array;
	}

	public void setArray(String array) {
		this.array = array;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public void createRide() {
		try {
			da.createRide( depart, array,data, seat, Float.parseFloat(price), "driver1@gmail.com");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ride ondo sortua",  "Ride ondo sortua"));
		} catch (RideMustBeLaterThanTodayException | RideAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	};
}
