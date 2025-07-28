package com.repartizareexamen.repartizare.Curs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursRepository extends JpaRepository<Curs,Integer> {
}
