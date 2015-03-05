#Contraptions

Contraptions goal is to provide a framework for adding increased functionality to blocks in minecraft. At its core it is meant to be highly flexible to the addition of new functional elements to individual minecraft blocks, allowing for the simple creation of a wide range of different kinds of interactive structures. This program will allow tens of thousands of interactive blocks to be added to the minecraft world in an efficient manner giving rise to emergent constructions of complex machinery.

##The Contraption

A contraption is a single minecraft block that pieces together a bunch of widgets and resources. Widgets are functional components that interact with the minecraft world and the resources of the contraption. Resources are simple values within the contraption that represent a range of different properties, they allow widgets to interact with each other and factories to keep track of their current state. Via the composition of multiple widgets and resources contraptions will easily give rise to complex behavior and interaction with the minecraft world and other contraptions.

Potential contraptions include:

1.  A Factory which costs a good bit to make but allows people to execute custom recipe using it. However the factory continously consume energy, and when it runs out it is destroyed! However it will automatically consume fuel held within it to regenerate energy, and may also be energized via a power plant.
1.  A loader, which simply loads all the items in its inventory into the block infront of it. However this takes a lot of energy, and overtime the loader just being around will drain energy as well.
1.  A Unloader, about the same as the loader, but it unloads items from sorrounding blocks into itself instead.
1.  A Power Plant that will provide energy to all contraptions within a radius of itself, that way the contraptions don't have to fuel themselves!
1.  A Trigger, this simply trigger sorrounding contraptions to do something, like have a factory produce a recipe.

##Widgets

A contraption's widgets are what allow it to do things, and each contraption implementation decides how specifically to use the widgets that have been coded. These widgets can respond to events, do things over time, or respond to other widgets in the same contraption or others. Programmatic constraints dictate that widgets should not trigger chunk loads and should not be required to run frequently.

##Tentative Widgets:

1.  Match widget: A widget that when triggered attempts to consume a set of items, but only consumes them if the inventory only contains those items.
1.  Production widget: A widget that when triggered attempts to convert one set of items in the contraption to another set of items
1.  Decay widget: A widget which drains a resource over time.
1.  Generation widget: A widget that will convert a set of items into a resource.
1.  Load widget: Load items from surrounding containers over time
1.  Unload widget: Unloads items to surrounding containers over time
1.  Aura widget: Influence the resources of surrounding contraptions every so often
1.  Trigger widget: Trigger widgets in ajoining functional blocks
1.  Teleportation widget: Teleport the interacting player to nether
1.  Effect widget: Bestow an effect upon the interacting player
