package be.pxl.auctions.dao.impl;

import be.pxl.auctions.dao.BidDao;
import be.pxl.auctions.model.Bid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class BidDaoImpl implements BidDao {
    private static final Logger LOGGER = LogManager.getLogger(BidDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Bid saveBid(Bid bid) {
        LOGGER.info("Saving bid [" + bid.getId() + "]");
        entityManager.persist(bid);
        return bid;
    }
}
