== confused_bot ==
I'm sorry I'm not understanding you at all :( 
If you are in a hurry, please call 555-12345 to order your taxi.
-> END

== confused_bot_with_handover ==
I'm struggling with that one. Do you want me to call our service line for you?
::SET_REPROMPT Would you like me to call our service line?
::SET_HINT Yes or no
::ADD_QUICK_REPLY Yes
::ADD_QUICK_REPLY No
+ [YesIntent]
    Calling our service operators now. Please hold the line.
+ [NoIntent]
    Okay. I'm here if you need me.
- -> END