package guru.sfg.brewery.web.controllers;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.services.BeerOrderService;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class IndexControllerIT extends BaseIT {

    @MockBean
    BeerService beerService;

    @MockBean
    BeerOrderService beerOrderService;

    @MockBean
    BreweryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    PersistentTokenRepository persistentTokenRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    GoogleAuthenticator googleAuthenticator;

    @Test
    @Disabled
    void testGetIndexSlash() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void testGetBeersFind() throws Exception {
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isUnauthorized());
    }
}
