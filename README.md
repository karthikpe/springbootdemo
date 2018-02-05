# springboot / REST / File demo

A simple controller and service class to read the content of a file based on the user input.
This includes unit tests.

Java version: 1.8

The app can be tested using the following API's

http://localhost:8888/
- Returns all sightings: Only reported year

http://localhost:8888/search
- Default values: year=2000, month=12

http://localhost:8888/search?year=2001&month=11
