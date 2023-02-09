# SETA
Uber-like system

*The goal of the project is to develop SETA (SElf-driving TAxi service), a peer-to-peer system of self-driving taxis for the citizens of a smart city.*

(The self driving and pathfinding will not be implemented and substituded with a simple 10x10 matrix representing the smart-city and point to point distance calculation)

![OverallArchitectureOfSETA](https://user-images.githubusercontent.com/71270277/217896018-562a714b-8d51-4e4a-88e6-8e7fd898ee6c.png)

The smart city is divided into four districts. The citizens of the smart city use SETA to request a selfdriving taxi that takes them from their current position to a destination point. In SETA, the taxis of the smart city coordinate with each other to decide which taxi will handle each of such requests. Each taxi is also equipped with a sensor that periodically detects the air
pollution level of the smart city. Moreover, every ride consumes the battery level of the taxi. When the residual battery level is too low, the taxi must go to the recharge station of the district in which it is positioned. 

Periodically, the taxis have to communicate to a remote server called Administrator Server information about the air pollution levels, the number of completed
1rides, the number of kilometers driven, and the remaining battery level. 

The SETA administrators (Administrator Client) are in charge ofmonitoring this information through the Administrator Server. Furthermore, through the Administrator Server it is also possible to dynamically register and remove taxis from the system.

## Project structure

The prject is composed of the following different applications:

- ### SETA
A process that simulates the taxi service request generated by the citizens that myst be cinnybcated ti the fleet of self-driving taxis through MQTT.

- ### MQTT Broker
The MQTT broker is used by SETA to communicate to the taxis and vice-versa.
We will use Mosquito as our MQTT broker. https://mosquitto.org/

- ### Taxi
A specific self-driving taxi of the system.

- ### Administrator Server:
REST server that receives statistics from the smart city taxis and dynamically adds/removes taxis from the network.

- ### Administrator Client:
A client that queries the *Adminsitrator Server* to obtain informations about the statiscrs if the taxis and their rides.

## Smart city

The smart city is represented as a 10 × 10 grid, divided into four 5×5 districts. Each cell of the grid represents a square kilometer of the smart city.

![SmartCityRepresentation](https://user-images.githubusercontent.com/71270277/217907440-4390a240-8e22-4f26-bf9f-6833c69455a0.png)

At a specific time, each cell may contain an arbitrary number of taxis (i.e., 0, 1, more than 1). When a taxi is registered within SETA, it will be randomly placed at the recharge station cell of one of the districts.

## MQTT Broker
The MQTT Broker on which SETA relies is online at the following address: 
```
tcp://localhost:1883
```
SETA uses this broker to communicate the ride requests of the smart city
citizens to the taxis. As it will be described in the next section, SETA uses a dedicated topic for the requests originating in each district of the smart city. SETA publishes on these topics, while the taxis subscribe to them in order to receive the ride requests.

## SETA

SETA is a process that simulates the taxi service requests generated by the citizens of the smart city. Specifically, this process generates 2 new ride requests from the citizens every 5 seconds. Every ride request is characterized by:
• ID • Starting Position • Destination Position
Starting and destination points are expressed as the Cartesian coordinates of a smart city’s grid cell. Such coordinates must be randomly generated. Each ride’s starting and destination points may be located in the same district as well as in different districts. The starting and the destination point of a ride must be different. 
### Rides Topic
To communicate the generated rides to the taxis of the smart city, SETA relies on the MQTT Broker presented in Section 3. Specifically, SETA assumes the role of publisher for the following four MQTT topics:
```
- seta/smartcity/rides/district1 
- seta/smartcity/rides/district2
- seta/smartcity/rides/district3
- seta/smartcity/rides/district4
```
Whenever a new ride request with starting point included in the district i is generated, SETA publishes such a request on the following MQTT topic:
```
seta/smartcity/rides/district{i}
```

### Additioal topics
SETA will subscribe (not publishing) to other topics that will help its coordination with taxis in the system

#### Taxi In Travel
Whenever a taxis start traveling, carring a specific ride, will communicate to SETA, in order to prevent SETA publishing again that specific ride.
```
seta/smartcity/traveling
```

#### Taxi Death (Un-controlled termination)
Whenever a taxi terminate in an uncontrolled way, other taxis in the system will communicate to SETA the death of that specific taxi.
```
seta/smartcity/death
```

#### Taxi Presence
```
seta/smartcity/taxi/district1
seta/smartcity/taxi/district2
seta/smartcity/taxi/district3
seta/smartcity/taxi/district4
```
