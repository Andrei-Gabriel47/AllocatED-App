package com.repartizareexamen.repartizare.Repartizare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepartizareRepository extends JpaRepository<Repartizare, Integer> {
}
