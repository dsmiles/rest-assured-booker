# Issues Found

While testing the Restful Booker API, I encountered several issues that are worth noting:

Note: I'm using the Docker image, therefore the endpoint URLs will be `http://localhost:3001/` and not `https://restful-booker.herokuapp.com/`

## 1. Endpoint: 'https://localhost:3001/auth'

### 1.1 Bad credentials

The /auth endpoint incorrectly responds with an HTTP 200 status code when given bad credentials. 

```http request
HTTP/1.1 200 OK
X-Powered-By: Express
Content-Type: application/json; charset=utf-8
Content-Length: 28
ETag: W/"1c-J3EiwfZwVQjKTKvpvazUfTni8fI"
Date: Wed, 06 Mar 2024 11:53:30 GMT
Connection: keep-alive

{
    "reason": "Bad credentials"
}
```
This is incorrect behavior. The API should return an HTTP 401 status code when given bad credentials.


## 2. Endpoint: `https://localhost:3001/booking`

### 2.1 General issues

#### 2.1.1 Authorization
The API does not implement the normal [challenge-response mechanism](https://datatracker.ietf.org/doc/html/rfc2617#section-1.2) to indicate explicitly when the  consumer needs to authenticate to access the resource. By default, REST-assured waits for the server to challenge before sending the credentials. For this reason, I decided to use the _preemptive()_ directive to instruct REST-assured to send the credentials without waiting for an Unauthorized response.

```http request
given().auth()
  .preemptive()
  .basic("user1", "user1Pass")
  .when()
  // ...
```

#### 2.1.2 HTTP Status Code 418 I am a Teapot
Experimentation has revealed that the Restful Booker API generates an `HTTP 418` status code response with the message "I'm a Teapot" in the following scenarios:

- Whenever an HTTP POST, PUT, or PATCH request is received by the application that contains an unsupported `Accept:` header.

Here's an example HTTP request and response demonstrating the 418 status code:
```http request
  HTTP/1.1 418 I'm a Teapot
  X-Powered-By: Express
  Content-Type: text/plain; charset=utf-8
  Content-Length: 12
  ETag: W/"c-MoOTQ9Zl5bv7AjXgAKmn0YHM8sY"
  Date: Fri, 01 Mar 2024 13:34:33 GMT
  Connection: keep-alive

  I'm a teapot
```

### 2.2 Get Booking IDs

#### 2.2.1 check in dates
Filtering by `checkin` date does not work correctly. According to the documentation, it should return bookings that have a checkin date '_greater than or equal to_' the given date. However, experimentation reveals that it only returns bookings based on the '_greater than_' condition.

This discrepancy has been confirmed by examining the source code, where the MongoDB database query incorrectly uses `$gt` instead of `$gte` when processing the checkin dates. Here's an excerpt from the source code demonstrating the issue:

```javascript
  if (typeof(req.query.checkin) != 'undefined') {
      query["bookingdates.checkin"] = {$gt: new Date(req.query.checkin).toISOString()}
  }
```

#### 2.2.2 check out dates
Filtering by `checkout` date does not work correctly. According to the documentation, it should return bookings that have a checkout date 'less than or equal to' the given date. However, experimentation reveals that it only returns bookings based on the 'less than' condition.

This discrepancy has been confirmed by examining the source code, where the MongoDB database query incorrectly uses `lt` instead of `lte` when processing the checkout dates. Here's an excerpt from the source code demonstrating the issue:

```javascript
  if(typeof(req.query.checkout) != 'undefined'){
    query["bookingdates.checkout"] = {$lt: new Date(req.query.checkout).toISOString()}
  }
```

#### 2.2.3 Filtering bookings by date with invalid value
Filtering by a date with an invalid value `"2024-00-00"` returns a `500 Internal Server Error` status code. It would be more appropriate to return a `400 Bad Request` status code. 

```http request
  HTTP/1.1 500 Internal Server Error
  X-Powered-By: Express
  Content-Type: application/json; charset=utf-8
  Content-Length: 36
  ETag: W/"24-5Z
```

#### 2.2.4 Wrong Content-Type header for XML payload response
When retrieving a booking in XML format, set via the `Accept` header, the response has the wrong `Content-Type` header set for an XML payload.
The `Content-Type:` is incorrectly set to `text/html; charset=utf-8`, whereas it should be `application/xml`.

```http response
HTTP/1.1 200 OK
X-Powered-By: Express
Content-Type: text/html; charset=utf-8
Content-Length: 353
ETag: W/"161-lSEjFSnoC+ULM5jc7L9Pyp35x/4"
Date: Wed, 06 Mar 2024 11:53:19 GMT
Connection: keep-alive

<booking>
  <firstname>Cristina</firstname>
  <lastname>Shields</lastname>
  <totalprice>399</totalprice>
  <depositpaid>false</depositpaid>
  ...
```


### 2.3 Create Booking

#### 2.3.1 Total price
The total price value cannot be a floating-point number; precision is lost during saving. The JSON specification suggests that JSON Numbers can contain fractional values and therefore should be represented by a Double datatype.
 
#### 2.3.2 Booking with invalid Checkin and checkout dates
The `checkin` and `checkout` dates are validated, preventing the creation of invalid bookings, but the API returns a `200 OK` status code instead of the more appropriate `400 Bad Request`.

#### 2.3.3 Internal Server Error response when null values in booking  
The API responds with an `500 Internal Server Error`, when a booking payload is received containing `null` values. Server side validation SHOULD stop this. It would be more appropriate to return `400 Bad Request`.

#### 2.3.4 Invalid Content-Type 
The API responds with `418 I'm a Teapot` when `Accept` header contains invalid content-type. See the [HTTP Status Code 418 I am a Teapot](#http-status-code-418-i-am-a-teapot) section for more details.


### 2.4 Update Booking

#### 2.4.1 Non-existent booking
Attempting an update `PUT` of a non-existent booking returns a `405 Method Not Allowed`. It would be more appropriate to return a `404 Not Found` status code.


### 2.5 Partial Update Booking
 
#### 2.4.1 Non-existent booking
Attempting a partial update `PATCH` of a non-existent booking returns a `405 Method Not Allowed`. It would be more appropriate to return a `404 Not Found` status code.


## 3. Endpoint: `https://localhost:3001/booking/1`

### 3.1 Delete Booking

#### 3.1.1 Response code deleting a booking resource
A successful delete action returns a `201 Created` status code with no response body. This is an inappropriate choice for a delete operation, since a `201` status code is typically used when creating new data. 
The more appropriate response should either be: 

- `200 OK` status code indicates that the resource has been removed and it will include a message for the client that describes the status.  
- `204 No Content` status code indicates that the resource has been removed but there is no message body to further describe the action or the status. 

#### 3.1.2 Non-existent booking
Attempting to delete a non-existent booking returns a `405 Method Not Allowed`. It would be more appropriate to return a `404 Not Found` status code. You would only use a `405 Method Not Allowed` when attempting to delete ALL bookings.

#### 3.1.3 Wrong status code when attempting to delete collection
Attempting to delete the entire booking collection returns a '404 Not found'. It would be more appropriate to return a `405 Method Not Supported` status code. Deleting the entire collection is not a desirable action and should be prevented. 


## 4. Endpoint: `https://localhost:3001/ping`

### 4.1 Health Check
The ping endpoint returns a `201 Created` status code. Using a `200 OK` status code would be a more suitable choice, since you are not creating any data on the system. This should be an idempotent operation.

