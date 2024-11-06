# Backend Fall Exam

### Choices:
I added a Long guideId variable to TripDTO so that we can easily link a trip to an existing guide when creating it. This makes it simpler to create trips without changing the method that adds trips to the database.

### What I would have done, if I had time:
#### 6.3 Add the packing items to the response of the endpoint for getting a trip by id.
For 6.3, I would have added entities for the Items and and ItemDAO, so we could save it in our database and then find it there. Then we could have modified our read trip method, to include this as well.
#### 6.4 Add a new endpoint to get the sum of the weights of all packing items for a trip.
I would have done pretty much the same as I would have for 6.3.

### Theoretical questions:
3.3.5 Theoretical question: Why do we suggest a PUT method for adding a guide to a trip instead of a POST method?
Answer: Because PUT is used to update and POST is used to create. So if we send multiple POST requests, it may lead to duplicates, whereas a PUT operation is idempotent, which means that the result of sending the request will always be the same, the guide will be added to the trip.


# API Documentation

## Trip Endpoints

### Populate with data
**Request:**
```
POST http://localhost:7070/api/trips/populate
Authorization: Bearer {{jwt_token}}
```
**Response:**
```
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:43:47 GMT
Content-Type: application/json
Content-Length: 40
{
  "message": "Trips and guides populated successfully."
}
```
### Create a Trip
**Request:**
```
POST http://localhost:7070/api/trips/
Content-Type: application/json
Authorization: Bearer {{jwt_token}}

{
  "name": "Beach Getaway",
  "startTime": "2024-06-15",
  "endTime": "2024-06-22",
  "longitude": 98.765,
  "latitude": 43.21,
  "price": 150.0,
  "category": "BEACH"
}
```
**Response:**
```
HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 10:08:34 GMT
Content-Type: application/json
Content-Length: 161

{
  "id": 3,
  "name": "Beach Getaway",
  "startTime": [
    2024,
    6,
    15
  ],
  "endTime": [
    2024,
    6,
    22
  ],
  "longitude": 98.765,
  "latitude": 43.21,
  "price": 150.0,
  "guideId": null,
  "category": "BEACH"
}
```
### Read All Trips
**Request:**
```
GET http://localhost:7070/api/trips/
Authorization: Bearer {{jwt_token}}
```
**Response:**
```
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:13:41 GMT
Content-Type: application/json
Content-Length: 499

[
  {
    "id": 1,
    "name": "Trip to Paris",
    "startTime": [
      2024,
      5,
      15
    ],
    "endTime": [
      2024,
      5,
      20
    ],
    "longitude": 2.3522,
    "latitude": 48.8566,
    "price": 1500.0,
    "guideId": null,
    "category": "CITY"
  },
  {
    "id": 2,
    "name": "Trip to Miami Beach",
    "startTime": [
      2024,
      6,
      1
    ],
    "endTime": [
      2024,
      6,
      10
    ],
    "longitude": -80.1918,
    "latitude": 25.7617,
    "price": 2500.0,
    "guideId": null,
    "category": "BEACH"
  },
  {
    "id": 3,
    "name": "Beach Getaway",
    "startTime": [
      2024,
      6,
      15
    ],
    "endTime": [
      2024,
      6,
      22
    ],
    "longitude": 98.765,
    "latitude": 43.21,
    "price": 150.0,
    "guideId": null,
    "category": "BEACH"
  }
]

```

### Get Trip by ID
**Request:**
```
GET http://localhost:7070/api/trips/1
Authorization: Bearer {{jwt_token}}
```
**Response:**
```
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:17:51 GMT
Content-Type: application/json
Content-Length: 163

{
  "id": 1,
  "name": "Trip to Paris",
  "startTime": [
    2024,
    5,
    15
  ],
  "endTime": [
    2024,
    5,
    20
  ],
  "longitude": 2.3522,
  "latitude": 48.8566,
  "price": 1500.0,
  "guideId": null,
  "category": "CITY"
}
```
### Update a Trip
**Request:**
```
PUT http://localhost:7070/api/trips/1
Content-Type: application/json
Authorization: Bearer {{jwt_token}}

{
  "name": "Beach Getaway Updated",
  "startTime": [
    2024,
    6,
    15
  ],
  "endTime": [
    2024,
    6,
    25
  ],
  "longitude": 98.765,
  "latitude": 43.21,
  "price": 200.0,
  "category": "BEACH"
}
```
**Response:**
```
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:19:26 GMT
Content-Type: application/json
Content-Length: 169

{
  "id": 1,
  "name": "Beach Getaway Updated",
  "startTime": [
    2024,
    6,
    15
  ],
  "endTime": [
    2024,
    6,
    25
  ],
  "longitude": 98.765,
  "latitude": 43.21,
  "price": 200.0,
  "guideId": null,
  "category": "BEACH"
}
```
### Delete a Trip
**Request:**
```
DELETE http://localhost:7070/api/trips/1
Authorization: Bearer {{jwt_token}}
```
**Response:**
```
HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 10:20:32 GMT
Content-Type: text/plain

<Response body is empty>
```
### Get Trips by Category
**Request:**
```
GET http://localhost:7070/api/trips/category/CITY
Authorization: Bearer {{jwt_token}}
```
**Response:**
```
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:57:13 GMT
Content-Type: application/json
Content-Length: 165
[
  {
    "id": 1,
    "name": "Trip to Paris",
    "startTime": [
      2024,
      5,
      15
    ],
    "endTime": [
      2024,
      5,
      20
    ],
    "longitude": 2.3522,
    "latitude": 48.8566,
    "price": 1500.0,
    "guideId": null,
    "category": "CITY"
  }
]
```
### Get Packing Items by Category
**Request:**
```
GET http://localhost:7070/api/trips/packing-items/category/CITY

```
**Response:**
```
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 11:29:05 GMT
Content-Type: application/json
Content-Encoding: gzip
Content-Length: 460

[
  {
    "name": "City Map",
    "weightInGrams": 100,
    "quantity": 1,
    "description": "Detailed map of popular tourist spots.",
    "category": "city",
    "buyingOptions": [
      {
        "shopName": "Map World",
        "shopUrl": "https://shop5.com",
        "price": 10.0
      }
    ],
    "createdAt": "2024-10-30T17:44:58.547Z",
    "updatedAt": "2024-10-30T17:44:58.547Z"
  },
  {
    "name": "City Guidebook",
    "weightInGrams": 150,
    "quantity": 1,
    "description": "Comprehensive guidebook for city travelers.",
    "category": "city",
    "buyingOptions": [
      {
        "shopName": "Bookstore",
        "shopUrl": "https://shop22.com",
        "price": 20.0
      }
    ],
    "createdAt": "2024-10-30T17:44:58.547Z",
    "updatedAt": "2024-10-30T17:44:58.547Z"
  },
  {
    "name": "Portable Phone Charger",
    "weightInGrams": 150,
    "quantity": 1,
    "description": "Handy charger for long city explorations.",
    "category": "city",
    "buyingOptions": [
      {
        "shopName": "Tech Gadgets",
        "shopUrl": "https://shop5.com",
        "price": 20.0
      }
    ],
    "createdAt": "2024-10-30T17:44:58.547Z",
    "updatedAt": "2024-10-30T17:44:58.547Z"
  },
  {
    "name": "City Metro Card",
    "weightInGrams": 10,
    "quantity": 1,
    "description": "Rechargeable metro card for city transportation.",
    "category": "city",
    "buyingOptions": [
      {
        "shopName": "City Transit",
        "shopUrl": "https://shop6.com",
        "price": 15.0
      }
    ],
    "createdAt": "2024-10-30T17:44:58.547Z",
    "updatedAt": "2024-10-30T17:44:58.547Z"
  },
  {
    "name": "Compact Umbrella",
    "weightInGrams": 200,
    "quantity": 1,
    "description": "Small umbrella, perfect for unexpected showers.",
    "category": "city",
    "buyingOptions": [
      {
        "shopName": "Rainy Days",
        "shopUrl": "https://shop7.com",
        "price": 12.0
      }
    ],
    "createdAt": "2024-10-30T17:44:58.547Z",
    "updatedAt": "2024-10-30T17:44:58.547Z"
  }
]
```


### Template name for request
**Request:**
```
Template request
```
**Response:**
```
Template response
```




### How to run

1. Create a database in your local Postgres instance called `trip`
2. Run the main method in the config.Populate class to populate the database with some data
3. Run the main method in the Main class to start the server on port 7070
4. See the routes in your browser at `http://localhost:7070/routes`
5. Request the `http://localhost:7070/trips` endpoint in your browser to see the list of trips and rooms
6. Use the dev.http file to test the routes, GET/POST/PUT/DELETE requests are available

## Docker commands

```bash
docker-compose up -d
docker-compose down
docker logs -f  watchtower
docker logs watchtower
docker logs hotelAPI
docker logs db
docker container ls
docker rmi <image_id>
docker stop <container_id>
docker rm <container_id>
```
