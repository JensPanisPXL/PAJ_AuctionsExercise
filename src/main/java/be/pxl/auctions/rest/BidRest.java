package be.pxl.auctions.rest;

import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.service.BidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/auctions")
public class BidRest {
    private static final Logger LOGGER = LogManager.getLogger(BidRest.class);

    @Autowired
    private BidService bidService;

    @PostMapping("/{auctionId}/bids")
    public BidDTO createBid(
            @RequestBody BidCreateResource bidCreateResource,
            @PathVariable("auctionId") long auctionId
            ) {
        return bidService.createBid(bidCreateResource, auctionId);
    };
}
