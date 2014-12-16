#Starting inside user "hullzr@rose-hulman.edu" password "test"
#from homepage

click("1418709054307.png")
if exists("1418709106334.png"):
    print("Zach's tasks displaying correctly")
else:
    print("ERROR: Zach's tasks not displaying correctly")
click("1418709121277.png")
click("1418709175108.png")
click("1418709193104.png")
click("1418709211405.png")
wait(3)
click("1418709233836.png")
wait(3)

#switching to user "watersdr@rose-hulman.edu" password "password"
type("watersdr" + Key.TAB + "password" + Key.ENTER)
click("1418709054307.png")
if exists("1418709295678.png"):
    print("Donnie's tasks displaying correctly")
else:
    print("ERROR: Donnie's tasks not displaying correctly")