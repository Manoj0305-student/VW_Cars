package com.vw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vw.dto.CarDto;
import com.vw.entities.Appointment;
import com.vw.entities.Car;
import com.vw.repository.TestH2Repository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
//@ActiveProfiles("test")
public class CarsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @Autowired
    private TestH2Repository testRepo;
    private Car savedCar;

    @Nested
    public class NestedClass{

        @BeforeEach
        public  void setup(){
            List<Appointment> appointmentList =new ArrayList<>();
//            savedCar = new Car(1,"car1","audi",
//                    2020,"petrol","hybrid","some text",
//                    "car1.png","image/png",new byte[]{},appointmentList);
//            testRepo.deleteAll();
            testRepo.save(savedCar);
        }

        @AfterEach
        public void remove(){
            testRepo.deleteAll();
        }


        @Test
        public void testGetAllCars() throws Exception {
            log.info("get cars: {}",testRepo.findAll());
            mockMvc.perform(get("/cars"))
                    .andExpect(status().is2xxSuccessful()).
                    andExpect(MockMvcResultMatchers
                            .jsonPath("$[0].name").value("car1")).
                    andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                    .andDo(print());
        }

        @Test
        public void testGetCarById() throws Exception{
            List<Car> car = testRepo.findAll();
            int id = car.get(0).getCarId();
            log.info("savedCar: {}",testRepo.findAll());

            mockMvc.perform(get("/car/{id}", id))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.name").value("car1"))
                    .andDo(print());

        }

        @Test
        public void testGetCarByName() throws Exception{
            String name = "car1";
            mockMvc.perform(get("/searchName/{name}",name))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.[0].name").value(name))
                    .andDo(print());

        }

        @Test
        public void testGetCarByModel() throws Exception{
            int model= 2020;

            mockMvc.perform(get("/searchModel/{model}", model))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.[0].model").value(model))
                    .andDo(print());

        }

        @Test
        public void testGetCarByBrand() throws Exception{
            String brand = "audi";

            mockMvc.perform(get("/searchBrand/{brand}",brand))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.[0].brand").value(brand))
                    .andDo(print());

        }

        @Test
        public void testGetCarByImage() throws Exception{
            List<Car> car = testRepo.findAll();
            int id = car.get(0).getCarId();
            log.info("savedCar id = {} : {}",id,testRepo.findAll());
            mockMvc.perform(get("/searchImage/{id}",id))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath(("$.content")).value(""))
                    .andDo(print());

        }

        @Test
        public void testAddCar() throws Exception {

            String carData = "{\"name\": \"Test Car\", \"brand\": \"Test Brand\", \"model\": \"2024\", \"price\": 20000}";
            MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());
            log.info("savedCar: {}",testRepo.findAll());
            mockMvc.perform(multipart("/cars")
                            .file(file)
                            .param("data",carData))
                    .andExpect(status().isCreated())
                    .andDo(print()).andReturn();

            log.info("cars: {}",testRepo.findAll());

        }

        @Test
        public void testDeleteCar() throws Exception{
//            int id = savedCar.getId();
            List<Car> car = testRepo.findAll();
            int id = car.get(0).getCarId();
            log.info("savedCar: {}",testRepo.findAll());
            mockMvc.perform(delete("/cars/{id}",id))
                    .andExpect(status().is2xxSuccessful())
                    .andDo(print());
            log.info("cars: {}",testRepo.findAll());

        }


        @Test
        public void testUpdateCar() throws Exception{
//            int id = savedCar.getId();
            List<Car> car = testRepo.findAll();
            int id = car.get(0).getCarId();
            String carData = "{\"name\": \"Test Car\", \"brand\": \"Test Brand\", \"model\": \"2024\", \"price\": 20000}";
            MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());

            log.info("savedCar : {}",testRepo.findAll());
            mockMvc.perform(multipart(HttpMethod.PUT,"/cars/{id}",id)
                            .file(file)
                            .param("data",carData))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.name").value("Test Car"))
                    .andDo(print());
            log.info("upated car: {}",testRepo.findAll());
        }


    }

// negative testcases
    @BeforeEach
    public  void init(){
        testRepo.deleteAll();
    }


    @Test
    public void testGetAllCarsEmpty() throws Exception {
        log.info("car: {}",testRepo.findAll());
        mockMvc.perform(get("/cars"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }



    @Test
    public void testGetCarByIdNotFound() throws Exception{
        int id = 1;
        mockMvc.perform(get("/car/{id}",id))
                .andExpect(status().is4xxClientError()).andDo(print());

    }



    @Test
    public void testGetCarByNameNotFound() throws Exception{
        String name = "car1";
        mockMvc.perform(get("/searchName/{name})", name))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }



    @Test
    public void testGetCarByModelNotFound() throws Exception{
        int model = 2020;
        mockMvc.perform(get("/searchModel/{model}",model))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }



    @Test
    public void testGetCarByBrandNotFound() throws Exception{
        String brand = "audi";

        mockMvc.perform(get("/searchBrand/{brand})", brand))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }



    @Test
    public void testGetCarByImageNotFound() throws Exception{
        int id = 1;
        mockMvc.perform(get("/searchImage/{id}",id))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void testAddCarEmpty() throws Exception {
        String carData = objMapper.writeValueAsString(new CarDto());
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());

        String s= null;
        mockMvc.perform(multipart("/cars")
                        .file(file)
                        .param("data", carData))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(s))
                .andDo(print());
    }

    @Test
    public void testDeleteCarNotFound() throws Exception{
        int id = 1;
        mockMvc.perform(delete("/cars/{id}",id))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    public void testUpdateCarNotFound() throws Exception{

        String carData = objMapper.writeValueAsString(new CarDto());
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());
        mockMvc.perform(multipart(HttpMethod.PUT,"/cars/{id}",1)
                        .file(file)
                        .param("data",carData))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }


}
