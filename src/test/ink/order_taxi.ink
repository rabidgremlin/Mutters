VAR address=""

== order_taxi ==


- (order_taxi_loop)
{
  - address == "":
    -> request_address
  else:
    -> order_the_taxi
}
-> END

= request_address
What is the pick up address ?
+ GetAddress
- -> order_taxi_loop
  
= order_the_taxi  
:ORDER_TAXI
Taxi {address} is on its way
-> END  