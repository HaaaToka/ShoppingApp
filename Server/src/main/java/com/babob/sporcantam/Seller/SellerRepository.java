package com.babob.sporcantam.Seller;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SellerRepository extends CrudRepository<Seller, Integer>{
    @Query(
            value = "SELECT * FROM seller u WHERE u.sessionid = sessionID",nativeQuery = true)
    Collection<Seller> findBySessionID(String sessionID);

    @Query(
            value = "SELECT * FROM seller u WHERE u.email = email",nativeQuery = true)
    Collection<Seller> findByEmail(String email);
}
