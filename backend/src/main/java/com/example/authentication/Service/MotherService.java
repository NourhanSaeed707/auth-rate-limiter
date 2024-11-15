package com.example.authentication.Service;
import com.example.authentication.Repository.MotherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotherService {
    @Autowired
    private MotherRepository motherRepository;
}
