package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.ChangePasswordDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * admin can add more user which can be employee
     * @param newUser
     * @return
     */
    @PostMapping()
    public ResponseEntity<?> addNewUser(@RequestBody RegisterDto newUser){
        String msg = userService.saveNewUser(newUser);
        if (msg == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        }
    }


    /**
     * upload avatar image
     * @param file
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file,
                                         @PathVariable("id") Long id) throws IOException {
        String uploadImage = userService.uploadImage(file,id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    /**
     * change personal info
     * @param userDto
     * @return
     */
    @PutMapping("/info")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){
        String userSave = userService.update(userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userSave);
    }

    /**
     * Get Avater Image from user
     * @param id
     * @return
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<?> findUserImageById(@PathVariable("id") Long id){
        byte[] imageData = userService.findImageById(id);
        MediaType mediaType = getMediaTypeForImageData(imageData);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(mediaType)
                .body(imageData);
    }

    /**Change password
     *
     * @param
     * @return
     */
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        String message = userService.changePassword(changePasswordDto);
        return ResponseEntity.ok(message);
    }

    private MediaType getMediaTypeForImageData(byte[] imageData) {
        // Detect the image format based on the image's magic bytes or other characteristics
        // Implement logic here to identify whether it's JPEG or PNG
        // For example, you can analyze the first few bytes of the image data
        // Default to JPEG if the format cannot be determined
        MediaType defaultMediaType = MediaType.IMAGE_JPEG;
        // Add logic to detect PNG format
        if (isPngImageData(imageData)) {
            return MediaType.IMAGE_PNG;
        }
        // Return the default media type (JPEG) if no other format is detected
        return defaultMediaType;
    }

    private boolean isPngImageData(byte[] imageData) {
        // Implement logic to detect PNG format based on the image data
        // For example, check for PNG-specific magic bytes or other characteristics
        // Return true if it's PNG, false otherwise
        if (imageData.length >= 8) {
            // Check for PNG magic bytes (89 50 4E 47 0D 0A 1A 0A)
            if (imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50
                    && imageData[2] == (byte) 0x4E && imageData[3] == (byte) 0x47
                    && imageData[4] == (byte) 0x0D && imageData[5] == (byte) 0x0A
                    && imageData[6] == (byte) 0x1A && imageData[7] == (byte) 0x0A) {
                return true; // It's a PNG image
            }
        }
        return false;
    }
}
