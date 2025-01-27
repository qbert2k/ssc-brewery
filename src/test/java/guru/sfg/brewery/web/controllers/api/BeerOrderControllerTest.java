package guru.sfg.brewery.web.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.Customer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerOrderDto;
import guru.sfg.brewery.web.model.BeerOrderLineDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static guru.sfg.brewery.bootstrap.DefaultBreweryLoader.DUNEDIN_DISTRIBUTING;
import static guru.sfg.brewery.bootstrap.DefaultBreweryLoader.DUNEDIN_USER;
import static guru.sfg.brewery.bootstrap.DefaultBreweryLoader.KEYWEST_USER;
import static guru.sfg.brewery.bootstrap.DefaultBreweryLoader.KEY_WEST_DISTRIBUTING;
import static guru.sfg.brewery.bootstrap.DefaultBreweryLoader.STPETE_USER;
import static guru.sfg.brewery.bootstrap.DefaultBreweryLoader.ST_PETE_DISTRIBUTING;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerOrderControllerTest extends BaseIT {

    public static final String API_ROOT = "/api/v1/customers/";

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    Customer stPeteCustomer;
    Customer dunedinCustomer;
    Customer kyleCustomer;
    List<Beer> loadedBeers;

    @BeforeEach
    void setUp() {
        stPeteCustomer = customerRepository.findAllByCustomerName(ST_PETE_DISTRIBUTING).orElseThrow();
        dunedinCustomer = customerRepository.findAllByCustomerName(DUNEDIN_DISTRIBUTING).orElseThrow();
        kyleCustomer = customerRepository.findAllByCustomerName(KEY_WEST_DISTRIBUTING).orElseThrow();

        loadedBeers = beerRepository.findAll();
    }

    @Test
    void createOrderNoAuth() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isUnauthorized());
    }

    @WithUserDetails("spring")
    @Test
    void createOrderUserAdmin() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isCreated());
    }

    @WithUserDetails(STPETE_USER)
    @Test
    void createOrderUserAuthCustomer() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isCreated());
    }

    @WithUserDetails(KEYWEST_USER)
    @Test
    void createOrderUserNotAuthCustomer() throws Exception {
        BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

        mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void listOrdersNotAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                .andExpect(status().isUnauthorized());
    }

    @WithUserDetails("spring")
    @Test
    void listOrdersAdminAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                .andExpect(status().isOk());
    }

    @WithUserDetails(STPETE_USER)
    @Test
    void listOrdersCustomerAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                .andExpect(status().isOk());
    }

    @WithUserDetails(DUNEDIN_USER)
    @Test
    void listOrdersCustomerNotAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listOrdersNoAuth() throws Exception {
        mockMvc.perform(get(API_ROOT + stPeteCustomer.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Disabled
    @Test
    void pickUpOrderNotAuth() {
    }

    @Disabled
    @Test
    void pickUpOrderNotAdminUser() {
    }

    @Disabled
    @Test
    void pickUpOrderCustomerUserAuth() {
    }

    @Disabled
    @Test
    void pickUpOrderCustomerUserNotAuth() {
    }

    private BeerOrderDto buildOrderDto(Customer customer, UUID beerId) {
        List<BeerOrderLineDto> orderLines = Arrays.asList(BeerOrderLineDto.builder()
                .id(UUID.randomUUID())
                .beerId(beerId)
                .orderQuantity(5)
                .build());

        return BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef("123")
                .orderStatusCallbackUrl("http://example.com")
                .beerOrderLines(orderLines)
                .build();
    }
}
