VAR conversation_has_completed_at_least_once = false

-> start


=== start ====
-> all_intents

= all_intents
+ [OneIntent] -> option_one
+ [TwoIntent] -> option_two
+ [ThreeIntent] -> option_three

= option_one
You chose option one
-> restart_conversation

= option_two
You chose option two
+ [OneIntent] -> option_one
+ [TwoIntent] -> option_two
-> restart_conversation

= option_three
You chose option three
-> restart_conversation


=== restart_conversation ===
~ conversation_has_completed_at_least_once = true
-> start