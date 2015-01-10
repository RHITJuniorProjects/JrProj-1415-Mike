#Starting inside user "hullzr@rose-hulman.edu" password "test"

click("1418686395253.png")
if exists("1418686452938.png"):
    print("Zach's tasks displaying correctly")
else:
    print("ERROR: Zach's tasks not displaying correctly")
click("1418686612972.png")
click("1418686625936.png")
click("1418686636513.png")
click("1418705270363.png")
click("1418705287264.png")

#switching to user "watersdr@rose-hulman.edu" password "password"
type("watersdr" + Key.TAB + "password" + Key.ENTER)
click("1418686395253.png")
if exists("1418706386619"):
    print("Donnie's tasks displaying correctly")
else:
    print("ERROR: Donnie's tasks not displaying correctly")