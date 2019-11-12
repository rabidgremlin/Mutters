// INK script to test long term session attribute functions

-> start

// holds the order number for the current conversation
VAR order_number = ""

=== start ===
+ [CreateOrderIntent] -> create_order
+ [CheckStatusIntent] -> check_status
+ [GetPriceIntent] -> get_price
-> END

=== create_order ===
~ order_number = 123456 // fake order creation
Your order {order_number} has been created!
::SET_LONG_TERM_ATTR name::currentorder value::{order_number}
-> END

=== check_status ===
::GET_LONG_TERM_ATTR name::currentorder var::order_number
{
  - order_number == "":
    -> get_order_number_for_status_check 
  - else:
    -> check_order_status  
}
-> END

= get_order_number_for_status_check
What is the order number of the order you want to check the status of ?
// the rest of this divert would:
//  * capture the order number via a number intent
//  * set the order_number variable
//  * jump to display_order_status
-> END

= check_order_status
For order {order_number} ?
::SET_REPROMPT For order {order_number} ? Please answer Yes or No.
::SET_HINT Yes or No
::ADD_QUICK_REPLY Yes
::ADD_QUICK_REPLY No
+ [YesIntent]
  -> display_order_details
+ [NoIntent]
 // customer wants to talk about another order so unset current order long term attribute
 ::REMOVE_LONG_TERM_ATTR name::currentorder
 -> get_order_number_for_status_check
-> END

= display_order_details
// return dummy status
Order {order_number} is currently being packed.
// store current order for next conversation in session
::SET_LONG_TERM_ATTR name::currentorder value::{order_number}
-> END


=== get_price ===
Do you want the price for blue widgets ?
// no reprompt so we can use this conversation path for default reprompt testing
+ [YesIntent]
  Blue Widgets cost $23
+ [NoIntent]
  All non-blue widgets cost $12
- -> END