This is the README of CookMe!
For support visit the thread here: http://forums.canarymod.net/?topic=3523.0
Thanks for using!

This plugin sends usage statistics! If you wish to disable the usage stats, look at plugins/PluginMetrics/config.txt!
This plugin is released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0) license.


Standard config:

#Sat May 18 19:29:28 CEST 2013
configuration.debug=false
configuration.messages=true
configuration.permissions=true
configuration.cooldown=30
configuration.preventVanillaPoison=false
configuration.duration.min=15
configuration.duration.max=30
effects.death=4.0
effects.damage=8.0
effects.weakness=8.0
effects.venom=8.0
effects.blindness=8.0
effects.instant_damage=8.0
effects.slowness=8.0
effects.refusing=8.0
effects.slowness_blocks=8.0
effects.confusion=8.0
effects.hungervenom=8.0
effects.wither=8.0
effects.hungerdecrease=8.0
food=RawBeef,RawChicken,RawFish,Pork,RottenFlesh

Commands & Permissions

Permission: /cookme safe
Description: No effects will appear, if a player has got this permission

Permission: /cookme

/cookme reload
Description: Reloads the config

/cookme help
Description: Displays the help

/cookme set <effect> <percentage>
Description: Sets the percentage for the specified effect

/cookme enable permissions
Description: Enables the permissions! (Only OPs or player with the permission can use a specific command)

/cookme enable messages
Description: Enables the messages!

/cookme disable permissions
Description: Disables the permissions! ALL players can use the commands!

/cookme disable messages
Description: Disables the messages!

/cookme set cooldown <value>
Description: Sets the cooldown value in seconds

/cookme set duration min <value>
Description: Sets the minimum duration value in seconds

/cookme set duration max <value>
Description: Sets the maximum duration value in seconds