package io.swagger.petstore.steps;

import io.restassured.response.ValidatableResponse;
import io.swagger.petstore.constants.EndPoints;
import io.swagger.petstore.model.AuthorisationPojo;
import io.swagger.petstore.model.BookingPojo;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class BookerSteps {

    @Step("Getting Token")
    public ValidatableResponse getToken(String username, String password) {
        AuthorisationPojo authPojo = new AuthorisationPojo();
        authPojo.setUsername(username);
        authPojo.setPassword(password);

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .when()
                .body(authPojo)
                .post(EndPoints.GET_AUTH)
                .then();

    }

    @Step("Creating new booking with firstName : {0}, lastName: {1}, email: {2}, totalprice: {3} depositpaid: {4}, bookingdates: {5} and additonalneeds: {6}")
    public ValidatableResponse createBooking(String firstname, String lastname, int totalprice, Boolean depositpaid, String checkin, String checkout, String additionalneeds) {
        BookingPojo.BookingDates dates = new BookingPojo.BookingDates();
        dates.setCheckin(checkin);
        dates.setCheckout(checkout);
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(dates);
        bookingPojo.setAdditionalneeds(additionalneeds);
        return SerenityRest.given()
                .header("Content-Type", "application/json")
                .body(bookingPojo)
                .when()
                .post(EndPoints.GET_BOOKING)
                .then().log().all();

    }

    @Step("Get all booking ids")
    public ValidatableResponse getAllId() {
        return SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_BOOKING)
                .then().statusCode(200);
    }

    @Step("Get Single Booking ID using id {0}")
    public ValidatableResponse getSingleId(int userId) {
        return SerenityRest.given().log().all()
                .pathParam("bookingId", userId)
                .when()
                .get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then().statusCode(200).log().all();
    }

    @Step("Get Single Booking by name using id {0}")
    public ValidatableResponse getSingleName(int userId) {
        return SerenityRest.given().log().all()
                .pathParam("user_id", userId)
                .when()
                .get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("findAll{it.bookingid == '" + userId + "'}.get(0)");
    }

    @Step("Updating record with id:{0}, token{1}, firstName: {2}, lastName: {3}, email: {4}, totalprice: {5} depositpaid: {6}, bookingdates: {7} and additonalneeds: {8} ")
    public ValidatableResponse updateBookingWithID(String token, int bookingid, String firstname, String lastname, int totalprice, boolean depositpaid, String checkin, String checkout, String additionalneeds) {
        BookingPojo.BookingDates dates = new BookingPojo.BookingDates();
        dates.setCheckin(checkin);
        dates.setCheckout(checkout);
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(dates);
        bookingPojo.setAdditionalneeds(additionalneeds);
        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .pathParam("bookingid", bookingid)
                .body(bookingPojo)
                .when()
                .put(EndPoints.UPDATE_BY_ID)
                .then().log().all().statusCode(200);
    }

    @Step("Getting student information with bookingId: {0}")
    public ValidatableResponse getUserbyId(String token, int bookingid) {
        return SerenityRest.given().log().all()
                .header("Cookie", "token=" + token)
                .pathParam("bookingId", bookingid)
                .when()
                .get(EndPoints.GET_SINGLE_BOOKING_BY_ID)
                .then();
    }

    @Step("Deleting existing booking with id: {0} and token: {1}")
    public ValidatableResponse deleteABookingID(String token, int bookingid) {
        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .pathParam("bookingId", bookingid)
                .when().delete(EndPoints.DELETE_BY_ID)
                .then().log().all();

    }

    @Step("Getting booking info by ID")
    public ValidatableResponse getBookingByID(String token, int bookingid) {
        return SerenityRest.given()
                .header("Cookie", "token=" + token)
                .pathParam("bookingId", bookingid)
                .when()
                .get()
                .then().statusCode(200);
    }

}
