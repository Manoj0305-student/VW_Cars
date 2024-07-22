package com.vw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vw.dto.AppointmentDto;
import com.vw.dto.CarDto;
import com.vw.dto.ImageDto;
import com.vw.entities.Car;
import com.vw.exceptions.IdNotFoundException;
import com.vw.exceptions.ListOfCarIsEmptyException;
import com.vw.service.CarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers =  CarController.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objMapper;


    @MockBean
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private CarDto carDto;

    @BeforeEach
    public void setup(){
//        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
//        appointmentDtoList.add(new AppointmentDto(1,"Manoj","9019107853",new Date(2024,07,13,12, 30,00),"test-drive","jane.smith@example.com",1,"KA-0319850034761"));
//        carDto = new CarDto(1,"car1","audi",2020,
//                "petrol","hybrid","description",
//                "image.png");
    }

    @AfterEach
    void tearDown(){
        carDto = null;
    }

    @Test
    public void testGetAllCars() throws Exception {
        List<CarDto> list = new ArrayList<>();
        list.add(carDto);

        when(carService.getAllCars()).thenReturn(list);
       mockMvc.perform(get("/cars"))
                .andExpect(status().is2xxSuccessful()).
                andExpect(MockMvcResultMatchers
                        .jsonPath("$[0].id").value(1)).
               andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andDo(print());


    }

    @Test
    public void testGetAllCarsEmpty() throws Exception {
        when(carService.getAllCars()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/cars"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void testGetCarById() throws Exception{
        when(carService.getCarById(1)).thenReturn(carDto);
        mockMvc.perform(get("/car/{id}", 1)).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("car1")).andDo(print());


    }

    @Test
    public void testGetCarByIdNotFound() throws Exception{
        int id = 1;
        when(carService.getCarById(id)).thenThrow(new IdNotFoundException(id+ " not found!"));
        mockMvc.perform(get("/car/{id}",id))
                .andExpect(status().is4xxClientError()).andDo(print());

    }

    @Test
    public void testGetCarByName() throws Exception{
        String name = "car1";
        List<CarDto> list = new ArrayList<>();
        list.add(carDto);
        when(carService.getCarByName(name)).thenReturn(list);
        mockMvc.perform(get("/searchName/{name}",name))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].name").value("car1"))
                .andDo(print());

    }

    @Test
    public void testGetCarByNameNotFound() throws Exception{
        String name = "car1";
        when(carService.getCarByName("car1")).thenThrow(ListOfCarIsEmptyException.class);
        mockMvc.perform(get("/searchName/{name})", name))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void testGetCarByModel() throws Exception{
        int model= 2020;
        List<CarDto> list = new ArrayList<>();
        list.add(carDto);
        when(carService.getCarModel(anyInt())).thenReturn(list);
        mockMvc.perform(get("/searchModel/{model}", model))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].model").value(model))
                .andDo(print());

    }

    @Test
    public void testGetCarByModelNotFound() throws Exception{
            when(carService.getCarModel(anyInt())).thenThrow(new ListOfCarIsEmptyException("Data Not Found!!"));
            mockMvc.perform(get("/searchModel/{model}",2020))
                    .andExpect(status().is4xxClientError())
                    .andDo(print());
    }

    @Test
    public void testGetCarByBrand() throws Exception{
        String brand = "audi";
        List<CarDto> list = new ArrayList<>();
        list.add(carDto);
        when(carService.getCarBrand(brand)).thenReturn(list);
        mockMvc.perform(get("/searchBrand/{brand}",brand))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].brand").value(brand))
                .andDo(print());

    }

    @Test
    public void testGetCarByBrandNotFound() throws Exception{
        String brand = "audi";
        when(carService.getCarBrand(brand)).thenThrow(ListOfCarIsEmptyException.class);
        mockMvc.perform(get("/searchBrand/{brand})", brand))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void testGetCarByImage() throws Exception{
        ImageDto image  = new ImageDto();
        image.setImgName("image.png");
        image.setContentType("image/png");
        image.setData(new byte[]{});

        int id = 1;

        when(carService.findImage(id)).thenReturn(image);
        mockMvc.perform(get("/searchImage/{id}",id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("image/png"))
                .andDo(print());

    }

    @Test
    public void testGetCarByImageNotFound() throws Exception{
        int id = 1;
        when(carService.findImage(id)).thenThrow(new IdNotFoundException("1 not found!"));
        mockMvc.perform(get("/searchImage/{id}",id))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }



    @Test
    public void testAddCar() throws Exception {

        Car car = new Car(); // populate with necessary test data
        car.setCarId(1);
        car.setName("car1");
        String carData = objMapper.writeValueAsString(carDto);
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());

        when(carService.addCar(any(CarDto.class), any(MultipartFile.class))).thenReturn(car);

        mockMvc.perform(multipart("/cars")
                        .file(file)
                        .param("data",carData))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(car.getCarId()))
                .andExpect(jsonPath("$.name").value(car.getName()))
                .andDo(print());
}

    @Test
    public void testAddCarEmpty() throws Exception {
        Car car = new Car();
        String carData = objMapper.writeValueAsString(new CarDto());
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());

        when(carService.addCar(any(CarDto.class), any(MultipartFile.class))).thenReturn(car);
        String s= null;
        mockMvc.perform(multipart("/cars")
                        .file(file)
                        .param("data",carData))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.name").value(s))
                .andDo(print());
    }

    @Test
    public void testDeleteCar() throws Exception{
        int id = 1;
        mockMvc.perform(delete("/cars/{id}",id))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

    }

    @Test
    public void testDeleteCarNotFound() throws Exception{
        int id = 1;
        when(carController.deleteCar(id)).thenThrow(new IdNotFoundException("1 not found!"));
        mockMvc.perform(delete("/cars/{id}",id))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    public void testUpdateCar() throws Exception{

        Car car = new Car();
        car.setCarId(1);
        car.setName("car1");
        String carData = objMapper.writeValueAsString(carDto);
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());

        when(carService.updateCar(any(CarDto.class), anyInt(), any(MultipartFile.class))).thenReturn(car);

        mockMvc.perform(multipart(HttpMethod.PUT,"/cars/1")
                .file(file)
                .param("data",carData))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("car1"))
                .andDo(print());
    }

    @Test
    public void testUpdateCarNotFound() throws Exception{

        String carData = objMapper.writeValueAsString(new CarDto());
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());
        when(carService.updateCar(any(CarDto.class), anyInt(), any(MultipartFile.class))).thenThrow(new IdNotFoundException("Id not found!"));
        mockMvc.perform(multipart(HttpMethod.PUT,"/cars/{id}",1)
                        .file(file)
                        .param("data",carData))
                        .andExpect(status().is4xxClientError())
                        .andDo(print());

    }

}
