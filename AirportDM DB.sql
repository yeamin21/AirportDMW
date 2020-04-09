create database [AirportDM]
use [AirportDM]


create table [admin](admin_id varchar(15) not null primary key, admin_pass varchar(20) not null)

CREATE TABLE Aircraft(aircraft_type_code varchar(15) not null primary key,aircraft_type_name varchar(45),aircraft_type_capacity int)

insert into Aircraft(aircraft_type_code, aircraft_type_name, aircraft_type_capacity) values('Boeing737', 'Dreamliner' ,50);


create table Airline(airline_code varchar(15) not null primary key,airline_name varchar(45),airline_country varchar(45))
insert into Airline(airline_code, airline_name, airline_country) values('BB', 'Biman Bangla' ,'Bangladesh');


create table Airline_Aircraft(airline_aircraft_code  varchar(10) primary key,  aircraft_type_code varchar(15) foreign key references Aircraft(aircraft_type_code),airline_code varchar(15) foreign key references 
Airline(airline_code))
insert into Airline_Aircraft(airline_aircraft_code, aircraft_type_code,airline_code) values ('B2BB','Boeing737','BB')


create table Travel_Class(travel_class_code varchar(10) not null primary key,price float)


create table Ticket_Type(ticket_type_code varchar(10) not null primary key,price float)


create table Airport(airport_code varchar(3) not null primary key,airport_name varchar(45),airport_location varchar(45))



create table customer(customer_id varchar(15) not null primary key,customer_name varchar(45) not null,customer_email varchar(45),customer_password varchar(45),
customer_address varchar(45),customer_phone int)


create table Flights(flight_no varchar(5) not null primary key,airline_aircraft_code varchar(10) foreign key references Airline_Aircraft(airline_aircraft_code),origin_airport_code varchar(3) foreign key references airport(airport_code),
destination_airport_code varchar(3) foreign key references airport(airport_code),departure_date_time DATETIME,arrival_date_time DATETIME,base_price float)





CREATE TABLE Booking(booking_status_code int  identity(100,1) not null primary key, is_booked varchar(3) default 'no',customer_id varchar(15) foreign key references customer(customer_id),flight_no varchar(5) foreign key references flights(flight_no))


alter table Booking
add bookingdate date not null

alter table Booking
add bookingagent varchar(3) default null


create table billing(billing_id int identity(1000,1) primary key, booking_status_code int foreign key references booking(booking_status_code),cost decimal(7,2) not null default 0)








create table payment([date] date not null ,billing_id int references billing(billing_id), amount decimal(7,2) default 0)


create table booking_agents (agent_id varchar(15) not null primary key, agent_name varchar(20) not null, agent_password varchar(20) not null);







---------------------------------------------- Queries used in the application---------------------------------
---insert into booking(booking_status_code, customer_id,flight_no) values(

---inserting initial values
insert into admin(admin_id,admin_pass) values('root','root');
insert into admin(admin_id,admin_pass) values('r','r');
insert into booking_agents(agent_id,agent_name, agent_password) values('b','Baker', 'b')

insert into customer(customer_id,customer_name,customer_email,customer_password,customer_address) values('y','Yeamin','yeamin21@outlook.com','y','bd');
insert into  airport(AIRPORT_CODE,AIRPORT_NAME,airport_location) values ('a','air','dhaka');
insert into  airport(AIRPORT_CODE,AIRPORT_NAME,airport_location) values ('b','bir','dhaka');

insert into Ticket_Type (ticket_type_code,price) values('ONEWAY',1);
insert into Ticket_Type (ticket_type_code,price) values('TWOWAY',2);

insert into Travel_Class (travel_class_code,price) values('ECONOMY',0);
insert into Travel_Class (travel_class_code,price) values('BUSINESS',15);

insert into Flights(flight_no,airline_aircraft_code,origin_airport_code,destination_airport_code,departure_date_time,arrival_date_time,base_price) values('B2A2', 'B2BB', 'a','b','2020-04-07 00:00',' 2020-04-08 00:00',40000)





insert into payment(billing_id,	amount,date) values(1001,5000,GETDATE())
insert into payment(billing_id,	amount,date) values(1001,5000,GETDATE())
insert into payment(billing_id,	amount,date) values(1002,5000,GETDATE())




select booking.booking_status_code as booking_status_code,
billing.cost as cost,
booking.flight_no as flight_no,
booking.is_booked as is_booked,
Flights.departure_date_time as departure_date_time,
Flights.arrival_date_time as arrival_date_time,
sum(payment.amount) as paid from booking
inner join billing on Booking.booking_status_code=billing.booking_status_code
inner join Flights on Booking.flight_no=Flights.flight_no 
left join payment on billing.billing_id=payment.billing_id 
where booking.customer_id='y'
group by payment.billing_id, booking.booking_status_code,billing.cost,booking.flight_no,booking.is_booked,flights.departure_date_time,Flights.arrival_date_time


select billing_id, sum(amount) as p from payment group by billing_id
--

select airline_aircraft_code,airline_name,aircraft_type_name from Airline inner join Airline_Aircraft on airline.airline_code=Airline_Aircraft.airline_code
left join Aircraft on Airline_Aircraft.aircraft_type_code=aircraft.aircraft_type_code


--select Flights (package: Admin, Class: FlightScheduleManagement)
select flight_no,base_price,airline.airline_name,aircraft.aircraft_type_name,
departure_date_time,arrival_date_time,origin_airport.airport_name as origin_airport, destination_airport.airport_name as destination_airport
from Flights inner join Airline_Aircraft on flights.airline_aircraft_code=Airline_Aircraft.airline_aircraft_code
inner join Airline on Airline_Aircraft.airline_code=airline.airline_code
inner join Aircraft on airline_aircraft.aircraft_type_code=aircraft.aircraft_type_code
inner join Airport as origin_airport on Flights.origin_airport_code=origin_airport.airport_code
inner join Airport as destination_airport on Flights.destination_airport_code=destination_airport.airport_code



---------
--reference: https://stackoverflow.com/questions/39317296/extract-date-from-datetime-column-sql-server-compact

Select flight_no, departure_date_time,  arrival_date_time, airline_name
from flights
inner join Airline_Aircraft on Flights.airline_aircraft_code=Airline_Aircraft.airline_aircraft_code
inner join Airline on airline_aircraft.airline_code=Airline.airline_code
where origin_airport_code='a' 
and destination_airport_code='b'
and convert(nvarchar, departure_date_time, 23)='2019-12-12' 



select billing_id, billing.booking_status_code,bookingdate,flights.flight_no,customer_id,base_price
from Booking 
inner join Flights on Flights.flight_no=Booking.flight_no
inner join Billing on Billing.Booking_Status_code=Booking.Booking_Status_code





---shows most popular airline

SELECT top 1 sum(cost),airline_name,count(airline_name) as total_bookings
FROM booking 

inner join flights on Booking.flight_no=flights.flight_no
iNNER JOIN airline_aircraft on flights.airline_aircraft_code=airline_aircraft.airline_aircraft_code
inner join airline on airline_aircraft.airline_code=airline.airline_code
inner join billing on billing.booking_status_code=Booking.booking_status_code
group by airline_name

---shows agent with most bookings process

SELECT top 1 agent_name,
COUNT(agent_name) AS booking_processed,
sum(cost) as cost
FROM Booking
INNER JOIN booking_agents ON Booking.bookingagent = booking_agents.agent_id
inner join billing on billing.booking_status_code=Booking.booking_status_code
GROUP BY agent_name




SELECT bookingdate,sum(cost) as sum_total,
COUNT(bookingdate) AS c_booking_date
FROM booking
inner join  billing on billing.booking_status_code=booking.booking_status_code
gROUP BY bookingdate
                  
                   


                 
           