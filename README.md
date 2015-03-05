#Contraptions

Contraptions goal is to provide a framework for adding increased functionality to blocks in minecraft. At its core it is meant to be highly flexible to the addition of new functional elements to individual minecraft blocks, allowing for the simple creation of a wide range of different kinds of interactive structures. This program will allow tens of thousands of interactive blocks to be added to the minecraft world in an efficient manner.

##The Contraption

A Contraption is a single minecraft block that pieces together a bunch of widgets and resources. Widgets are functional components that interact with the minecraft world and the resources of the Contraption. Resources are simple values within the contraption that represent a range of different properties, they allow widgets to interact with each other. Via the composition of multiple widgets and resources contraptions will easily give rise to complex behavior and interaction with the minecraft world and other contraptions.

Potential contraptions include:

1.  A Factory which costs a good bit to make but allows people to execute custom recipe using it. However the factory continously consume energy, and when it runs out it is destroyed! However it will automatically consume fuel held within it to regenerate energy, and may also be energized via a power plant.
1.  A loader, which simply loads all the items in its inventory into the block infront of it. However this takes a lot of energy, and overtime the loader just being around will drain energy as well.
1.  A Unloader, about the same as the loader, but it unloads items from sorrounding blocks into itself instead.
1.  A Power Plant that will provide energy to all contraptions within a radius of itself, that way the contraptions don't have to fuel themselves!
1.  A Trigger, this simply trigger sorrounding contraptions to do something, like have a factory produce a recipe.

##Widgets

A contraption's widgets are what allow
Modules are what enable FunctionalBlocks to do things. They can either be triggered events, say produce some goods, or time based events, say consume some energy. Time based events preferrably are not required to be run frequently, should no require chunk loading, and are executed via Bukkit's scheduler.

##Tentative Modules planned:

1.  Match modile: A module that when triggered attempts to consume a set of items, but only consumes them if the inventory only contains those items.
1.  Production module: A module that when triggered attempts to convert one set of items in the FunctionalBlocks inventory to another set of Items
1.  Drain module: A module which drains an abstract resource over time, when the resource reaches zero it attempts to consume a set of items which will add more resource to the module. There is also a maximum amount of resource the the module can contain. This resource can also be changed via other modules in other FunctionalBlock.
1.  Sink module: Pull items from surrounding containers over time
1.  Source module: Push items to surrounding containers over time
1.  Aura module: Influence the attributes of surrounding containers over time
1.  Trigger module: trigger ajoining functional blocks
1.  Teleportation module: Teleport the interacting player to nether
1.  Effect module: Bestow an effect upon the interacting player