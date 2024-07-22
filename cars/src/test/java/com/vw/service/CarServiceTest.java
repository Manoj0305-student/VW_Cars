package com.vw.service;


import com.vw.repo.CarRepo;
import com.vw.dto.CarDto;
import com.vw.dto.ImageDto;
import com.vw.entities.Appointment;
import com.vw.entities.Car;
import com.vw.exceptions.IdNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepo carRepo;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        List<Appointment> appointmentList = new ArrayList<>();
        car = new Car(1, "car1", "audi",
                2020, "petrol", "hybrid", "some text",
                "car1.png", "image/png", 0.0,new byte[]{}, appointmentList);
    }

    @Test
    public void testGetAllCars() {
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setName("Car1");
        car1.setImgData(new byte[]{});

        Car car2 = new Car();
        car2.setCarId(2);
        car2.setName("Car2");
        car2.setImgData(new byte[]{});

        when(carRepo.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<CarDto> cars = carService.getAllCars();
        assertEquals(2, cars.size());
        verify(carRepo, times(1)).findAll();
    }

    @Test
    public void testGetAllCarsNoReturn() {

        when(carRepo.findAll()).thenReturn(Collections.emptyList());
        List<Car> result = carRepo.findAll();
        assertTrue(result.isEmpty());
    }


    @Test
    public void testGetCarById() {
        when(carRepo.findById(1)).thenReturn(Optional.of(car));

        CarDto carDto = carService.getCarById(1);
        assertNotNull(carDto);
        assertEquals("car1", carDto.getName());
        verify(carRepo, times(1)).findById(1);
    }

    @Test
    public void testGetCarByIdNotFound() {
        when(carRepo.findById(anyInt())).thenReturn(Optional.empty());
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> carService.getCarById(1));
        assertTrue(exception.getMessage().contains("1 not found"));
        verify(carRepo, times(1)).findById(1);
    }


    @Test
    public void testAddCar() throws IOException {
        Car car = new Car();
        car.setName("Car1");

        CarDto carDto = new CarDto();
        carDto.setName("Car1");

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{});
        when(file.getContentType()).thenReturn("image/jpeg");
        when(carRepo.save(any(Car.class))).thenReturn(car);
        Car savedCar = carService.addCar(carDto, file);
        assertNotNull(savedCar);
        assertEquals("Car1", savedCar.getName());
        verify(carRepo, times(1)).save(any(Car.class));
    }

    @Test
    public void testAddCarIsEmpty() throws IOException {
        Car car = new Car();
        CarDto carDto = new CarDto();

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{});
        when(carRepo.save(any(Car.class))).thenReturn(car);

        Car savedCar = carService.addCar(carDto, file);
        assertNull(savedCar.getName());
        assertNull(savedCar.getImgData());
        verify(carRepo, times(1)).save(any(Car.class));
    }

    @Test
    public void testDeleteCar() {
        Car car = new Car();
        car.setCarId(1);
        carRepo.save(car);
        when(carRepo.existsById(1)).thenReturn(true);
        carService.deleteCar(1);
        assertTrue(carRepo.existsById(1));
        verify(carRepo, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteCarNotFound() {

        when(carRepo.existsById(1)).thenReturn(false);
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> carService.deleteCar(1));
        assertTrue(exception.getMessage().contains("1 not found for delete operation!"));
        verify(carRepo, times(1)).existsById(1);
    }

    @Test
    public void testUpdateCar() throws IOException {
        Car car = new Car();
        car.setCarId(1);
        car.setName("OldName");

        CarDto carDto = new CarDto();
        carDto.setName("NewName");

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{});
        when(file.getContentType()).thenReturn("image/jpeg");

        when(carRepo.findById(1)).thenReturn(Optional.of(car));
        when(carRepo.save(any(Car.class))).thenReturn(car);

        Car updatedCar = carService.updateCar(carDto, 1, file);
        assertNotNull(updatedCar);
        assertEquals("NewName", updatedCar.getName());
        verify(carRepo, times(1)).findById(1);
        verify(carRepo, times(1)).save(any(Car.class));
    }

    @Test
    public void testUpdateCarNotFound() throws IOException {
        CarDto carDto = new CarDto();
        MultipartFile file = mock(MultipartFile.class);

        when(carRepo.findById(anyInt())).thenReturn(Optional.empty());

        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> carService.updateCar(carDto, 1, file));


        assertTrue(exception.getMessage().contains("1 not found!"));
        assertEquals(Optional.empty(), carRepo.findById(1));

    }

    @Test
    public void testGetCarByName() {
        Car car1 = new Car();
        car1.setName("Car1");
        car1.setImgData(new byte[]{});

        when(carRepo.findByName("Car1")).thenReturn(List.of(car1));

        List<CarDto> cars = carService.getCarByName("Car1");
        assertEquals(1, cars.size());
        assertEquals("Car1", cars.get(0).getName());
        verify(carRepo, times(1)).findByName("Car1");
    }

    @Test
    public void testGetCarByNameNotFound() {

        when(carRepo.findByName("name")).thenReturn(Collections.emptyList());
        List<Car> result = carRepo.findByName("name");
        assertTrue(result.isEmpty());
    }


    @Test
    public void testGetCarModel() {



        when(carRepo.findByModel(2020)).thenReturn(List.of(car));

        List<CarDto> cars = carService.getCarModel(2020);
        assertEquals(1, cars.size());
        assertEquals(2020, cars.get(0).getModel());
        verify(carRepo, times(1)).findByModel(2020);
    }

    @Test
    public void testGetCarModelNotFound() {

        when(carRepo.findByModel(2000)).thenReturn(Collections.emptyList());
        List<Car> result = carRepo.findByModel(2000);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCarBrand() {
        Car car1 = new Car();
        car1.setBrand("Brand1");
        car1.setImgData(new byte[]{});

        when(carRepo.findByBrand("Brand1")).thenReturn(List.of(car1));
        List<CarDto> cars = carService.getCarBrand("Brand1");
        assertEquals(1, cars.size());
        assertEquals("Brand1", cars.get(0).getBrand());
        verify(carRepo, times(1)).findByBrand("Brand1");
    }

    @Test
    public void testGetCarBrandNotFound() {

        when(carRepo.findByBrand("brand")).thenReturn(Collections.emptyList());
        List<Car> result = carRepo.findByBrand("brand");
        assertTrue(result.isEmpty());
    }


    @Test
    public void testFindImage() {
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setImgName("image1.png");

        when(carRepo.findById(1)).thenReturn(Optional.of(car1));
        ImageDto image1 = carService.findImage(1);

        assertEquals("image1.png", image1.getImgName());
        verify(carRepo, times(1)).findById(1);
    }

    @Test
    public void testFindImageNotFound() {
        when(carRepo.findById(1)).thenReturn(Optional.empty());
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> carService.findImage(1));
        assertTrue(exception.getMessage().contains("1 not found for image operation!"));
        assertEquals(Optional.empty(), carRepo.findById(1));
    }
}
