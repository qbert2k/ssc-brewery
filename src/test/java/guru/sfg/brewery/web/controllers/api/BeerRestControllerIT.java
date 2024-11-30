package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/12345678-1234-1234-1234-12345678901")
                        .header("Api-Key", "spring")
                        .header("Api-Secret", "guruXXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/12345678-1234-1234-1234-12345678901")
                        .header("Api-Key", "spring")
                        .header("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerUrlParameters() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("apiKey", "spring");
        parameters.add("apiSecret", "guru");
        mockMvc.perform(delete("/api/v1/beer/12345678-1234-1234-1234-12345678901")
                        .params(parameters))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerWithHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/12345678-1234-1234-1234-12345678901")
                        .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/12345678-1234-1234-1234-12345678901"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/12345678-1234-1234-1234-12345678901"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/12345678901"))
                .andExpect(status().isOk());
    }
}
