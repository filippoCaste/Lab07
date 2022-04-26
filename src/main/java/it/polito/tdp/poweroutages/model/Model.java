package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	private PowerOutageDAO podao;
	private Integer victims;
	private Long hours;
	private List<PowerOutage> result;
	
	public Model() {
		podao = new PowerOutageDAO();
		this.hours = Long.MIN_VALUE;
		victims = Integer.MIN_VALUE;
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<PowerOutage> getPowerOutageList(int i) {
		return podao.getPowerOutages(i);
	}
	
	public List<PowerOutage> getPowerOutage() {
		return podao.getPowerOutages();
	}
	
	public String maximizeVictims(final int X, final int Y, final Nerc nerc) {
		List<PowerOutage> outagesList = new ArrayList<>(this.getPowerOutageList(nerc.getId()));
		this.result = new ArrayList<>();
		
		this.maximize_recursive(0, outagesList, X, Y, new ArrayList<PowerOutage>(), 0, 0);
		String output="Il massimo numero di affected customers è: " + this.victims + "\nIl totale di ore di disservizio è stato di: " + this.hours +
				"\nI disservizi si sono verificati nelle seguenti date:";
		for(PowerOutage po : this.result) {
			output+="\n	 "+po.toString();
		}
		return output;
	}

	private void maximize_recursive(int level, List<PowerOutage> outagesList, int X, int Y, List<PowerOutage> partial, long hours, int affected) {
		// ending condition
		
		if(hours==Y) {
			this.result = partial;
			this.victims = affected;
			this.hours = hours;
			return ;
		}

		if(affected > this.victims && X > this.hours && !partial.isEmpty()) {
			this.result = partial;
			this.victims = affected;
			this.hours = hours;
			return ;
		}
		
		// recursive step
		
		for(PowerOutage po : outagesList) {
			if(level == 0) {
				partial.add(po);
			}
			else if((po.getDate_event_began().getYear() - partial.get(0).getDate_event_began().getYear()) > Y) {
				continue;
			}
			List<PowerOutage> newOutagesList = new ArrayList<>(outagesList);
			newOutagesList.remove(po);
			hours += po.getDuration();
			affected += po.getCustomers_affected();
			this.maximize_recursive(++level, newOutagesList, X, Y, partial, hours, affected);
			
			/* BACKTRACKING */
			level--;
			partial.remove(po);
			hours -= po.getDuration();
			affected -= po.getCustomers_affected();
	}			
	}
}
