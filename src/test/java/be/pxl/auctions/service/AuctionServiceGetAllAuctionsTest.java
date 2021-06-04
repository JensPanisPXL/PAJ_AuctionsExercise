package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.AuctionBuilder;
import be.pxl.auctions.rest.resource.AuctionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceGetAllAuctionsTest {

    private static final long AUCTION_ID = 5L;

    @Mock
    private AuctionDao auctionDao;
    @InjectMocks
    private AuctionService auctionService;

    @Test
    public void returnsListOfAuctionDTOS() {
        Auction auction1 = AuctionBuilder.anAuction().withId(AUCTION_ID).build();
        Auction auction2 = AuctionBuilder.anAuction().withId(AUCTION_ID + 1).build();

        when(auctionDao.findAllAuctions()).thenReturn(Arrays.asList(auction1, auction2));
        List<AuctionDTO> actualList = auctionService.getAllAuctions();

        assertEquals(actualList.size(), 2);
        assertTrue(actualList.contains(auction1));
        assertTrue(actualList.contains(auction2));
    }
}
