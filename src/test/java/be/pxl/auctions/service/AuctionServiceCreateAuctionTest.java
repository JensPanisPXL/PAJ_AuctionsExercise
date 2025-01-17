package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.AuctionCreateResourceBuilder;
import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceCreateAuctionTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String DATE_IN_PAST = LocalDate.now().minusDays(1).format(FORMATTER);

    @Mock
    private AuctionDao auctionDao;
    @InjectMocks
    private AuctionService auctionService;
    private AuctionCreateResource auctionCreateResource;
    @Captor
    private ArgumentCaptor<Auction> auctionCapter;

    @BeforeEach
    void init() {
        auctionCreateResource = AuctionCreateResourceBuilder.anAuctionCreateResource().build();
    }

    @Test
    void throwsRequiredFieldExceptionWhenAuctionCreateResourceHasNoDescription() {
        auctionCreateResource.setDescription("");
        assertThrows(RequiredFieldException.class,
                () -> auctionService.createAuction(auctionCreateResource));
    }

    @Test
    void throwsRequiredFieldExceptionWhenAuctionCreateResourceHasNoDate() {
        auctionCreateResource.setEndDate(null);
        assertThrows(RequiredFieldException.class,
                () -> auctionService.createAuction(auctionCreateResource));
    }

    @Test
    void throwsInvalidDateExceptionWhenUserCreateResourceHasADateOfBirthInTheFuture() {
        auctionCreateResource.setEndDate(DATE_IN_PAST);

        assertThrows(InvalidDateException.class,
                () -> auctionService.createAuction(auctionCreateResource));
    }

    @Test
    void mapToAuctionDTOSavesAuctionCorrectly() {
        when(auctionDao.saveAuction(any())).thenAnswer(returnsFirstArg());

        AuctionDTO createdAuction = auctionService.createAuction(auctionCreateResource);
        assertNotNull(createdAuction);

        verify(auctionDao).saveAuction(auctionCapter.capture());
        Auction auctionSaved = auctionCapter.getValue();
        assertEquals(AuctionCreateResourceBuilder.DESCRIPTION, auctionSaved.getDescription());
        assertEquals(AuctionCreateResourceBuilder.ENDDATE, auctionSaved.getEndDate().format(FORMATTER));
    }
}