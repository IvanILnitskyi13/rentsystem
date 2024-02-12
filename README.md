# API

To test use -> POSTMAN

## Get all reservation for rental property
### Method Type - GET
- http://localhost:8080/api/reservations/rental-property/:id
  
`Params: id - rental property identifier`

## Get all reservation for tenant name
### Method Type - GET
- http://localhost:8080/api/reservations/tenant/:tenantName

`Params: tenantName - tenant name`

## Create new reservation
### Method Type - POST
- http://localhost:8080/api/reservations/save

`requestBody -> {
    "rentalObjectId": 1,
    "tenantName": "Jolanta Krasowska",
    "startDate": "2024-02-18",
    "endDate": "2024-02-19"
}`

## Update reservation
### Method Type - PUT
- http://localhost:8080/api/reservations/update/:reservationId

`Params: reservationId - reservation identifier`

`requestBody -> {
"rentalObjectId": 1,
"tenantName": "Jolanta Krasowska",
"startDate": "2024-02-18",
"endDate": "2024-02-19"
}`