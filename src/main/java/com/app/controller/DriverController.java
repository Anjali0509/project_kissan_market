package com.app.controller;

import com.app.dto.DriverDto;
import java.util.Base64;
import java.util.ArrayList;
import com.app.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Save Driver
    @PostMapping("/save")
    public ResponseEntity<Object> saveDriver(
            @RequestParam String driverName,
            @RequestParam String vehicleNo,
            @RequestParam String address,
            @RequestParam double ratePer100Km,
            @RequestParam int maxCapacity,
            @RequestParam int drivingExperience,
            @RequestParam String phoneNo,
            @RequestParam double rating,
            @RequestParam MultipartFile driverPhoto) {

        try {
            // Check for duplicate entries based on unique fields (e.g., vehicleNo or
            // phoneNo)
            String checkSql = "SELECT COUNT(*) FROM driver WHERE vehicleNo = ? OR phoneNo = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, new Object[] { vehicleNo, phoneNo });

            if (count > 0) {
                return ResponseEntity.ok(Map.of(
                        "error", true,
                        "message", "Driver with the same vehicle number or phone number already exists."));
            }

            // Insert the new driver record
            String sql = "INSERT INTO driver (driverName, vehicleNo, address, ratePer100Km, maxCapacity, drivingExperience, phoneNo, rating, driverPhoto) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int result = jdbcTemplate.update(sql, driverName, vehicleNo, address, ratePer100Km, maxCapacity,
                    drivingExperience, phoneNo, rating, driverPhoto.getBytes());

            if (result > 0) {
                return ResponseEntity.ok(Map.of(
                        "error", false,
                        "message", "Driver saved successfully!"));
            } else {
                return ResponseEntity.ok(Map.of(
                        "error", true,
                        "message", "Failed to save driver!"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.ok(Map.of(
                    "error", true,
                    "message", "An error occurred while saving the driver. Please try again!"));
        }
    }

    // Get Driver by ID
    @PostMapping("/getDriver")
    public ResponseEntity<Object> getDriver(@RequestBody Map<String, Integer> requestBody) {
        try {
            Integer driverId = requestBody.get("driverId");
            if (driverId == null) {
                return ResponseEntity.ok(Map.of(
                        "error", true,
                        "message", "Driver ID is required."));
            }

            String sql = "SELECT * FROM driver WHERE driverId = ?";
            Driver driver = jdbcTemplate.queryForObject(sql, driverRowMapper(), new Object[] { driverId });

            DriverDto dto = convertToDto(driver);

            return ResponseEntity.ok(Map.of(
                    "error", false,
                    "data", dto));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.ok(Map.of(
                    "error", true,
                    "message", "Driver not found or invalid input!"));
        }
    }

    // Fetch all drivers
    @PostMapping("/all")
    public ResponseEntity<Object> getAllDrivers() {
        try {
            String sql = "SELECT * FROM driver";
            List<Driver> drivers = jdbcTemplate.query(sql, driverRowMapper());

            List<DriverDto> driverDtos = new ArrayList<>();
            for (Driver d : drivers) {
                driverDtos.add(convertToDto(d));
            }

            return ResponseEntity.ok(Map.of("error", false, "data", driverDtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", true,
                    "message", "Something went wrong while fetching drivers"));
        }
    }

    private DriverDto convertToDto(Driver driver) {
        DriverDto dto = new DriverDto();
        dto.setDriverId(driver.getDriverId());
        dto.setDriverName(driver.getDriverName());
        dto.setVehicleNo(driver.getVehicleNo());
        dto.setAddress(driver.getAddress());
        dto.setRatePer100Km(driver.getRatePer100Km());
        dto.setMaxCapacity(driver.getMaxCapacity());
        dto.setDrivingExperience(driver.getDrivingExperience());
        dto.setPhoneNo(driver.getPhoneNo());
        dto.setRating(driver.getRating());

        if (driver.getDriverPhoto() != null) {
            String base64Image = Base64.getEncoder().encodeToString(driver.getDriverPhoto());
            dto.setDriverPhotoBase64(base64Image);
        }

        return dto;
    }

    // RowMapper for Driver
    private RowMapper<Driver> driverRowMapper() {
        return (rs, rowNum) -> {
            Driver driver = new Driver();
            driver.setDriverId(rs.getInt("driverId"));
            driver.setDriverName(rs.getString("driverName"));
            driver.setVehicleNo(rs.getString("vehicleNo"));
            driver.setAddress(rs.getString("address"));
            driver.setRatePer100Km(rs.getDouble("ratePer100Km"));
            driver.setMaxCapacity(rs.getInt("maxCapacity"));
            driver.setDrivingExperience(rs.getInt("drivingExperience"));
            driver.setPhoneNo(rs.getString("phoneNo"));
            driver.setRating(rs.getDouble("rating"));
            driver.setDriverPhoto(rs.getBytes("driverPhoto")); // Assuming photo is stored as a BLOB
            return driver;
        };
    }
}
