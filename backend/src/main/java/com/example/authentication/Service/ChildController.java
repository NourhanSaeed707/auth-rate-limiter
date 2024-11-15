package com.example.authentication.Service;

import com.example.authentication.Repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChildController {
    @Autowired
    private ChildRepository childRepository;
}
