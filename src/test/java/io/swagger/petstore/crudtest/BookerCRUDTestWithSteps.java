package io.swagger.petstore.crudtest;

import io.restassured.response.ValidatableResponse;
import io.swagger.petstore.steps.BookerSteps;
import io.swagger.petstore.testbase.TestBase;
import io.swagger.petstore.utils.TestUtils;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

public class BookerCRUDTestWithSteps extends TestBase {
    static String username = "admin";
    static String password = "password123";
    static String firstname = "Jim99" + TestUtils.getRandomValue();
    static String lastname = "Brown" + TestUtils.getRandomValue();
    static int totalprice = Integer.parseInt(1 + TestUtils.getRandomValue());
    static Boolean depositpaid = true;
    static String additionalneeds = "Breakfast";
    static String checkin = "2024-01-06";
    static String checkout = "2024-01-20";
    static String token;
    static int bookingid;


    @Steps
    BookerSteps bookerSteps;

    @Title("Creating token.")
    @Test
    public void test001() {
        ValidatableResponse response = bookerSteps.getToken(username, password);
        token = response.extract().path("token");
        System.out.println(token);
    }
    //CREATE USER

    @Title("Creating booking and verifying booking created.")
    @Test
    public void test002() {
        ValidatableResponse response = bookerSteps.createBooking(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
        response.log().all().statusCode(200);
        bookingid = response.extract().path("bookingid");
        HashMap<String, Object> value = response.extract().path("");
        Assert.assertThat(value, hasValue(bookingid));
    }
    @Title("Getting All IDs.")
    @Test
    public void test003() {
        ValidatableResponse response = bookerSteps.getAllId();
        response.log().all().statusCode(200);
        List<String> booking = response.extract().path("bookingid");
        Assert.assertTrue(booking.contains(bookingid));
    }

    @Title("Getting Single ID.")
    @Test
    public void test004() {
        ValidatableResponse response = bookerSteps.getSingleId(bookingid);
        response.log().all().statusCode(200);
        HashMap<String, Object> value = response.extract().path("");
        Assert.assertThat(value, hasValue(firstname));
    }



    @Title("Updating Single ID.")
    @Test
    public void test005() {
        firstname = firstname + "-updated";
        bookerSteps.updateBookingWithID(token, bookingid, firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
        ValidatableResponse response = bookerSteps.getUserbyId(token, bookingid);
        response.log().all().statusCode(200);
        HashMap<String, Object> value = response.extract().path("");
        Assert.assertThat(value, hasValue(firstname));
    }



    @Title("Deleting the ID and verifying deletion.")
    @Test
    public void test006() {
        bookerSteps.deleteABookingID( token,bookingid).statusCode(201);
        bookerSteps.getUserbyId(token, bookingid).statusCode(404);
    }
}
