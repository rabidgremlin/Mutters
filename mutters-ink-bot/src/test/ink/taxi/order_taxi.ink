VAR address=""
VAR taxiNo = ""

== order_taxi ==
- (order_taxi_loop)
{
 - address == "": -> request_address 
 - else: -> order_the_taxi
}

= request_address
What is the pick up address ?
::SET_REPROMPT Where would you like to be picked up ?
::SET_HINT 123 Someplace Rd
+ [GaveAddress]
- -> order_taxi_loop

= order_the_taxi  
::ORDER_TAXI
Taxi {taxiNo} is on its way
::ADD_ATTACHMENT type::link url::http:\/\/trackcab.example.com/t/{taxiNo} title::Track your taxi here
::ADD_QUICK_REPLY Where is my taxi?
::ADD_QUICK_REPLY Cancel my taxi
-> END