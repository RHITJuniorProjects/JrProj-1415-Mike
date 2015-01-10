##Tests ran from user already signed in
##Begin for view all tasks assigned for me
find("1418587468765.png")
click("1418587474047.png")
if exists("1418766819254.png"):
    print("It worked")
else:
    print("It failed")
##End test
click("1418587489657.png")
wait(3)

##Test view all members of a project
click("1418587294365.png")
wait(5)
click("1418587316674.png")
wait(5)
if exists("1418587348437.png"):
    print("It worked")
else:
    print("It failed")
##End test

click("1418587358380.png")



##Begin test for making sure my tasks are flagged
click("1418587507533.png")
dragDrop("1418587539411.png","1418587546096.png")
wait(3)
click("1418587566337.png")
click("1418587580436.png")
click("1418587589325.png")
if exists("1418587598508.png"):
    print("It worked")
else:
    print("It failed")
##End Test




