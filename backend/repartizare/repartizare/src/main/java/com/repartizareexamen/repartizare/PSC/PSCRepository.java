package com.repartizareexamen.repartizare.PSC;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PSCRepository extends JpaRepository<PSC,PSCId> {
}
