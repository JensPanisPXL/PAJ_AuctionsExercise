package be.pxl.auctions.dao.impl;

import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.BidBuilder;
import be.pxl.auctions.model.User;
import be.pxl.auctions.model.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class BidDaoImplTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BidDaoImpl bidDao;

    @Test
    public void bidCanBeSaved() {
        Bid bid = BidBuilder.aBid().build();
        Bid savedBid = bidDao.saveBid(bid);
        entityManager.flush();
        entityManager.clear();

        assertEquals(bid, savedBid);
    }
}
