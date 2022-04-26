package it.polito.tdp.poweroutages.model;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println(model.getNercList());

		PowerOutageDAO pdao = new PowerOutageDAO();
		System.out.print(pdao.getPowerOutages().get(0).getDuration());
	}

}
