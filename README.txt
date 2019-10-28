Virtual World Project
CSC 203, Summer '19

I put getCurrentImage() in Entity and made it a static method instead of
splitting it into two methods because I felt that it would be redundant.
It just made more sense to me because Background can always statically
call Entity.getCurrentImage() instead of having two overlapping methods.