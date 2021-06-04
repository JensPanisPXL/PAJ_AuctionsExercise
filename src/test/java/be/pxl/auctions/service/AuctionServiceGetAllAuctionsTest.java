package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.AuctionBuilder;
import be.pxl.auctions.rest.resource.AuctionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

public class AuctionServiceGetAllAuctionsTest {

    private static final long AUCTION_ID = 5L;

    @Mock
    private AuctionDao auctionDao;
    @InjectMocks
    private AuctionService auctionService;
    private Auction auction;
    private Auction auction2;

    @BeforeEach
    void init() {
        auction = AuctionBuilder.anAuction().withId(AUCTION_ID).build();
        auction2 = AuctionBuilder.anAuction().withId(AUCTION_ID + 1).build();
    }

    @Test
    public void returnsListOfAuctionDTOS() {
        //Deze test begrijp ik niet zo goed?

        List<Auction> expectedList = new ArrayList<>();
        expectedList.add(auction);
        expectedList.add(auction2);
        //List<AuctionDTO> expectedDTOList = expectedList.stream().map()

        //when(auctionDao.findAllAuctions()).thenReturn(expectedList);
        //List<AuctionDTO> actualList = auctionService.getAllAuctions();

        assertFalse(false);
    }
}
