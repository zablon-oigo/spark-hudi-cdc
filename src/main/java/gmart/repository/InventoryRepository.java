package gmart.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gmart.domain.entity.Inventory;
import gmart.domain.entity.Status;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByName(String name);
    List<Inventory> findByStatus(Status status);
    
}