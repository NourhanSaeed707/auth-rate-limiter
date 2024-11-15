package com.example.authentication.Controller;
import com.example.authentication.Service.MotherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/mother")
public class MotherController {
    @Autowired
    private MotherService motherService;
}
