# Развертывание
1. выполнить команду `.\gradlew clean bootJar` чтобы собрать проект
2. выполнить команду `docker compose up` чтобы развернуть проект

- Spring boot сервер будет доступен по адресу http://localhost:8080/
- БД Postgres будет доступен по адресу http://localhost:5433/

# Зависимости
java 17
'org.postgresql', name: 'postgresql', version: '42.7.4'
'org.projectlombok:lombok'
'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0'

# CRM - система 
В качестве ответа возвращается application/json

В качестве тела запроса используется application/json

В случае возникновения ошибки ответ имеет статус 400, а тело формат:
``` 
{ 
    "errorCode"
    "message" 
} 
``` 
Где errorCode является кодом, характеризующем ошибки.
Возможные варианты ошибок указаны для каждого запроса.

## Seller
### Создание продавца 

POST /seller - отвечает за создание объекта продавец 

Body:
````
{
    "name": "string",
    "contactInfo": "string"
    
}
````
"name" - имя продавца

"contactInfo" - контактные данные продавца 

Response:
Http Status Code: 200

````
{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
}
````
"id" - сгенерированный идентификатор продавца 

Http Status Code:

409 - "CONTACT_INFO_IS_TAKEN" - продавец с такими контактными данными уже существует 

### Получение списка продавцов 
GET /seller/all

Response:
Http Status Codes: 200

````
[{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
},
{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
},]
````
### Получение информации о пользователе 
GET /seller/{id} - получение информации о продавце 

PATH параметры: 
id продавца 

Response: 
Http Status Codes: 200

````
{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
}
````
Http Status Codes:

404 - "OBJECT_NOT_FOUND" - продавец с таким id не найден

### Изменение информации о продавце
PATCH /seller/{id} - изменение информации о продавце 

PATH параметры:
id продавца

Body: 
````
{
    "name": "string",
    "contactInfo": "string"
}
````

Response:
Http Status Codes: 200

````
{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
}
````

Response:
Http Status Codes:
409 - "CONTACT_INFO_IS_TAKEN" - продавец с такими контактными данными уже существует 

### Удаление продавца 
DELETE seller/delete/{id} - удаление продавца

PATH параметры:
id продавца

Body:
````
{
    "id": "uuid"
}
````

Response:
Http Status Codes: 200
```
{
    "message": "Success"
}
```
### Список продавцов, у которых сумма всех транзакции за выбранный период меньше определеноц суммы 

POST seller/worst/...

Body:
````
{
    "Time_period": "LocalDate"
    "amount": "int"
}
````

Response:
Http Status Codes: 200

````
[{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
},
{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
},]
````
Response:
Http Status Codes: 400

"INVALID_TIME_PERIOD" - дата окончания периода меньше даты начала

### Лучший продавец за выбранный период времени 

POST /seller/best/between_dates - выдает продавца с наибольшей суммой по всем транзакциям за заданный промежуток времени 

Body:
````
{
    "beginDay": "LocalDate"
    "endDay": "LocalDate"
    "amount": "int"
}
````
Response:
Http Status Codes: 200

````
{
    "id": "uuid",
    "name": "string",
    "contactInfo": "string",
    "registrationDate": "LocalDateTime"
}
````
Response:
Http Status Codes: 400

"INVALID_TIME_PERIOD" - дата окончания периода меньше даты начала

## Transaction 
### Создание транзакции 
POST /transaction - создает транзакцию 

Body:
````
{
    "sellerId": "uuid"
    "PaymantType": "string"
    "amount": "int"
}
````
Response:
Http Status Codes: 200

```
{
    "id": "uuid",
    "seller":{
        "sellerId": "uuid",
        "name": "string",
        "contactInfo": "string",
        "registrationDate":"LocalDateTime"
    },
    "amount": "int",
    "PaymantType": "string",
    "transactionDate": "LocalDateTime"
}
```
Response:
Http Status Codes: 400
"INVALID_AMOUNT" - сумма транзакции меньше нуля 

## Получение информации о транзакции 

GET /transaction/{id} - получение транзакции по её id

Response:
Http Status Codes: 200

```
{
    "id": "uuid",
    "seller":{
        "sellerId": "uuid",
        "name": "string",
        "contactInfo": "string",
        "registrationDate":"LocalDateTime"
    },
    "amount": "int",
    "PaymantType": "string",
    "transactionDate": "LocalDateTime"
}
```

Response:

Http Status Codes: 400 

"OBJECT_NOT_FOUND" -  транзакции не существует 

## Получение информации о всех транзакциях 

GET /transaction - получение списка всех транзакций 

Response: 
Http Status Codes: 200

```
[{
    "id": "uuid",
    "seller":{
        "sellerId": "uuid",
        "name": "string",
        "contactInfo": "string",
        "registrationDate":"LocalDateTime"
    },
    "amount": "int",
    "PaymantType": "string",
    "transactionDate": "LocalDateTime"
},
{
    "id": "uuid",
    "seller":{
        "sellerId": "uuid",
        "name": "string",
        "contactInfo": "string",
        "registrationDate":"LocalDateTime"
    },
    "amount": "int",
    "PaymantType": "string",
    "transactionDate": "LocalDateTime"
}
```
## Получение всех транзакций определенного продавца
GET /seller/{sellerId}

Response:
Http Status Codes: 200

```
[{
    "id": "uuid",
    "seller":{
        "sellerId": "uuid",
        "name": "string",
        "contactInfo": "string",
        "registrationDate":"LocalDateTime"
    },
    "amount": "int",
    "PaymantType": "string",
    "transactionDate": "LocalDateTime"
},
{
    "id": "uuid",
    "seller":{
        "sellerId": "uuid",
        "name": "string",
        "contactInfo": "string",
        "registrationDate":"LocalDateTime"
    },
    "amount": "int",
    "PaymantType": "string",
    "transactionDate": "LocalDateTime"
}
```

