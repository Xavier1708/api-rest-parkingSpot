package com.api.parkingcontrol.services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;

@Service
public class ParkingSpotServices {
	
	/*Ponto de injeção*/

	final ParkingSpotRepository parkingSpotRepository;
	
	public ParkingSpotServices (ParkingSpotRepository parkingSpotRepository) {
		this.parkingSpotRepository = parkingSpotRepository;
	}

	
	@Transactional
	public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.save(parkingSpotModel);
	}


	// SERVICES FOR VERIFICATION IF EXIST OF DATA BASE
	public boolean existsByLicensePlateCar(String licensePlateCar) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
	}


	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
	}


	public boolean existsByApartmentAndBlock(String apartment, String block) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
	}


	// metodos para buscas
	public List<ParkingSpotModel> findAll() {
		// TODO Auto-generated method stub
		return parkingSpotRepository.findAll();
	}


	// ESTE METODO SERVE PARA PEGAR O ID LA DO BANCO 
	// USAMOS O OPTIONAL PARA FZR ISSO
	public Optional<ParkingSpotModel> findById(UUID id) {
		// TODO Auto-generated method stub
		return parkingSpotRepository.findById(id);
	}


	// SERVIÇO PARA DELETAR 
	@Transactional
	public void delete(ParkingSpotModel parkingSpotModel) {
		// TODO Auto-generated method stub
		 parkingSpotRepository.delete(parkingSpotModel);
		
	}

}
