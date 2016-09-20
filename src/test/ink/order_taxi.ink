VAR address=""
VAR taxiNo = ""

== order_taxi ==


- (order_taxi_loop)
{
  - address == "":
    -> request_address
  - else:
    -> order_the_taxi
}
-> END

= request_address
What is the pick up address ?
:SET_REPROMPT Where would you like to be picked up ?
:SET_HINT 123 Someplace Rd
+ GaveAddress
- -> order_taxi_loop
  
= order_the_taxi  
:ORDER_TAXI
Taxi {taxiNo} is on its way
:SET_ACTION OPEN_URL url:http:\/\/trackcab.example.com/t/{taxiNo} 
-> END  