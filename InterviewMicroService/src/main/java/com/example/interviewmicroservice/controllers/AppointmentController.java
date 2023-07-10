package com.example.interviewmicroservice.controllers;

import com.example.interviewmicroservice.DTOs.AddPlanningRequest;
import com.example.interviewmicroservice.DTOs.AppointmentRequest;
import com.example.interviewmicroservice.DTOs.PaymentRequest;
import com.example.interviewmicroservice.models.*;
import com.example.interviewmicroservice.repositories.*;
import com.example.interviewmicroservice.responses.ApiResponse;
import com.example.interviewmicroservice.services.AppointmentService;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@AllArgsConstructor
@RestController
@RequestMapping("/interviews")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DayRepository dayRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final MoreInfoRepository moreInfoRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPlanning(@PathVariable("id") String username){
        return appointmentService.getPlanning(username);
    }
    @PostMapping("/add-planning")
    public ResponseEntity<Object> addPlanning(
            @RequestBody AddPlanningRequest request){
        return appointmentService.addPlanning(request);
    }

    @PostMapping("/add-appointment")
    public ResponseEntity<Object> addAppointment(
            @RequestBody AppointmentRequest request){
        return appointmentService.addAppointment(request);
    }

    @GetMapping("/days")
    public List<Day> getDays(){
        return dayRepository.findAll();
    }

    @GetMapping("/slots")
    public List<TimeSlot> getSlots(){
        return timeSlotRepository.findAll();
    }

    @GetMapping("/appointments/client/{email}")
    public Collection<Appointment> getAppointmentsByClient(@PathVariable("email") String email){
        try {
            Client client = clientRepository.findClientByEmail(email).orElseGet(() -> null);
            Collection<Appointment> appointments = client.getAppointments();
            return appointments;
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("/appointments/provider/{email}")
    public Collection<Appointment> getAppointmentsByProvider(@PathVariable("email") String email){
       try {
              Provider provider = providerRepository.findByEmail(email).orElseGet(() -> null);
              Collection<Appointment> appointments = provider.getAppointments();
              return appointments;
         } catch (Exception e){
              return null;
       }
    }
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable("id") Integer id){
        return appointmentService.deleteAppointment(id);
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<Object> getAppointment(@PathVariable("id") Integer id){
        try {
            Appointment appointment = appointmentRepository.findById(id).get();
            return new ResponseEntity<Object>(
                    appointment,
                    new HttpHeaders(), HttpStatus.OK
            );
        } catch (Exception e){
            return new ResponseEntity<Object>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "Appointment not found"),
                    new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }
        @PostMapping(value = "/payment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Object> payment(@RequestParam("file") MultipartFile file,
                                              @RequestParam("email") String email,
                                              @RequestParam("username") String username,
                                              @RequestParam("appointmentId") String appointmentId){
        Appointment appointment = appointmentRepository.findById(Integer.parseInt(appointmentId)).orElseGet(() -> null);
            if (appointment != null){
            appointment.setIsPayed(true);
            Appointment savedAppointment = appointmentRepository.save(appointment);
                try {
                    byte[] imageData = file.getBytes();
                    Payment payment = Payment.builder()
                            .paymentId(null)
                            .appointmentId(savedAppointment.getAppointmentId())
                            .email(email)
                            .username(username)
                            .file(imageData)
                            .build();
                    paymentRepository.save(payment);
                    return new ResponseEntity<Object>(
                            new ApiResponse(HttpStatus.OK, "Appointment payed"),
                            new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e){
                    return new ResponseEntity<Object>(
                            new ApiResponse(HttpStatus.BAD_REQUEST, "Something went wrong"),
                            new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }

        }
        return new ResponseEntity<Object>(
                new ApiResponse(HttpStatus.NOT_FOUND, "Appointment not found"),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
    @GetMapping("/download/{id}")
    @Transactional
    public ResponseEntity<Resource> downloadImage(@PathVariable Integer id) {
        // Retrieve the image entity from the database
        Payment payment = paymentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        // Create a ByteArrayResource from the image data
        ByteArrayResource resource = new ByteArrayResource(payment.getFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PostMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addMoreInfo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("domain") String domain,
            @RequestParam("description") String description,
            @RequestParam("appointmentId") String appointmentId) {

            try{
                Appointment appointment = appointmentRepository.findById(Integer.parseInt(appointmentId)).orElseGet(() -> null);
                if(appointment != null) {
                    byte[] data = file.getBytes();
                    MoreInfo moreInfo = MoreInfo.builder()
                            .moreInfoId(null)
                            .appointmentId(appointment.getAppointmentId())
                            .domain(domain)
                            .description(description)
                            .file(data)
                            .build();
                    moreInfoRepository.save(moreInfo);
                    return new ResponseEntity<Object>(
                            new ApiResponse(HttpStatus.OK, "More info added"),
                            new HttpHeaders(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Object>(
                            new ApiResponse(HttpStatus.NOT_FOUND, "Appointment not found"),
                            new HttpHeaders(), HttpStatus.NOT_FOUND);
                }

            } catch (Exception e){
                return new ResponseEntity<Object>(
                        new ApiResponse(HttpStatus.NOT_FOUND, "Appointment not found"),
                        new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
    }


    @GetMapping("/info/{id}")
    @Transactional
    public ResponseEntity<Resource> downloadInfo(@PathVariable Integer id) {
        try {
            // Retrieve the image entity from the database
            MoreInfo moreInfo = moreInfoRepository.findByAppointmentId(id)
                    .orElseThrow(() -> new NotFoundException("More info not found"));

            // Create a ByteArrayResource from the image data
            ByteArrayResource resource = new ByteArrayResource(moreInfo.getFile());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg")
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Exception e){
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

        }
    }



    @GetMapping("/get-token")
    public String getToken() {


        String VIDEOSDK_API_KEY = "10118176-26c7-4378-a3bc-7f85bad0ee00";
        String VIDEOSDK_SECRET_KEY = "2ca68eb067fbbec51114cb4f0170539c62ebf31123569ebfbac6633f6ba463f8";

        Map<String, Object> payload = new HashMap<>();
        payload.put("apikey", VIDEOSDK_API_KEY);
        payload.put("permissions", new String[]{"allow_join", "allow_mod"});

        String token = Jwts.builder().setClaims(payload)
                .setExpiration(new Date(System.currentTimeMillis() + 7200 * 1000))
                .signWith(SignatureAlgorithm.HS256,VIDEOSDK_SECRET_KEY.getBytes()).compact();
        return token;
    }
}
