package com.vw.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.vw.dto.CarDto;
import com.vw.dto.ImageDto;
import com.vw.entities.Car;
import com.vw.exceptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vw.repo.CarRepo;

@Service
public class CarService {
	
	@Autowired 
	private  CarRepo carRepo;

	public CarService(CarRepo carRepo) {
		this.carRepo = carRepo;
	}

	//car to DTO converter
	public CarDto convertCartoDto(Car car) {
		CarDto carDto = new CarDto();
		carDto.setId(car.getCarId());
		carDto.setName(car.getName());
		carDto.setBrand(car.getBrand());
		carDto.setModel(car.getModel());
		carDto.setFuel(car.getFuel());
		carDto.setType(car.getType());
		carDto.setDescription(car.getDescription());
		carDto.setAppointmentDtoList(car.getAppointmentList());
		carDto.setPrice(car.getPrice());
		carDto.setTransmission(car.getTransmission());
		carDto.setColor(car.getColor());
		String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(car.getImgData());
		carDto.setContent(encodeImage);
		return carDto;

	}


	
//	 find all cars using dto
	public  List<CarDto> getAllCars(){
        return carRepo.findAll().
				stream().
				map(this::convertCartoDto)
				.toList();
	}



//	 find car by id
	public CarDto getCarById(int id) {
		Car carObj = carRepo.findById(id).orElseThrow(() -> new IdNotFoundException(id+" not found!"));
			return convertCartoDto(carObj);
	}

	//get cars by name
	public List<CarDto> getCarByName(String name) {
		return carRepo.findByName(name).stream().map(this::convertCartoDto).toList();

	}

	//get car by model
	public List<CarDto> getCarModel(Date model) {
		return carRepo.findByModel(model).stream().map(this::convertCartoDto).toList();
	}

	//get car by brand
	public List<CarDto> getCarBrand(String brand) {
		return carRepo.findByBrand(brand).stream().map(this::convertCartoDto).toList();
	}

	//get image by car id
	public ImageDto findImage(Integer id) {
		Car c = carRepo.findById(id).orElseThrow(()-> new IdNotFoundException(id+" not found for image operation!"));
		ImageDto img = new ImageDto();
		img.setContentType(c.getContentType());
		img.setImgName(c.getImgName());
		img.setData(c.getImgData());
		return img;

	}

	public Car addCar(CarDto carDto, MultipartFile file) throws IOException {
		Car carObj = new Car();
		carObj.setName(carDto.getName());
		carObj.setBrand(carDto.getBrand());
		carObj.setModel(carDto.getModel());
		carObj.setFuel(carDto.getFuel());
		carObj.setType(carDto.getType());
		carObj.setDescription(carDto.getDescription());
		carObj.setTransmission((carDto.getTransmission()));
		carObj.setColor(carDto.getColor());
		carObj.setImgName(file.getOriginalFilename());
		carObj.setContentType(file.getContentType());
		carObj.setPrice(carDto.getPrice());
		carObj.setImgData(file.getBytes());

		return this.carRepo.save(carObj);
	}
	
	//deleting a car using id
	public void deleteCar(int id) {
		if(carRepo.existsById(id))
			this.carRepo.deleteById(id);
		else
			throw new IdNotFoundException(id+" not found for delete operation!");
	}
	
	//updating the car with id
	public Car updateCar(CarDto carDto, int id, MultipartFile file) throws IOException {
		
		Car carObj = carRepo.findById(id).orElseThrow(
				()-> new IdNotFoundException(id+" not found!")
		);
		carObj.setName(carDto.getName());
		carObj.setBrand(carDto.getBrand());
		carObj.setModel(carDto.getModel());
		carObj.setFuel(carDto.getFuel());
		carObj.setType(carDto.getType());
		carObj.setDescription(carDto.getDescription());
		carObj.setTransmission(carDto.getTransmission());
		carObj.setColor(carDto.getColor());
		carObj.setImgName(file.getOriginalFilename());
		carObj.setContentType(file.getContentType());
		carObj.setImgData(file.getBytes());
		carObj.setAppointmentList(carDto.getAppointmentDtoList());
		carObj.setPrice(carDto.getPrice());
		carRepo.save(carObj);
		return carObj;
	}
}

