#checking for lines of code displaying
#script starts at the "Projects" page 
#using user "test2@test.com" "test"

#using Henry - Platform project
find("1415322757940.png")
click("1415322770367.png")
find(Pattern("1415327103115.png").targetOffset(21,-3))
click(Pattern("1415327103115.png").targetOffset(21,-3))
if exists("1415322824444.png"):
    print("Lines of code displaying correctly at project level")
else:
    print("ERROR: Lines of code not displaying correctly for project level")

click("1415322929874.png")

#using iOS 7 project
click(Pattern("1415326739775.png").targetOffset(128,-1))
find(Pattern("1415327103115.png").targetOffset(21,-3))
click(Pattern("1415327103115.png").targetOffset(21,-3))
if exists("1415326912810.png"):
    print("Lines of code displaying correctly at project level")
else:
    print("ERROR: Lines of code not displaying correctly for project level")