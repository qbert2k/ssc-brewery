package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@PreAuthorize("hasAuthority('order.read') OR hasAuthority('customer.order.read') AND @beerOrderAuthenticationManager.customerIdMatched(authentication, #customerId)")
public @interface BeerOrderReadPermission {
}
