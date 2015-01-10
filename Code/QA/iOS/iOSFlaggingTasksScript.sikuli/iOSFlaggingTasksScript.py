#Starting inside user "hullzr@rose-hulman.edu" password "test"
#from homepage

click("1418709054307.png")
if exists("1418710454448.png"):
    print("Incompleted tasks displaying correctly")
else:
    print("ERROR: Incompleted tasks not displaying correctly")

if exists("1418710501385.png"):
    print("Completed tasks displaying correctly")
else:
    print("ERROR: Completed tasks not displaying correctly")