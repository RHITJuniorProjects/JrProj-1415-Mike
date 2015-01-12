#Starting inside user "hullzr@rose-hulman.edu" password "test"
#from homepage

click(Pattern("1418709054307.png").similar(0.72))
if exists(Pattern("1418768072878.png").similar(0.60)):
    print("Incompleted tasks displaying correctly")
else:
    print("ERROR: Incompleted tasks not displaying correctly")

if exists(Pattern("1418710501385.png").similar(0.48)):
    print("Completed tasks displaying correctly")
else:
    print("ERROR: Completed tasks not displaying correctly")