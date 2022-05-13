package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Model {
	
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(Model.class);
	
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
	
	public List<PowerOutage> getPowerOutageList(int nerc) {
		return podao.getPowerOutages(nerc);
	}
	
	public List<PowerOutage> getPowerOutage() {
		return podao.getPowerOutages();
	}
	
	public String maximizeVictims(final int X, final int Y, final Nerc nerc) {
		List<PowerOutage> outagesList = new ArrayList<>(this.getPowerOutageList(nerc.getId()));
		this.result = new ArrayList<>();
		
		this.maximize_recursive(outagesList, X, Y, new ArrayList<PowerOutage>(), 0, 0);
		String output="Il massimo numero di affected customers è: " + this.victims + "\nIl totale di ore di disservizio è stato di: " + this.hours +
				"\nI disservizi si sono verificati nelle seguenti date:";
		for(PowerOutage po : this.result) {
			output+="\n	"+po.toString();
		}
		return output;
	}

	private void maximize_recursive(List<PowerOutage> outagesList, int X, int Y, List<PowerOutage> partial, long hours, int affected) {
		// ending conditions
		
		if(hours > X) { // devo tornare indietro 
			return ;
		}

		if(affected > this.victims && hours <= X) {
			this.result = new ArrayList<>( partial);
			this.victims = affected;
			this.hours = hours;
		}
		
		// recursive step
		
		for(PowerOutage po : outagesList) {
			
			/* non migliora i tempi!! */
//			if(level!=0 && (po.getDate_event_began().getYear() - partial.get(0).getDate_event_began().getYear()) > Y) {
//				continue;
//			}
			partial.add(po);
			List<PowerOutage> newOutagesList = new ArrayList<>(outagesList);
			newOutagesList.remove(po);
			
			/* con variabili si ha leggero miglioramento perché chiama solo una volta */
			long add = po.getDuration();
			int addCus = po.getCustomers_affected();
			
			hours += add;
			affected += addCus;
			
			this.maximize_recursive(newOutagesList, X, Y, partial, hours, affected);

			// i tempi aumentano parecchio!
			// logger.info("Backtracking al livello {}", level);
			
			/* BACKTRACKING */
			partial.remove(po);
			hours -= add;
			affected -= addCus;
		}	
		return;
	}
}
