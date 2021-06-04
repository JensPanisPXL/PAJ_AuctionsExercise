package be.pxl.auctions.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    private Auction auction;
    private static final LocalDate YESTERDAY = LocalDate.now().minusDays(1);
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);

    @BeforeEach
    void init() {
        auction = AuctionBuilder.anAuction().build();
    }

    @Test
    void isFinishedReturnsTrueWhenAuctionEndDateWasYesterday() {
        auction.setEndDate(YESTERDAY);
        assertTrue(auction.isFinished());
    }

    @Test
    void isFinishedReturnsFalseWhenAuctionEndDateIsToday() {
        auction.setEndDate(TODAY);
        assertFalse(auction.isFinished());
    }

    @Test
    void isFinishedReturnsFalseWhenAuctionEndDateIsTomorrow() {
        auction.setEndDate(TOMORROW);
        assertFalse(auction.isFinished());
    }

    @Test
    void findHighestBidShouldReturnNullWhenNoBids() {
        Bid highestBid = auction.findHighestBid();
        assertNull(highestBid);
    }

    @Test
    void findHighestBidShouldReturnTheHighestBid() {
        BidBuilder.aBid().withAuction(auction).build();
        BidBuilder.aBid().withAuction(auction).withAmount(80.0).build();
        BidBuilder.aBid().withAuction(auction).withAmount(35.0).build();
        BidBuilder.aBid().withAuction(auction).withAmount(15.0).build();

        Bid highestBid = auction.findHighestBid();
        assertNotNull(highestBid);
        assertEquals(80.0, highestBid.getAmount());
        //assertEquals(BidBuilder.DATE, highestBid.getDate());
        //assertEquals(BidBuilder.USER, highestBid.getUser());
        assertEquals(auction, highestBid.getAuction());
    }
}