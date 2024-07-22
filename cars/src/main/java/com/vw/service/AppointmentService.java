package com.vw.service;

import com.vw.dto.CustomerDto;
import com.vw.repo.AppointmentRepo;
import com.vw.repo.CarRepo;
import com.vw.repo.CustomerRepo;
import com.vw.repo.ExecutiveRepo;
import com.vw.dto.AppointmentDto;
import com.vw.entities.Appointment;
import com.vw.entities.Car;
import com.vw.entities.Customer;
import com.vw.entities.Executive;
import com.vw.exceptions.AppointmentException;
import com.vw.exceptions.CustomerException;
import com.vw.exceptions.ExecutiveException;
import com.vw.exceptions.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepository;

    @Autowired
    private CarRepo carRepository;

    @Autowired
    private ExecutiveRepo executiveRepository;

    @Autowired
    private CustomerRepo customerRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    CustomerService customerService;

    public AppointmentService(AppointmentRepo appointmentRepository, CarRepo carRepository, ExecutiveRepo executiveRepository, CustomerRepo customerRepository) {
        this.appointmentRepository = appointmentRepository;
        this.carRepository = carRepository;
        this.executiveRepository = executiveRepository;
        this.customerRepository = customerRepository;
    }

    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setAppointmentId(appointment.getAppointmentId());
        appointmentDto.setCarId(appointment.getCar().getCarId());
        appointmentDto.setExecutiveId(appointment.getExecutive().getExecutiveId());
        appointmentDto.setCustomerId(appointment.getCustomer().getCustomerId());
        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDto.setAppointmentType(appointment.getAppointmentType());
        appointmentDto.setApproved(appointment.isApproved());
        return appointmentDto;
    }

    private Appointment convertToEntity(AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentDto.getAppointmentId());
        appointment.setCar(carRepository.findById(appointmentDto.getCarId())
                .orElseThrow(() -> new IdNotFoundException("Car not found with id: " + appointmentDto.getCarId())));

        appointment.setExecutive(executiveRepository.findById(appointmentDto.getExecutiveId())
                .orElseThrow(() -> new ExecutiveException("Executive not found with id: " + appointmentDto.getExecutiveId())));

        appointment.setCustomer(customerRepository.findById(appointmentDto.getCustomerId())
                .orElseThrow(() -> new CustomerException("Customer not found with id: " + appointmentDto.getCustomerId())));

        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setAppointmentType(appointmentDto.getAppointmentType());
        appointment.setApproved(false);
        return appointment;
    }

    private void validateAppointment(Appointment appointment) {
        // Validate car
        Optional<Car> carOptional = carRepository.findById(appointment.getCar().getCarId());
        if (carOptional.isEmpty()) {
            throw new IdNotFoundException("Invalid car id: " + appointment.getCar().getCarId());
        }

        // Validate executive
        Optional<Executive> executiveOptional = executiveRepository.findById(appointment.getExecutive().getExecutiveId());
        if (executiveOptional.isEmpty()) {
            throw new ExecutiveException("Invalid executive id: " + appointment.getExecutive().getExecutiveId());
        }

        // Validate customer
        Optional<Customer> customerOptional = customerRepository.findById(appointment.getCustomer().getCustomerId());
        if (customerOptional.isEmpty()) {
            throw new CustomerException("Invalid customer id: " + appointment.getCustomer().getCustomerId());
        }

        // Ensure the car is available for the appointment date
        boolean isCarAvailable = appointmentRepository.findByCarAndAppointmentDate(appointment.getCar(), appointment.getAppointmentDate())
                .isEmpty();
        if (!isCarAvailable) {
            throw new AppointmentException("The car is not available for the selected date.");
        }

    }


//    private void sendTestDriveConfirmationEmail(Appointment appointment) {
//        String carInfo = "Car Brand: " + appointment.getCar().getBrand() +
//                "\nCar Name: "+ appointment.getCar().getName()+
//                "\nCar Model: " + appointment.getCar().getModel() +
//                "\nAppointment Date: " + appointment.getAppointmentDate();
//
//        emailService.sendEmail(appointment.getCustomer().getEmail(),
//                "Test Drive Confirmation",
//                "An executive will contact you for further details.\n\n" + carInfo);
//    }

    private void sendApprovalRequestToExecutive(Appointment appointment) {
        // Fetch executive's email
        String executiveEmail = appointment.getExecutive().getEmail();

        // Prepare email content
        String subject = "Approval Required: New Appointment";
        String message = String.format("Dear %s, \n\nAn appointment has been created and is pending your approval" +
                        ".\n\nAppointment Details:\n- Customer: %s\n- Car: %s\n- Date: %s\n\nPlease log in to the " +
                        "dashboard to approve or reject the request.\n\nBest Regards,\nVW Group",
                appointment.getExecutive().getName(),
                appointment.getCustomer().getName(),
                appointment.getCar().getName(),
                appointment.getAppointmentDate().toString());

        // Send email (assuming you have an emailService)
        emailService.sendEmail(executiveEmail, subject, message);
    }

    private void notifyExecutiveForApproval(Appointment appointment) {
        String message = String.format("Appointment ID %d has been updated and requires your approval.", appointment.getAppointmentId());
        sendEmail(appointment.getExecutive().getEmail(), "Appointment Update Approval Required", message);
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (MailException e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    private void sendCarPurchaseConfirmationEmail(Customer customer, Car car) {
        String subject = "Congratulations on your new car purchase!";
        String body = String.format("Dear %s,\n\nCongratulations on purchasing your new car!\n\nCar Details:\nModel: " +
                        "%s\nPrice: $%.2f\n\nThank you for your business.\n\nBest regards,\nVW Group",
                customer.getName(), car.getModel(), car.getPrice());
        emailService.sendEmail(customer.getEmail(), subject, body);
    }

    private void sendConfirmationEmailForCustomer(Appointment appointment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(appointment.getCustomer().getEmail());
            message.setSubject("Appointment Confirmation");
            message.setText("Dear " + appointment.getCustomer().getName() + ",\n\n"
                    + "Your appointment for " + appointment.getCar().getName() + " " + appointment.getCar().getModel()
                    + " has been confirmed.\n\n"
                    + "Appointment Details:\n"
                    + "Date: " + appointment.getAppointmentDate() + "\n"
                    + "Type: " + appointment.getAppointmentType() + "\n\n"
                    + "Thank you for choosing us!" +"\n"
                    + "VW Group"+"\n");

            // Send email
            javaMailSender.send(message);
        } catch (MailException e) {
            // Handle exception
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private void sendPendingAppointmentEmailToCustomer(Appointment appointment) {
        String customerEmail = appointment.getCustomer().getEmail();
        String subject = "Your Appointment is Pending";
        String message = "Dear " + appointment.getCustomer().getName() + ",\n\n" +
                "Your appointment scheduled on " + appointment.getAppointmentDate() + " is currently pending approval.\n" +
                "We will notify you once it has been approved.\n\n" +
                "Thank you,\n" +
                "VW Group";

        emailService.sendEmail(customerEmail, subject, message);
    }


    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentById(int id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentException("Appointment not found with id: " + id));
        return convertToDto(appointment);
    }

    public List<AppointmentDto> getAppointmentsByExecutive(int executiveId) {
        Executive executive = executiveRepository.findById(executiveId)
                .orElseThrow(() -> new ExecutiveException("Executive not found with id: " + executiveId));
        List<Appointment> appointments = appointmentRepository.findByExecutive(executive);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

//    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
//        Appointment appointment = convertToEntity(appointmentDto);
//        validateAppointment(appointment);
//        appointment.setApproved(false);
//        Appointment savedAppointment = appointmentRepository.save(appointment);
//        sendPendingAppointmentEmailToCustomer(savedAppointment);
//        sendApprovalRequestToExecutive(savedAppointment);
//        return convertToDto(savedAppointment);
//    }


    public AppointmentDto createTestDriveAppointment(AppointmentDto appointmentDto) {
        Customer customer = customerRepository.findById(appointmentDto.getCustomerId())
                .orElseThrow(() -> new CustomerException("Customer details are not found!"));
        CustomerDto customerDto = customerService.convertCustomerToDto(customer);

        appointmentDto.setCustomer(customerDto);
        Appointment appointment = convertToEntity(appointmentDto);
        validateAppointment(appointment);
        appointment.setApproved(false);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        sendPendingAppointmentEmailToCustomer(savedAppointment);
        sendApprovalRequestToExecutive(savedAppointment);
        return convertToDto(savedAppointment);
    }

    public AppointmentDto updateAppointment(int id, AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentException("Appointment not found with id: " + id));
        Car car = carRepository.findById(appointmentDto.getCarId())
                .orElseThrow(() -> new IdNotFoundException("Car not found with id: " + appointmentDto.getCarId()));
        appointment.setCar(car);
        Executive executive = executiveRepository.findById(appointmentDto.getExecutiveId())
                .orElseThrow(() -> new ExecutiveException("Executive not found with id: " + appointmentDto.getExecutiveId()));
        appointment.setExecutive(executive);
        Customer customer = customerRepository.findById(appointmentDto.getCustomerId())
                .orElseThrow(() -> new CustomerException("Customer not found with id: " + appointmentDto.getCustomerId()));
        appointment.setCustomer(customer);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setAppointmentType(appointmentDto.getAppointmentType());
        Appointment updatedAppointment;

        if (appointmentDto.isApproved() && appointmentDto.getAppointmentType().equals("test-drive")) {
            appointment.setApproved(true);
            updatedAppointment = appointmentRepository.save(appointment);
            sendConfirmationEmailForCustomer(updatedAppointment);
            return convertToDto(updatedAppointment);
        }
        appointment.setApproved(false);
        updatedAppointment = appointmentRepository.save(appointment);

        // Notify executive about the update and pending approval.
        notifyExecutiveForApproval(updatedAppointment);
        // Notify Customer about the pending approval.
        sendPendingAppointmentEmailToCustomer(updatedAppointment);

       return convertToDto(updatedAppointment);
    }

    public void deleteAppointment(int id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        } else {
            throw new AppointmentException("Appointment not Found by Id: " + id);
        }

    }

    public void buyACar(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException("Appointment not found with id: " + appointmentId));
        if(!appointment.isApproved()){
            throw new AppointmentException("Appointment status is not approved by the executive. Cannot buy car.");
        }
        Car car = appointment.getCar();
        Customer customer = appointment.getCustomer();

        // Send confirmation email
        sendCarPurchaseConfirmationEmail(customer, car);
    }

    public AppointmentDto bookAppointmentWithDirectExecutive(AppointmentDto appointmentDto) {
        // Validate and set the executive
        Executive executive = executiveRepository.findById(appointmentDto.getExecutiveId())
                .orElseThrow(() -> new ExecutiveException("Executive not found with id: " + appointmentDto.getExecutiveId()));

        Appointment appointment = convertToEntity(appointmentDto);
        appointment.setExecutive(executive);

        validateAppointment(appointment);

        // Save the appointment as approval
        appointment.setApproved(true);


        // Save the appointment in the repository
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send confirmation email to customer
        sendConfirmationEmailForCustomer(savedAppointment);

        return convertToDto(savedAppointment);
    }

}

