package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model")
public class TransactionTest {

    @Test
    public void testCreate() {
        Integer expected_id = 1;
        Integer expected_user = 2;
        CartProduct[] expected_products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        String expected_dateTime = "3/27";
        String expected_paymentMethod = "visa";
        String expected_shippingAddress = "address";

        Transaction transaction = new Transaction(expected_id, expected_user, expected_products,
                expected_dateTime, expected_paymentMethod, expected_shippingAddress);
        assert transaction.getId() == expected_id;
        assert transaction.getUser().equals(expected_user);
        assert transaction.getProducts().equals(expected_products);
        assert transaction.getDateTime().equals(expected_dateTime);
        assert transaction.getPaymentMethod().equals(expected_paymentMethod);
        
        Transaction transaction2 = new Transaction(expected_user, expected_products, expected_paymentMethod);
        assert transaction2.getUser().equals(expected_user);
        assert transaction2.getProducts().equals(expected_products);
        assert transaction2.getPaymentMethod().equals(expected_paymentMethod);
    }

    @Test
    public void testToString() {
        Integer id = 1;
        Integer user = 2;
        CartProduct[] products = {new CartProduct(1, 1), new CartProduct(4, 4)};
        String dateTime = "3/27";
        String paymentMethod = "visa";
        String shippingAddress = "address";

        Transaction transaction = new Transaction(id, user, products, dateTime, paymentMethod, shippingAddress);
        String productList = "";

        for (CartProduct product : products) {
            productList = productList + product.toString() + ", ";
        }

        String expected_string = String.format(Transaction.STRING_FORMAT, id, user, dateTime, paymentMethod, shippingAddress, productList);
        assert transaction.toString().equals(expected_string);
    }

}
