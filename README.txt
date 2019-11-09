Virtual World Project
CSC 203, Summer '19

'''~~~[Project 3 Justifications]~~~''' -> 
  Scalability
  	  ->The purpose of the two additions below is because I was considering that
	    it's possible in the future to have other types of Movable objects, or
		that I maybe want to add more attributes, or methods to the Miner
		abstract class.
		  ->I decided to create a MovableEntity abstract class because not
			everything that is animated, moves. An example of this is Quake.
			Quake is an animated object but it doesn't move at all.
		  ->I decided to create a MinerEntity abstract class because Miners are the
			only things that have the resourceLimit and resourceCount attributes.

  Cleanliness
	  ->I defined the scheduleActions method in ActiveEntity, however I overrode it
		in AnimatedEntity because ALL active entities only schedule activities,
		while ALL animatable entities schedule both activities and animations.
	  ->I separated the getCurrentImage from Background. Original I just kept the
	    if-else statement that checked the class of the entity, now Entity and
		Background has their own getCurrentImage.