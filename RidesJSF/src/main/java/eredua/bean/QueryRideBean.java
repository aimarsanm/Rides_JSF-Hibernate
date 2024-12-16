package eredua.bean;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

//import businessLogic.BLFacade;
//import domain.*;
import nagusia.GertaerakDataAccess;
import eredua.domeinua.*;

public class QueryRideBean {
	private Date data;
	private String depart;
	private String array;
	private String hiri;
	private List<String> dcities;
	private List<String> acities;
	private List<String> hiriak;
	private List<Ride> rides;
	//private  BLFacade facadeBL=FacadeBean.getBusinessLogic();
	private GertaerakDataAccess da= new GertaerakDataAccess();
	public QueryRideBean(){
		setDcities();
		setHiriak(da.getHiriak());
		
	}	
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

	public List<String> getDcities() {
		return dcities;
	}

	public void setDcities() {
		
		try {
			this.dcities = da.getDepartCities();		
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getAcities() {
		return acities;
	}

	public List<Ride> getRides() {
		return rides;
	}
	
	public void updateAcities(AjaxBehaviorEvent event) {
	    try {
	        this.acities = da.getArrivalCities(getDepart());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void searchRides() {
	    try {
	        this.rides = da.getRides(getDepart(), getArray(), getData());
	        if (rides == null || rides.isEmpty()) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ez dago bidaiarik eskuragarri.", "Ez dago bidaiarik eskuragarri."));
	            }
	    } catch (Exception e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to fetch rides."));
	    }
	}
	public String searchRides3() {
	    try {
	        this.rides = da.getRides2(getDepart());
	        if (rides == null || rides.isEmpty()) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ez dago bidaiarik eskuragarri.", "Ez dago bidaiarik eskuragarri."));
	            }
	    } catch (Exception e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to fetch rides."));
	    }
	    return "hiriak2";
	}
	public String searchRides2(){
		FacesContext context = FacesContext.getCurrentInstance();
        
        if(depart != null) {
            rides = da.getRides2(depart); 
            
            
            
            if (rides.isEmpty()) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No rides found", "Ez dira bidairik aurkitu.");
                context.addMessage(null, message);
            }
        }
        context.getApplication().getNavigationHandler().handleNavigation(context, null, "Proba2?faces-redirect=false");
        return "hiriak2";
    }

	
	public String egiaztatu(){
		//if bat jarri ber det eta ikusi logeauta daon edo ez logeauta badao close bidali eta bestela index.
		
		return "index";
	}
	public List<String> getHiriak(){
		return hiriak;
	}
	public String getHiri() {
		return hiri;
	}
	public void setHiri(String hiri) {
		this.hiri = hiri;
	}
	public void setHiriak(List<String> hiriak) {
		this.hiriak = hiriak;
	}
	public String egiaztatu2(){
		if(this.depart !=null) {
			return "hiriak2";
		}else {
			return "";
		}
	}
}
