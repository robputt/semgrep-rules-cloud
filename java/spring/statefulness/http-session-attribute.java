package com.example.orders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

  private final CartStore cartStore;

  public CartController(CartStore cartStore) {
    this.cartStore = cartStore;
  }

  @PostMapping("/cart/session")
  public void addToSession(HttpSession session, String sku) {
    // ruleid: http-session-attribute
    session.setAttribute("cart", sku);
  }

  @PostMapping("/cart/request")
  public void addViaRequest(HttpServletRequest request, String sku) {
    // ruleid: http-session-attribute
    request.getSession(true).setAttribute("cart", sku);
  }

  @PostMapping("/cart/shared")
  public void addToSharedStore(String cartId, String sku) {
    // ok: http-session-attribute
    cartStore.append(cartId, sku);
  }

  interface CartStore {
    void append(String cartId, String sku);
  }
}
