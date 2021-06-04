package be.pxl.auctions.dao.impl;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.AuctionBuilder;
import be.pxl.auctions.model.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
class AuctionDaoImplTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuctionDaoImpl auctionDao;

    @Test
    void auctionCanBeSavedAndRetrievedById() {
        Auction auction = AuctionBuilder.anAuction().build();
        long newAuctionId = auctionDao.saveAuction(auction).getId();
        entityManager.flush();
        entityManager.clear();

        Optional<Auction> retrievedAuction = auctionDao.findAuctionById(newAuctionId);
        assertTrue(retrievedAuction.isPresent());

        assertEquals(AuctionBuilder.END_DATE, retrievedAuction.get().getEndDate());
        assertEquals(AuctionBuilder.DESCRIPTION, retrievedAuction.get().getDescription());
    }

    @Test
    void findAllAuctions() {
        int currentAmountOfAuctions = auctionDao.findAllAuctions().size();

        Auction auction = AuctionBuilder.anAuction().build();
        long newAuctionId = auctionDao.saveAuction(auction).getId();
        entityManager.flush();
        entityManager.clear();

        List<Auction> auctionList = auctionDao.findAllAuctions();
        assertFalse(auctionList.isEmpty());
        assertEquals(currentAmountOfAuctions + 1, auctionList.size());

        Optional<Auction> retrievedAuction = auctionDao.findAuctionById(newAuctionId);
        assertTrue(retrievedAuction.isPresent());
    }
}