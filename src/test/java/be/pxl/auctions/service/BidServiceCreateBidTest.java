package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.BidDao;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.*;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.util.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidServiceCreateBidTest {
    private static final long AUCTION_ID = 5L;
    private static final String USER_EMAIL = "sophie@pxl.be";


    @Mock
    private BidDao bidDao;
    @Mock
    private UserDao userDao;
    @Mock
    private AuctionDao auctionDao;
    @InjectMocks
    private BidService bidService;
    private BidCreateResource bidCreateResource;
    @Captor
    private ArgumentCaptor<Bid> bidCaptor;

    @BeforeEach
    void init() {
        bidCreateResource = BidCreateResourceBuilder.aBidCreateResource().build();
    }

    @Test
    void throwsRequiredFieldExceptionWhenBidHasNoEmail() {
        bidCreateResource.setEmail("");
        assertThrows(RequiredFieldException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidEmailExceptionWhenBidHasInvalidEmail() {
        bidCreateResource.setEmail("jenspanis.com");
        assertThrows(InvalidEmailException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsRequiredFieldExceptionWhenBidHasNoPrice() {
        bidCreateResource.setPrice(null);
        assertThrows(RequiredFieldException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsUserNotFoundExceptionWhenThereIsNoUserWithEmail() {
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsAuctionNotFoundExceptionWhenThereIsNoAuctionWithAuctionId() {
        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidBidExceptionWhenBidIsLowerThanCurrentPrice() {
        bidCreateResource.setPrice(50.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().build();
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));

        Bid bid = BidBuilder.aBid().withAmount(100.0).build();
        auction.getBids().add(bid);

        assertThrows(InvalidBidException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidBidExceptionWhenAuctionIsClosed() {
        bidCreateResource.setPrice(150.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().minusDays(5)).build();
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));

        Bid bid = BidBuilder.aBid().withAmount(100.0).build();
        auction.getBids().add(bid);

        assertThrows(InvalidDateException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidBidExceptionWhenBidderHasHighestBid() {
        bidCreateResource.setPrice(150.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().plusDays(1)).build();
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));

        Bid bid = BidBuilder.aBid().withUser(userDao.findUserByEmail(BidCreateResourceBuilder.EMAIL).get()).withAmount(100.0).build();
        auction.getBids().add(bid);

        assertThrows(InvalidBidException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void mapToBidDtoSavesUserCorrectly() {
        bidCreateResource.setPrice(150.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().plusDays(1)).build();
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));

        Bid bid = BidBuilder.aBid().withAmount(100.0).build();
        auction.getBids().add(bid);

        when(bidDao.saveBid(any())).thenAnswer(returnsFirstArg());

        BidDTO createdBid = bidService.createBid(bidCreateResource, AUCTION_ID);
        assertNotNull(createdBid);

        verify(bidDao).saveBid(bidCaptor.capture());
        Bid savedBid = bidCaptor.getValue();
        assertEquals("mark@facebook.com", savedBid.getUser().getEmail());
        assertEquals(150, savedBid.getAmount());
    }
}
