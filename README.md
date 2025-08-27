# Project overview
A small automation framework for mock API site [restful-booker](https://restful-booker.herokuapp.com/apidoc/index.html).</br>
Tests are covering only positive CRUD operations with bookings.</br>
Framework uses Maven as a build and dependency manager, Lombok for reducing amount of boilerplate code, 
JUnit 5 as test runner and Rest Assured for sending requests and assertations.</br>
</br>
Tests are organized into classes extending BaseTest. All constants used in tests stored in TestData class.

**PingTest** : 
- Verifies the health check endpoint (/ping).</br>

**AuthTest** : 
- Tests token creation (/auth).

**BookingTests** (Covers all booking operations (/booking)) :

- GET list of booking without filters
- GET list of bookings with filters
- POST create booking
- GET booking by ID
- PUT full update
- PUT partial update
- DELETE booking