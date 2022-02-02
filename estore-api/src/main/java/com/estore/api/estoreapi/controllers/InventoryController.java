package com.estore.api.estoreapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    public InventoryController() {
    }

    @PostMapping(value = "")
    public ResponseEntity createProduct(@RequestBody ResponseEntity product) {
        // TODO: process POST request

        return product;
    }

}
