package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@PreAuthorize("hasAuthority('order.pickup') OR hasAuthority('customer.order.pickup') AND @beerOrderAuthenticationManager.customerIdMatched(authentication, #customerId)")
public @interface BeerOrderPickupPermission {
}
