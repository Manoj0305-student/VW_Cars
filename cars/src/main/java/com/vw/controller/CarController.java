package com.vw.controller;

import java.io.IOException;
import java.util.*;

import com.vw.entities.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vw.exceptions.ListOfCarIsEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vw.service.CarService;
import com.vw.dto.CarDto;
import com.vw.dto.ImageDto;





@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping(value="/")
public class CarController {

	@Autowired
	private CarService carService;

	@Autowired
	private ObjectMapper mapper;


	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//get all cars
	@GetMapping("cars")
	public List<CarDto> getCars(){

		List<CarDto> list = carService.getAllCars();
//		List<CarDto> list = new ArrayList<>();
		if(list.isEmpty()) {
			throw new ListOfCarIsEmptyException("No Data Found!!");
		}
		return list;
    }

	//get cars by id
	@GetMapping("car/{id}")
	public ResponseEntity <CarDto> getCar(@PathVariable int id) {
		CarDto carDto = carService.getCarById(id);
		return ResponseEntity.ok(carDto);
	}
	
	//get a car by name
	@GetMapping("searchName/{name}")
	public ResponseEntity<List<CarDto>> getCarsName(@PathVariable String name){
			List<CarDto> list = carService.getCarByName(name);
			logger.info(list.toString());
			if(list.isEmpty()) {
				throw new ListOfCarIsEmptyException("Data Not Found!!");
			}
			return ResponseEntity.ok(list);
	}
	
	//get a car by model
	@GetMapping("searchModel/{model}")
	public ResponseEntity<List<CarDto>> getCarsModel(@PathVariable Date model){
		List<CarDto> list = carService.getCarModel(model);
		if(list.isEmpty()) {
			throw new ListOfCarIsEmptyException("Data not found!!");
			
		}
		return ResponseEntity.ok(list);
		
	}
	
	//get car by brand
	@GetMapping("searchBrand/{brand}")
	public ResponseEntity<List<CarDto>> getCarsBrand(@PathVariable("brand") String brand){
		List<CarDto> list = carService.getCarBrand(brand);
		if(list.isEmpty()) {
			throw new ListOfCarIsEmptyException("Data not found!!");
		}
		return ResponseEntity.ok(list);
		
	}
	
	//get image by car id
	
	@GetMapping("searchImage/{id}")
	public ResponseEntity<Map<String,String>> getCarImage(@PathVariable Integer id){
		ImageDto img = carService.findImage(id);
		String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(img.getData());
		Map<String, String> jsonMap = new HashMap<>();
		jsonMap.put("content", encodeImage);
		return ResponseEntity.ok(jsonMap);
	}


	@PostMapping(value="cars",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Car> addCar(
			@RequestParam(value = "data") String carData,
			@RequestParam(value = "file") MultipartFile file) throws IOException {

		CarDto carDto = mapper.readValue(carData, CarDto.class);
		Car car = carService.addCar(carDto, file);
		logger.info("added car: {}",car);
		return ResponseEntity.status(HttpStatus.CREATED).body(car);
	}
	
	//delete a car
	@DeleteMapping("cars/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Map<String,String>> deleteCar(@PathVariable int id){
		
			carService.deleteCar(id);
			logger.info("Deleted id: {}",id);
			Map<String, String> jsonMap = new HashMap<>();
			jsonMap.put("message", "Deleted id: "+id);

			return ResponseEntity.ok(jsonMap);
	}
	
	//update a car
	@PutMapping(value = "cars/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Car> updateCar(@PathVariable int id,
            @RequestParam("data") String carData,
            @RequestPart("file") MultipartFile file) throws IOException {

		CarDto carDto = mapper.readValue(carData, CarDto.class);
			Car car = carService.updateCar(carDto, id, file);
			logger.info("updated id : {}",id);
			logger.info("{}",car);
			return ResponseEntity.ok(car);
}
}
