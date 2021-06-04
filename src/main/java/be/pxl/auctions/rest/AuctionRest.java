package be.pxl.auctions.rest;

import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.service.AuctionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auctions")
public class AuctionRest {
    private static final Logger LOGGER = LogManager.getLogger(AuctionRest.class);

    @Autowired
    private AuctionService auctionService;

    @GetMapping
    public List<AuctionDTO> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping("{auctionId}")
    public AuctionDTO getAuctionById(@PathVariable("auctionId") long auctionId) {
        return auctionService.getAuctionById(auctionId);
    }

    @PostMapping
    public AuctionDTO createAuction(@RequestBody AuctionCreateResource auctionCreateResource) {
        return auctionService.createAuction(auctionCreateResource);
    }
}
