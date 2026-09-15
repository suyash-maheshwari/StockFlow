package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Address;
import com.Suyash.StockFlow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
