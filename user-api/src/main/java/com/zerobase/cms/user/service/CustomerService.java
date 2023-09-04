package com.zerobase.cms.user.service;

import com.zerobase.cms.user.repository.CustomerRepository;
import com.zerobase.cms.user.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> findByIdAndEmail(Long id, String email){
        return customerRepository.findById(id)
                .stream().filter(customer -> customer.getEmail().equals(email)).findFirst();

    }

    public Optional<Customer> findValidCustomer(String email, String password) {
        return customerRepository.findByEmail(email).stream().filter(
                customer -> customer.isVerify() && customer.getPassword().equals(password)
        ).findFirst();

    }
}
