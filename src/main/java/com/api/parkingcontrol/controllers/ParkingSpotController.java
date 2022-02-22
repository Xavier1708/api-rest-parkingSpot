package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.ParkingSpotDtos;
import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotServices;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	
	final ParkingSpotServices parkingSpotServices;
	
	public ParkingSpotController(ParkingSpotServices parkingSpotServices) {
		this.parkingSpotServices = parkingSpotServices;
	}
	
	
	@PostMapping
	public ResponseEntity<Object> saveParkingSpot (@RequestBody @Valid ParkingSpotDtos parkingSpotDto){
		
		// VERIFICAÇÕES ANTES DE SALVAR (SE JA EXISTE NO BANCO )
		if(parkingSpotServices.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plater Car is already in use");
		}
		
		if(parkingSpotServices.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		
		
		if(parkingSpotServices.existsByApartmentAndBlock(parkingSpotDto.getApartment(),parkingSpotDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block");
		}
		
		var parkingSpotModel = new ParkingSpotModel();
		
		/*VAMOS CONVERTER UM DTO PARA MODEL ANTES DE SALVAR*/
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		
		/*TEM HAVER COM A DATA */
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotServices.save(parkingSpotModel));
	}
	
	// METODO PARA BUSCAR TODOS
	@GetMapping
	public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpot(){
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotServices.findAll());
	}
	
	// METODO PARA BUSCAR APENAS UM (PELO ID)
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOnParkingSpot(@PathVariable(value = "id") UUID id){
		Optional<ParkingSpotModel> parkingModelOptional = parkingSpotServices.findById(id);
		
		if(!parkingModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingModelOptional.get());
	}
	
	// METODO PARA APAGAR
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
		
		Optional<ParkingSpotModel> parkingModelOptional = parkingSpotServices.findById(id);
		if(!parkingModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
		}
		parkingSpotServices.delete(parkingModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully");
		
	}
	
	// METODO PARA ACTUALIZAR 
	@PutMapping("/{id}")
	public ResponseEntity<Object> upadateParkingSpot(@PathVariable(value = "id") UUID id,@RequestBody @Valid ParkingSpotDtos parkingSpotDto){
		
		Optional<ParkingSpotModel> parkingModelOptional = parkingSpotServices.findById(id);
		if(!parkingModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
		}
		
		/*
		 * ESTA É UMA FORMA DE SE FAZER A ACTUALIZAÇÃO MAS TEM OUTRA MAIS VIAVÉL QUE VAMOS VER LOGO ABAIXO
		var parkingSpotModel = parkingModelOptional.get();
		parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
		parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
		parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
		parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
		parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
		parkingSpotModel.setResponsableName(parkingSpotDto.getResponsableName());
		parkingSpotModel.setApartment(parkingSpotDto.getApartment());
		parkingSpotModel.setBlock(parkingSpotDto.getBlock()); */
		
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setId(parkingModelOptional.get().getId());
		parkingSpotModel.setRegistrationDate(parkingModelOptional.get().getRegistrationDate());
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotServices.save(parkingSpotModel));
	}
	

	

}
