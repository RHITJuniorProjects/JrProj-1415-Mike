#Starting inside user "hullzr@rose-hulman.edu" password "test"
#from homepage
click("1418711796504.png")
click(Pattern("1418712377349.png").targetOffset(114,2))
click("1418712402219.png")
if exists("1418712415990.png"):
    print("Team members displaying correctly")
else:
    print("ERROR: Team members not displaying correctly")
click("1418712477148.png")
click("1418712489871.png")
click(Pattern("1418712502983.png").targetOffset(115,-1))
click("1418712402219.png")
if exists("1418712560241.png"):
    print("Team members displaying correctly")
else:
    print("ERROR: Team members not displaying correctly")