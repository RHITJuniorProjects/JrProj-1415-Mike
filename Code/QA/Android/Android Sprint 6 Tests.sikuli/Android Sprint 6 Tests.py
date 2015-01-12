##Test assuming user already logged in and assigned to a project
##Test ability to see the burn down chart
click("1421095019568.png")
dragDrop(Pattern("1421095249572.png").exact(),"1421095256867.png")
find("1421095279657.png")
click("1421095296729.png")
click("1421095312578.png")
find("1421095035753.png")
click("1421095049504.png")
click("1421095074666.png")
if exists("1421095384881.png"):
    print("It worked")
else:
    print("It failed")

##Cannot current test second feature because of need to be on another 
##user account and a bug is currently stopping that.


